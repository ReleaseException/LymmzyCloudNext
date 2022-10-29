package de.gommzy.cloud.cloud.minecraft;

import de.gommzy.cloud.cloud.cloud.ServerType;
import de.gommzy.cloud.cloud.socket.ClientHandler;
import de.gommzy.cloud.files.FolderUtil;

public class Server {
    public String serverName;
    public String template;
    public ClientHandler cloudClientHandler;
    public ClientHandler serverClientHandler;
    public String address;
    public int port;
    public ServerType serverType;

    public Server(String serverName, String template, ClientHandler cloudClientHandler, String address, ServerType serverType) {
        this.serverName = serverName;
        this.template = template;
        this.cloudClientHandler = cloudClientHandler;
        this.address = address;
        this.serverType = serverType;

        FolderUtil.readFolder("temp/" + serverName, null, cloudClientHandler, "templates/ " + template);
        cloudClientHandler.write("startserver " + serverName + " " + serverType);
    }

    public void startedServer(int port) {
        this.port = port;
        System.out.println("Started server on " + address + " " + port);
    }
}
