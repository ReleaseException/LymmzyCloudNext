package de.gommzy.cloud.cloud.socket;

import de.gommzy.cloud.cloud.cloud.Cloud;
import de.gommzy.cloud.cloud.minecraft.Minecraft;
import de.gommzy.cloud.cloud.minecraft.ServerManager;
import de.gommzy.cloud.config.Config;

import java.net.Socket;

public class ClientReciver {
    private String cloudUUID;

    ClientReciver(ClientHandler clientHandler, Socket socket) {
        try {
            while(socket.isConnected()) {
                String read = clientHandler.read();
                if (read == null) {
                    break;
                }
                new Thread(() -> {
                    final String[] args = read.split(" ");
                    switch (args[0]) {
                        case "login" -> {
                            final String pswd = args[1];
                            System.out.println(Config.getOptionAsString("cloudpassword").equals(pswd));
                            if (!pswd.equals(Config.getOptionAsString("cloudpassword"))) {
                                System.out.println("[Wrong-Pswd] " + clientHandler.getAddress());
                                clientHandler.unregister();
                            } else {
                                System.out.println("[Register] " + clientHandler.getAddress());
                            }
                        }
                        case "registercloud" -> {
                            Cloud cloud = new Cloud(clientHandler.getAddress(), clientHandler);
                            cloudUUID = Minecraft.addCloud(cloud);
                            clientHandler.write("registercloud " + cloudUUID);
                        }
                        case "registerbungee" -> {
                            ServerManager.setServerClientHanlder(clientHandler, 25565);
                        }
                        case "registerspigot" -> {
                            ServerManager.setServerClientHanlder(clientHandler, Integer.parseInt(args[1]));
                        }
                        case "startedserver" -> {
                            ServerManager.servers.get(args[1]).startedServer(Integer.parseInt(args[2]));
                        }
                        case "setscore" -> {
                            Minecraft.clouds.get(cloudUUID).avaibleScore = Integer.parseInt(args[1]);
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
