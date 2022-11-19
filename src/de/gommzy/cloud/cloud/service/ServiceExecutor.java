package de.gommzy.cloud.cloud.service;

import com.releasenetworks.executor.exceptions.LymmzyCloudException;
import com.releasenetworks.utils.FolderUtils;
import com.releasenetworks.utils.SystemProperties;
import de.gommzy.cloud.LymmzyCloud;
import de.gommzy.cloud.Main;
import de.gommzy.cloud.cloud.cloud.Node;
import de.gommzy.cloud.cloud.cloud.ServerType;
import de.gommzy.cloud.cloud.templates.configuration.TemplateConfiguration;
import de.gommzy.cloud.config.Config;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ServiceExecutor {

    public static void main(String[] args) {
        new Config();
        try {
            createService(new TemplateConfiguration("Proxy", 100, ServerType.VELOCITY, 25565, 256,512, "jdk8", true, 1), null);
        } catch (LymmzyCloudException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    //TODO: create service command e.g. service create <templatename> <nodename> (ignored if using combined)

    public static void createService(TemplateConfiguration configuration, Node node) throws LymmzyCloudException, IOException {
        if (node == null && LymmzyCloud.getExecutionType() == LymmzyCloud.ExecutionType.CONTROLLER) throw new LymmzyCloudException();
        int serviceIdentifier = LymmzyCloud.services.get(configuration).size() + 1;
        Process process = null;
        File serviceLocation = new File(SystemProperties.getUserDirectory() + "/" + Config.getOptionAsString("templateLocation") + "/" + configuration.getTemplateName() + "-" + serviceIdentifier);


        if (configuration.isStaticService()) {
            serviceLocation = new File(SystemProperties.getUserDirectory() + "/" + Config.getOptionAsString("staticServiceLocation") + "/" + configuration.getTemplateName() + "-" + serviceIdentifier);
            FolderUtils.copyDirectory(SystemProperties.getUserDirectory() + "/" + Config.getOptionAsString("templateLocation") + "/" + configuration.getTemplateName(), serviceLocation.getPath());

        } else {
            FolderUtils.copyDirectory(SystemProperties.getUserDirectory() + "/" + Config.getOptionAsString("templateLocation") + "/" + configuration.getTemplateName(), serviceLocation.getPath());
        }

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(configuration.getServerType().getStartCommand(configuration.getInitialHeap(), configuration.getMaxRam(), (int) configuration.getStartPortRange(), configuration.getMaxPlayers()).split(" "));
            processBuilder.directory(serviceLocation);
            process = processBuilder.start();
            System.out.println(configuration.getServerType().getStartCommand(configuration.getInitialHeap(), configuration.getMaxRam(), (int) configuration.getStartPortRange(), configuration.getMaxPlayers()));
            System.out.printf("Service %s has been started%n", configuration.getTemplateName() + "-" + serviceIdentifier);
            System.out.println(serviceLocation);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        new Service(configuration.getTemplateName() + "-" + serviceIdentifier, process, configuration, node).registerService();
        //Main.services.put(configuration.getTemplateName() + "-" + serviceIdentifier, process);
    }
}