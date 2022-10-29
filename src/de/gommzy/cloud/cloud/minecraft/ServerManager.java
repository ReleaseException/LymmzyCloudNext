package de.gommzy.cloud.cloud.minecraft;

import de.gommzy.cloud.cloud.cloud.Cloud;
import de.gommzy.cloud.cloud.cloud.ServerType;
import de.gommzy.cloud.cloud.socket.ClientHandler;

import java.util.HashMap;

public class ServerManager {
    public enum ServerTypee{ SPIGOT, PAPER, BUNGEE, VELOCITY };

    public static HashMap<String, Server> servers = new HashMap<>();

    public static Server autoCreateServer(String template,String serverName, ServerType servertype) {
        int biggest = 1000;
        ClientHandler clientHandler = null;
        for (Cloud cloud : Minecraft.clouds.values()) {
            if (biggest > cloud.avaibleScore) {
                clientHandler = cloud.clientHandler;
            }
        }

        if (clientHandler == null) {
            System.out.println("No Hostsystem avaible.");
            return null;
        }

        System.out.println("[START-SERVER] Start " + serverName + " on " + clientHandler.getAddress() + " (AvaibleScore: " + biggest + ")");

        Server server = createServer(clientHandler, template, serverName, servertype);
        return server;
    }

    public static void setServerClientHanlder(ClientHandler clientHandler, int port) {
        for (Server server : servers.values()) {
            if (server.address.equals(clientHandler.getAddress()) && server.port == port) {
                System.out.println("[CONNECTED-SERVER] " + server.serverName);
                server.serverClientHandler = clientHandler;
                if (server.serverType == ServerType.BUNGEECORD) {
                    for (Server loop : servers.values()) {
                        if (loop.serverType == ServerType.PAPER) {
                            clientHandler.write("addserver " + loop.serverName + " " + loop.address + " " + loop.port);
                            if (loop.serverClientHandler != null) {
                                loop.serverClientHandler.write("addbungee " + server.address);
                            }
                        }
                    }
                } else {
                    for (Server loop : servers.values()) {
                        if (loop.serverType == ServerType.BUNGEECORD) {
                            clientHandler.write("addbungee " + loop.address);
                            if (loop.serverClientHandler != null) {
                                loop.serverClientHandler.write("addserver " + server.serverName + " " + server.address + " " + server.port);
                            }
                        }
                    }
                }
            }
        }
    }

    public static Server createServer(ClientHandler clientHandler, String template, String serverName, ServerType servertype) {
        String systemServerName = null;
        int i = 0;
        while (systemServerName == null || servers.containsKey(systemServerName)) {
            i++;
            systemServerName = serverName + "-" + i;
        }

        Server server = new Server(systemServerName, template, clientHandler, clientHandler.getAddress(), servertype);
        servers.put(systemServerName, server);
        return server;
    }
}
