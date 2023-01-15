package de.gommzy.cloud;

import com.releasenetworks.bridge.socket.Channel;
import com.releasenetworks.executor.CloudExecutor;
import com.releasenetworks.executor.exceptions.LymmzyCloudException;
import de.gommzy.cloud.cloud.cloud.Node;
import de.gommzy.cloud.cloud.minecraft.Server;
import de.gommzy.cloud.cloud.service.ServiceRegistry;
import de.gommzy.cloud.cloud.socket.SocketServer;
import de.gommzy.cloud.config.Config;
import de.gommzy.cloud.files.FolderUtil;
import de.gommzy.cloud.wrapper.socket.Client;
import org.reflections.Reflections;
import org.slf4j.event.Level;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static Map<String, Server> servers = new HashMap<>();
    public static List<String> serviceNames = new ArrayList<>();
    public static Map<String, Process> services = new ConcurrentHashMap<>();
    public static List<Node> connectedNodes = Collections.synchronizedList(new ArrayList<>());
    public static LymmzyCloud.ExecutionType executionType;

    public static void main(String[] args) throws LymmzyCloudException, IOException {

        Reflections.log.atLevel(Level.TRACE);

        try {
            CloudExecutor.execute();
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            Channel.pendingRequests.put("dummy", new ArrayList<>());
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        switch (executionType) {
            case WRAPPER -> {
                FolderUtil.createFolder("temp/services", true);
                FolderUtil.createFolder("temp/templates", false);
                Client client = new Client(Config.getOptionAsString("cloudhost"), Config.getOptionAsInt("cloudport"));
                client.login();
            }
            case CONTROLLER -> {
                SocketServer server =  new SocketServer(Config.getOptionAsInt("cloudport"));
                FolderUtil.createFolder(Config.getOptionAsString("templateLocation"), false);
                FolderUtil.createFolder(Config.getOptionAsString("cacheLocation"), false);
                server.start();
            }
            case COMBINED -> {
                System.out.println("Running combined mode");
                FolderUtil.createFolder("temp/services",true);
                FolderUtil.createFolder(Config.getOptionAsString("templateLocation"), false);
                FolderUtil.createFolder(Config.getOptionAsString("cacheLocation"), false);
                ServiceRegistry.resize();
            }
            default -> {
                System.out.println("The configuration does not match any of the cloudpart types: Please check configuration.lymmzycloud ('mode')");
                System.exit(99);
            }
        }
    }
}