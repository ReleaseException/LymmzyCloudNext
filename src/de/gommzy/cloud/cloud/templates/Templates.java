package de.gommzy.cloud.cloud.templates;

import com.releasenetworks.executor.annotations.LymmzyCloud;
import de.gommzy.cloud.cloud.cloud.ServerType;
import de.gommzy.cloud.cloud.templates.configuration.TemplateConfiguration;
import de.gommzy.cloud.config.Config;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@LymmzyCloud
public class Templates {

    public Templates() throws IOException {
        this.setTemplates();
    }


    public void setTemplates() throws IOException {
        File[] templateNames = new File(Config.getOptionAsString("templateLocation")).listFiles();
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
                String templateConfigurationName = templateObject.getString("templateName");
                de.gommzy.cloud.LymmzyCloud.configurationMap.put(templateConfigurationName, configuration);
            }
        }
    }


}
