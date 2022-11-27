package de.gommzy.cloud.cloud.templates;

import com.mysql.cj.log.Log;
import com.releasenetworks.executor.exceptions.LymmzyCloudException;
import com.releasenetworks.logger.Logger;
import com.releasenetworks.utils.FolderUtils;
import com.releasenetworks.utils.SystemProperties;
import de.gommzy.cloud.cloud.cloud.ServerType;
import de.gommzy.cloud.cloud.service.ServiceRegistry;
import de.gommzy.cloud.cloud.templates.configuration.TemplateConfiguration;
import de.gommzy.cloud.config.Config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class TemplateExecutor {

    public static void main(String[] args) throws LymmzyCloudException, IOException, InterruptedException {
        new Config();
        createTemplate("Proxy", 100, ServerType.VELOCITY, 25565, 256,512, "jdk8", true, 1, false);
    }

    public static void createTemplate(String name, int maxPlayers, ServerType type, long startPortRange, int initialHeap, int maxRam, String targetVMVersion, boolean staticService, int minServiceCont, boolean fallbackHost) throws LymmzyCloudException, IOException, InterruptedException {
        try {
            TemplateConfiguration templateConfiguration = new TemplateConfiguration(name, maxPlayers, type, startPortRange, maxRam, initialHeap, targetVMVersion, staticService, minServiceCont, fallbackHost);
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
            System.out.println(type);
            if (type == ServerType.LymmzyActiveSyncproxy) {
                processBuilder.command(type.getSyncProxyStartCommand(initialHeap, maxRam ,(int) startPortRange).split(" "));
                System.out.println("Hey LymmzyActiveSyncproxy");
            }
            processBuilder.directory(new File(SystemProperties.getUserDirectory() + "/" + Config.getOptionAsString("templateLocation") + "/" + name));
            process = processBuilder.start();
            Logger.log("Template generation for %s has been started!", Logger.Level.INFO, name);
            Thread.sleep(type.getStartupTime());
            process.destroy();
            Logger.log("Template generation for %s has been completed!", Logger.Level.INFO, name);
            ServiceRegistry.resize();
        } catch (IOException | InterruptedException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static void deleteTemplate(String name) {
        try {
            Logger.log("Removing template %s from cloudnetwork!", Logger.Level.INFO ,name);
            FolderUtils.deleteDirectory(Config.getOptionAsString("templateLocation") + "/" + name);
            Logger.log("%s has been successfully removed from cloudnetwork!%n", Logger.Level.INFO, name);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}