package com.releasenetworks.bridge.socket;

import com.releasenetworks.executor.annotations.LymmzyCloud;
import de.gommzy.cloud.Main;
import de.gommzy.cloud.config.Config;
import org.json.JSONObject;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

@LymmzyCloud(mode = "all")
public class Channel {

    public Channel() {
        this.start();
    }

    public void start() {
        if (Main.executionType == de.gommzy.cloud.LymmzyCloud.ExecutionType.COMBINED || Main.executionType == de.gommzy.cloud.LymmzyCloud.ExecutionType.CONTROLLER) {
            try {
                ServerSocket serverSocket = new ServerSocket(Config.getOptionAsInt("httpport"));
                Thread thread = new Thread(() -> {
                    try {
                        try {
                            System.out.println("Waiting for Proxy to connect");
                            new ChannelHandler(serverSocket.accept());
                        } catch (Exception exc) {
                            exc.printStackTrace();
                        }
                    } catch (Exception exc) {
                        exc.printStackTrace();
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
                JSONObject recivedPacket = new JSONObject(clientHandler.read());
                if (recivedPacket.toString() == null) {
                    break;
                }
                Thread theread = new Thread(() -> {
                    String protocol = recivedPacket.getString("packet");
                    String authentication = recivedPacket.getString("authkey");
                    switch (protocol) {
                        case "PING" -> {
                            clientHandler.write("PONG");
                        }
                        case "login" -> {
                            final String pswd = authentication;
                            System.out.println(Config.getOptionAsString("proxypassword").equals(pswd));
                            if (!pswd.equals(Config.getOptionAsString("proxypassword"))) {
                                System.out.println("[Wrong-Pswd] " + clientHandler.getAddress());
                                clientHandler.unregister();
                            } else {
                                System.out.println("[Register] " + clientHandler.getAddress());
                            }
                        }
                    }

                });
                theread.setName("Bridge - Listenerthread :: " + theread.getId());
                System.out.println(theread.getName());
                theread.start();
            }
            clientHandler.unregister();
        } catch (final Exception exc) {
            exc.printStackTrace();
            clientHandler.unregister();
        }
    }
}
