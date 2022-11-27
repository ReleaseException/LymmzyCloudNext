package de.gommzy.cloud;

import com.releasenetworks.bridge.socket.Channel;
import com.releasenetworks.bridge.socket.ChannelHandler;
import de.gommzy.cloud.cloud.cloud.Node;
import de.gommzy.cloud.cloud.minecraft.Server;
import de.gommzy.cloud.cloud.service.Service;
import de.gommzy.cloud.cloud.templates.configuration.TemplateConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LymmzyCloud {

    public static final int MAX_FILE_SIZE = 32 * 1024 * 1024;
    public static final int MAX_TOTAL_BYTES = 10 * MAX_FILE_SIZE;

    public static Map<String, Server> getServers() {
        return Main.servers;
    }

    public static List<String> getServiceNames() {
        return Main.serviceNames;
    }

    public static Map<String, Process> getServices() {
        return Main.services;
    }

    public static List<Node> getConnectedNodes() {
        return Main.connectedNodes;
    }

    public static ExecutionType getExecutionType() {
        return Main.executionType;
    }

    public static Map<TemplateConfiguration, List<Service>> services = new ConcurrentHashMap<>();
    public static final List<Service> serviceNames = Collections.synchronizedList(new ArrayList<>());
    public static Map<String, TemplateConfiguration> configurationMap = new ConcurrentHashMap<>();

    public static ChannelHandler proxyChannel;

    public enum ExecutionType {
        CONTROLLER,
        WRAPPER,
        COMBINED,
        ALL
    }
}