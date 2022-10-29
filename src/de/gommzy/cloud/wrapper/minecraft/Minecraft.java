package de.gommzy.cloud.wrapper.minecraft;

import de.gommzy.cloud.cloud.cloud.ServerType;
import de.gommzy.cloud.cloud.minecraft.ServerManager;
import de.gommzy.cloud.files.FolderUtil;

import java.io.File;
import java.net.ServerSocket;

public class Minecraft {
    public static String cloudUUID;

    public static int startServer(String servername, ServerType serverType) {
        try {
            int port = 25565;
            if (serverType == ServerType.PAPER) {
                ServerSocket serverSocket = new ServerSocket(0); //find free port
                port = serverSocket.getLocalPort();
                serverSocket.close();
            }

            if (serverType == ServerType.PAPER) {
                FolderUtil.copyFolder("jars/spigot", "temp/" + servername, "jars/spigot");
            } else {
                FolderUtil.copyFolder("jars/bungee", "temp/" + servername, "jars/bungee");
            }

            String[] command = ("java -Dcom.mojang.eula.agree=true -jar server.jar --nogui").split(" ");
            if (serverType == ServerType.PAPER) {
                command = ("java -jar server.jar --nogui --port "+port).split(" ");
            }

            Process p = null;
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(new File("temp/" + servername));
            p = pb.start();

            return port;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
