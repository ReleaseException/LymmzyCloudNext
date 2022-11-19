package de.gommzy.cloud.cloud.templates;

import com.releasenetworks.utils.FolderUtils;
import com.releasenetworks.utils.SystemProperties;
import de.gommzy.cloud.cloud.cloud.ServerType;
import de.gommzy.cloud.cloud.templates.configuration.TemplateConfiguration;
import de.gommzy.cloud.config.Config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class TemplateExecutor {

    public static void main(String[] args) {
        new Config();
        createTemplate("Proxy", 100, ServerType.VELOCITY, 25565, 256,512, "jdk8", true, 1);
    }

    public static void createTemplate(String name, int maxPlayers, ServerType type, long startPortRange, int initialHeap, int maxRam, String targetVMVersion, boolean staticService, int minServiceCont) {
        try {
            TemplateConfiguration templateConfiguration = new TemplateConfiguration(name, maxPlayers, type, startPortRange, maxRam, initialHeap, targetVMVersion, staticService, minServiceCont);
            FolderUtils.copyDirectory(Config.getOptionAsString("cacheLocation") + "/servers/" + type.getType().toLowerCase(), Config.getOptionAsString("templateLocation") + "/" + name);
            try {
                FileWriter file = new FileWriter(System.getProperty("user.dir") + "/" + Config.getOptionAsString("templateLocation") + "/" + name + "/" + templateConfiguration.getTemplateName() + ".lymmzycloud");
                file.write(templateConfiguration.getJsonObject().toString());
                file.close();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            Process process;
            ProcessBuilder processBuilder = new ProcessBuilder(type.getStartCommand(initialHeap, maxRam, (int) startPortRange, maxPlayers).split(" "));
            processBuilder.directory(new File(SystemProperties.getUserDirectory() + "/" + Config.getOptionAsString("templateLocation") + "/" + name));
            process = processBuilder.start();
            System.out.printf("Template generation for %s has been started!%n", name);
            Thread.sleep(type.getStartupTime());
            process.destroy();
            System.out.printf("Template generation for %s has been completed!%n", name);
        } catch (IOException | InterruptedException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static void deleteTemplate(String name) {
        try {
            System.out.printf("Removing template %s from cloudnetwork!%n", name);
            FolderUtils.deleteDirectory(Config.getOptionAsString("templateLocation") + "/" + name);
            System.out.printf("%s has been successfully removed from cloudnetwork!%n", name);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}