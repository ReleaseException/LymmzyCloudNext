package de.gommzy.cloud.cloud.service;

import com.releasenetworks.bridge.protocol.CloudProtocol;
import com.releasenetworks.bridge.protocol.ProtocolPacket;
import com.releasenetworks.bridge.socket.Channel;
import com.releasenetworks.executor.exceptions.LymmzyCloudException;
import com.releasenetworks.utils.FolderUtils;
import com.releasenetworks.utils.SystemProperties;
import de.gommzy.cloud.LymmzyCloud;
import de.gommzy.cloud.cloud.cloud.Node;
import de.gommzy.cloud.cloud.templates.configuration.TemplateConfiguration;
import de.gommzy.cloud.config.Config;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Service {

    private final String serviceName;
    private final Process process;
    private final TemplateConfiguration configuration;
    private final Node node;

    public Service(String serviceName, Process process, TemplateConfiguration configuration, Node node) {
        this.serviceName = serviceName;
        this.process = process;
        this.configuration = configuration;
        this.node = node;
    }

    public String getServiceName() {
        return serviceName;
    }

    public Process getProcess() {
        return process;
    }

    public TemplateConfiguration getConfiguration() {
        return configuration;
    }

    public Node getNode() {
        return this.node;
    }

    public synchronized void registerService() {
        LymmzyCloud.services.get(configuration).add(this);
        ProtocolPacket packet = new ProtocolPacket(CloudProtocol.SERVER_START_REQUEST, new JSONObject()
                .put("address", "127.0.0.1")
                .put("port", ((int) (int) configuration.getStartPortRange()) + Integer.parseInt(serviceName.split("-")[1]) - 1)
                .put("serviceName", serviceName)
                .put("isRequired", configuration.isFallbackHost()));
        if (Channel.channelHandler == null) {
            Channel.pendingRequests.get("dummy").add(packet);
        } else {
            Channel.channelHandler.writeAsPacket(packet);
        }
    }

    public synchronized void closeService(boolean shutdown) {
        CompletableFuture.supplyAsync(() -> {
            try {
                PrintWriter writer = new PrintWriter(this.getProcess().getOutputStream());
                writer.println("stop");
                writer.flush();
                Thread.sleep(5000);
                this.getProcess().destroy();
                if (Channel.channelHandler != null) {
                    Channel.channelHandler.writeAsPacket(new ProtocolPacket(CloudProtocol.SERVER_CLOSE_REQUEST, new JSONObject()
                            .put("address", "127.0.0.1")
                            .put("port", (int) configuration.getStartPortRange() + Integer.parseInt(serviceName.split("-")[1]) - 1)
                            .put("serviceName", serviceName)));
                }
                try {
                    if (!this.configuration.isStaticService()) {
                        Thread.sleep(8000);
                        FolderUtils.deleteDirectory(SystemProperties.getUserDirectory() + "/" + Config.getOptionAsString("tempFolder") + "/" + serviceName);
                    }
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }).thenAccept(rs -> {
            LymmzyCloud.services.get(configuration).remove(rs);
            if (!shutdown) {
                try {
                    ServiceRegistry.resize();
                } catch (IOException | LymmzyCloudException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public String toString() {
        return "Service{" +
                "serviceName='" + serviceName + '\'' +
                ", process=" + process +
                ", configuration=" + configuration +
                '}';
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
