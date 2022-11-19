package de.gommzy.cloud.cloud.service;

import com.releasenetworks.executor.annotations.LymmzyCloud;
import de.gommzy.cloud.Main;
import de.gommzy.cloud.cloud.cloud.ServerType;
import de.gommzy.cloud.cloud.templates.configuration.TemplateConfiguration;
import de.gommzy.cloud.config.Config;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@LymmzyCloud(mode = "all")
public class ServiceRegistry {

    public ServiceRegistry() throws IOException {
        if (Main.executionType == de.gommzy.cloud.LymmzyCloud.ExecutionType.COMBINED || Main.executionType == de.gommzy.cloud.LymmzyCloud.ExecutionType.CONTROLLER) {
            this.register();
        }
    }

    public void register() throws IOException {
        File[] templateNames = new File(Config.getOptionAsString("templateLocation")).listFiles();
        Map<TemplateConfiguration, List<Service>> result = new ConcurrentHashMap<>();
        if (templateNames != null) {
            for (File templateName : templateNames) {
                JSONObject templateObject = new JSONObject(Files.readString(Path.of(Config.getOptionAsString("templateLocation") + "/" + templateName.getName() + "/" + templateName.getName() + ".lymmzycloud")));
                result.put(new TemplateConfiguration(
                        templateObject.getString("templateName"),
                        templateObject.getInt("maxPlayers"),
                        ServerType.valueOf(ServerType.class, templateObject.getString("ServerType")),
                        templateObject.getLong("startPortRange"),
                        templateObject.getInt("maxHeap"),
                        templateObject.getInt("initialHeap"),
                        templateObject.getString("targetVMVersion"),
                        templateObject.getBoolean("staticService"),
                        templateObject.getInt("minServiceCount")
                ), new ArrayList<>());
            }
        }
        de.gommzy.cloud.LymmzyCloud.services = result;
    }
}