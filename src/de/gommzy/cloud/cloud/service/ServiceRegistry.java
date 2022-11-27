package de.gommzy.cloud.cloud.service;

import com.releasenetworks.executor.annotations.LymmzyCloud;
import com.releasenetworks.executor.exceptions.LymmzyCloudException;
import de.gommzy.cloud.Main;
import de.gommzy.cloud.cloud.cloud.ServerType;
import de.gommzy.cloud.cloud.templates.TemplateUtils;
import de.gommzy.cloud.cloud.templates.configuration.TemplateConfiguration;
import de.gommzy.cloud.config.Config;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@LymmzyCloud
public class ServiceRegistry {

    public ServiceRegistry() throws IOException {
        if (Main.executionType == de.gommzy.cloud.LymmzyCloud.ExecutionType.COMBINED || Main.executionType == de.gommzy.cloud.LymmzyCloud.ExecutionType.CONTROLLER) {
            this.register();
            System.out.println("SERVICE REGISTRY");
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
                        templateObject.getInt("minServiceCount"),
                        templateObject.getBoolean("fallbackHost")
                ), new ArrayList<>());
            }
        }
        de.gommzy.cloud.LymmzyCloud.services = result;
    }

    public static void resize() throws IOException, LymmzyCloudException {
        File[] templateNames = new File(Config.getOptionAsString("templateLocation")).listFiles();
        if (TemplateUtils.getTemplateByName(Config.getOptionAsArray("startOrder").get(0).toString()) != null) {
            TemplateConfiguration configuration = TemplateUtils.getTemplateByName(Config.getOptionAsArray("startOrder").get(0).toString());
            if (de.gommzy.cloud.LymmzyCloud.services.get(configuration).size() < configuration.getMinServiceCount()) {
                ServiceExecutor.createService(configuration, null);
            }
        }
        if (templateNames != null) {
            for (File templateName : templateNames) {
                JSONObject templateObject = new JSONObject(Files.readString(Path.of(Config.getOptionAsString("templateLocation") + "/" + templateName.getName() + "/" + templateName.getName() + ".lymmzycloud")));
                TemplateConfiguration configuration = new TemplateConfiguration(
                        templateObject.getString("templateName"),
                        templateObject.getInt("maxPlayers"),
                        ServerType.valueOf(ServerType.class, templateObject.getString("ServerType")),
                        templateObject.getLong("startPortRange"),
                        templateObject.getInt("maxHeap"),
                        templateObject.getInt("initialHeap"),
                        templateObject.getString("targetVMVersion"),
                        templateObject.getBoolean("staticService"),
                        templateObject.getInt("minServiceCount"),
                        templateObject.getBoolean("fallbackHost")
                );
                for (TemplateConfiguration configuration1 : de.gommzy.cloud.LymmzyCloud.services.keySet()) {
                    if (Objects.equals(configuration1.getTemplateName(), configuration.getTemplateName())) {
                        System.out.println("matching - " + configuration1.getTemplateName() + " => " + de.gommzy.cloud.LymmzyCloud.services.get(configuration1).size());
                        if (de.gommzy.cloud.LymmzyCloud.services.get(configuration1).size() < configuration1.getMinServiceCount()) {
                            ServiceExecutor.createService(configuration1, null);
                        }
                    }
                }
            }
        }
    }
}