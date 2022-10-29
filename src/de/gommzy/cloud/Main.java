package de.gommzy.cloud;

import com.releasenetworks.executor.CloudExecutor;
import de.gommzy.cloud.wrapper.socket.Client;
import de.gommzy.cloud.cloud.minecraft.Minecraft;
import de.gommzy.cloud.cloud.minecraft.Server;
import de.gommzy.cloud.cloud.socket.SocketServer;
import de.gommzy.cloud.config.Config;
import de.gommzy.cloud.files.FolderUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static Map<String, Server> servers = new HashMap<>();

    public static void main(String[] args) {
        try {
            CloudExecutor.execute();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        if (Config.getOptionAsBoolean("client")) {
            FolderUtil.createFolder("temp",true);
            Client client = new Client(Config.getOptionAsString("cloudhost"), Config.getOptionAsInt("port"));
            client.login();
        } else {
            SocketServer server =  new SocketServer(Config.getOptionAsInt("cloudport"));
            FolderUtil.createFolder(Config.getOptionAsString("templateLocation"), false);
            FolderUtil.createFolder(Config.getOptionAsString("cacheLocation"), false);
            try {
                File proxy = new File(Config.getOptionAsString("templateLocation") + "/proxy");
                File fallback = new File(Config.getOptionAsString("templateLocation") + "/fallback");
                if (!proxy.exists()) {
                    proxy.mkdirs();
                }
                if (!fallback.exists()) {
                    fallback.mkdirs();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            server.start();
            Minecraft.startMinecraft();
        }
    }
}