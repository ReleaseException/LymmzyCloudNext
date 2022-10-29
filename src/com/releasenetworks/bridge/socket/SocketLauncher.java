package com.releasenetworks.bridge.socket;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.releasenetworks.bridge.packet.BridgePacket;
import com.releasenetworks.bridge.protocol.CloudProtocol;
import com.releasenetworks.executor.annotations.LymmzyCloud;
import de.gommzy.cloud.config.Config;

@LymmzyCloud
public class SocketLauncher {

    private static final Configuration configuration = new Configuration();
    private static SocketIOServer server = new SocketIOServer(configuration);

    public SocketLauncher() {
        if (!Config.getOptionAsBoolean("client")) {
            configuration.setHostname(Config.getOptionAsString("cloudhost"));
            configuration.setPort(Config.getOptionAsInt("httpport"));

            server = new SocketIOServer(configuration);

            server.addEventListener("lymmzycloud", BridgePacket.class, (client, data, sender) -> {
                System.out.println(data);
                if (data.getProtocol() != null) {
                    switch (data.getProtocol()) {
                        case BROADCAST -> {
                            server.getBroadcastOperations().sendEvent(CloudProtocol.BROADCAST.getProtocol(), data);
                        }
                    }
                }
            });
            server.startAsync().syncUninterruptibly();
            System.out.println("[ReleaseBridge] Started Releasebridge socket!");
        }
    }

    public SocketIOServer getBridgeServer() {
        return server;
    }
}