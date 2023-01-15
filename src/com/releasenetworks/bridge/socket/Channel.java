package com.releasenetworks.bridge.socket;

import com.releasenetworks.bridge.protocol.CloudProtocol;
import com.releasenetworks.bridge.protocol.ProtocolPacket;
import com.releasenetworks.executor.annotations.LymmzyCloud;
import com.releasenetworks.logger.Logger;
import de.gommzy.cloud.Main;
import de.gommzy.cloud.cloud.service.ServiceExecutor;
import de.gommzy.cloud.cloud.service.ServiceRegistry;
import de.gommzy.cloud.cloud.templates.configuration.TemplateConfiguration;
import de.gommzy.cloud.config.Config;
import org.json.JSONObject;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@LymmzyCloud
public class Channel {

    public static ChannelHandler channelHandler = null;
    public static Map<String, List<ProtocolPacket>> pendingRequests = new ConcurrentHashMap<>();

    public Channel() {
        this.start();
    }

    public void start() {
        if (Main.executionType == de.gommzy.cloud.LymmzyCloud.ExecutionType.COMBINED || Main.executionType == de.gommzy.cloud.LymmzyCloud.ExecutionType.CONTROLLER) {
            try {
                ServerSocket serverSocket = new ServerSocket(Config.getOptionAsInt("httpport"));
                Thread thread = new Thread(() -> {
                    while (!serverSocket.isClosed()) {
                        try {
                            try {
                                System.out.println("Waiting for Proxy to connect");
                                channelHandler = new ChannelHandler(serverSocket.accept());
                            } catch (Exception exc) {
                                exc.printStackTrace();
                            }
                        } catch (Exception exc) {
                            exc.printStackTrace();
                        }
                    }
                });
                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void set(ChannelHandler clientHandler, Socket socket) {
        try {
                while(socket.isConnected()) {
                    String read = clientHandler.read();
                    if (!read.split("")[0].equals("{")) return;
                    JSONObject recivedPacket = new JSONObject(read);
                    if (recivedPacket.toString() == null) {
                        break;
                    }

                    switch (CloudProtocol.getProtocol(recivedPacket.getString("packet"))) {
                        case PING -> {
                            clientHandler.write("PONG");
                        }
                        case PROXY_LOGIN -> {
                            final String pswd = recivedPacket.getString("authkey");
                            if (!pswd.equals(Config.getOptionAsString("proxypassword"))) {
                                System.out.println("[Wrong-Pswd] " + clientHandler.getAddress());
                                clientHandler.closeNode();
                            } else {
                                Logger.log("Bridgechannel %s has been registered", Logger.Level.INFO, clientHandler.getAddress());
                                for (ProtocolPacket pendingPacket : Channel.pendingRequests.get("dummy")) {
                                    Thread.sleep(1000);
                                    channelHandler.writeAsPacket(pendingPacket);
                                    //Channel.pendingRequests.get("dummy").remove(pendingPacket);
                                }
                            }
                        }
                        case PROXY_STATS -> {
                            //System.out.println(recivedPacket.toString());
                        }
                        case PROXY_PING -> {
                            //Nothing because console spam p:
                        }
                        case PROXY_JOIN -> {
                            String UUID = recivedPacket.getString("uuid");
                            String name = recivedPacket.getString("name");
                            Logger.log("%s (%s) connected to the network", Logger.Level.INFO, name, UUID);
                        }
                        case PROXY_SWITCH -> {
                            String UUID = recivedPacket.getString("uuid");
                            String name = recivedPacket.getString("name");
                            String previousServer = recivedPacket.getString("previousServer");
                            String newServer = recivedPacket.getString("currentServer");
                            Logger.log("%s (%s) switched from %s to %s", Logger.Level.INFO, name, UUID, previousServer, newServer);
                        }
                        case PROXY_QUIT -> {
                            String UUID = recivedPacket.getString("uuid");
                            String name = recivedPacket.getString("name");
                            Logger.log("%s (%s) disconnected to the network", Logger.Level.INFO, name, UUID);
                        }
                        case SERVER_START_REQUEST -> {
                            if (recivedPacket.getString("template") == null) {
                                TemplateConfiguration configuration = de.gommzy.cloud.LymmzyCloud.configurationMap.get(recivedPacket.getString("template"));
                                ServiceExecutor.createService(configuration, null);
                            }

                        }
                    }
                }
            clientHandler.closeNode();
        } catch (final Exception exc) {
            exc.printStackTrace();
            clientHandler.closeNode();
        }
    }
}
