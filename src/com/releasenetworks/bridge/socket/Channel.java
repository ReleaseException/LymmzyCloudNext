package com.releasenetworks.bridge.socket;

import com.releasenetworks.executor.annotations.LymmzyCloud;
import de.gommzy.cloud.config.Config;

import java.net.ServerSocket;
import java.net.Socket;

@LymmzyCloud
public class Channel {

    public void start() {
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

    public static void set(ChannelHandler clientHandler, Socket socket) {
        try {
            while(socket.isConnected()) {
                String read = clientHandler.read();
                if (read == null) {
                    break;
                }
                new Thread(() -> {
                    final String[] args = read.split(" ");
                    switch (args[0]) {
                        case "PING" -> {
                            clientHandler.write("PONG");
                        }
                        case "login" -> {
                            final String pswd = args[1];
                            System.out.println(Config.getOptionAsString("proxypassword").equals(pswd));
                            if (!pswd.equals(Config.getOptionAsString("proxypassword"))) {
                                System.out.println("[Wrong-Pswd] " + clientHandler.getAddress());
                                clientHandler.unregister();
                            } else {
                                System.out.println("[Register] " + clientHandler.getAddress());
                            }
                        }
                    }

                }).start();
            }
            clientHandler.unregister();
        } catch (final Exception exc) {
            exc.printStackTrace();
            clientHandler.unregister();
        }
    }
}
