package de.gommzy.cloud.cloud.templates;

import com.releasenetworks.utils.FolderUtils;
import com.releasenetworks.utils.SystemProperties;
import de.gommzy.cloud.cloud.cloud.ServerType;
import de.gommzy.cloud.cloud.minecraft.ServerManager;
import de.gommzy.cloud.cloud.templates.configuration.DefaultTemplateConfiguration;
import de.gommzy.cloud.config.Config;
import de.gommzy.cloud.files.FolderUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TemplateExecutor {

    public static void createLobbyTemplate() {
        final String name = "Lobby";
        final int maxPlayers = -1;
        final ServerType type = ServerType.PAPER;
        final long startPortrange = 25599;
        final int initialHeap = 512;
        final int maxRam = 1024;
        DefaultTemplateConfiguration templateConfiguration = new DefaultTemplateConfiguration(name, maxPlayers, type, startPortrange, maxRam);
    }

    public static void main(String[] args) {
        new Config();
        createDefaultProxyTemplate();
    }

    public static void createDefaultProxyTemplate() {
            try {
                final String name = "Proxy";
                final int maxPlayers = 100;
                final ServerType type = ServerType.VELOCITY;
                final long startPortrange = 25565;
                final int initialHeap = 256;
                final int maxRam = 512;
                DefaultTemplateConfiguration templateConfiguration = new DefaultTemplateConfiguration(name, maxPlayers, type, startPortrange, maxRam);
                FolderUtils.copyDirectory(Config.getOptionAsString("cacheLocation") + "/servers/" + type.getType().toLowerCase(), Config.getOptionAsString("templateLocation") + "/" + name);
                try {
                    FileWriter file = new FileWriter(System.getProperty("user.dir") + "/" + Config.getOptionAsString("templateLocation") + "/" + name + "/" + templateConfiguration.getTemplateName() + ".lymmzycloud");
                    file.write(templateConfiguration.getJsonObject().toString());
                    file.close();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                Process process;
                ProcessBuilder processBuilder = new ProcessBuilder(type.getStartCommand((int) startPortrange, maxPlayers, initialHeap, maxRam).split(" "));
                processBuilder.directory(new File(SystemProperties.getUserDirectory() + "/" + Config.getOptionAsString("templateLocation") + "/" + name));
                process = processBuilder.start();
                System.out.println("Process started");
                Thread.sleep(20000);
                process.destroy();
                System.out.println("Process destroyed");
            } catch (IOException | InterruptedException exception) {
                throw new RuntimeException(exception);
            }
    }
}