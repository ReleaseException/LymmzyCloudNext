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

    //TODO: create service command e.g. service create <templatename> <nodename> (ignored if using combined)

    public static void createService(TemplateConfiguration configuration, Node node) throws LymmzyCloudException, IOException {
        if (node == null && LymmzyCloud.getExecutionType() == LymmzyCloud.ExecutionType.CONTROLLER) throw new LymmzyCloudException();
        int serviceIdentifier;
        if (LymmzyCloud.services.get(configuration) == null) {
            serviceIdentifier = 1;
        } else {
            serviceIdentifier = LymmzyCloud.services.get(configuration).size() + 1;
        }
        Process process = null;
        int port = (int) (configuration.getStartPortRange() - 1 + serviceIdentifier);
        File serviceLocation;


        if (configuration.isStaticService()) {
            serviceLocation = new File(SystemProperties.getUserDirectory() + "/" + Config.getOptionAsString("staticServiceLocation") + "/" + configuration.getTemplateName() + "-" + serviceIdentifier);
            if (!serviceLocation.exists()) {
                FolderUtils.copyDirectory(SystemProperties.getUserDirectory() + "/" + Config.getOptionAsString("templateLocation") + "/" + configuration.getTemplateName(), serviceLocation.getPath());
            }
        } else {
            serviceLocation = new File(SystemProperties.getUserDirectory() + "/" + Config.getOptionAsString("tempFolder") + "/" + configuration.getTemplateName() + "-" + serviceIdentifier);
            FolderUtils.copyDirectory(SystemProperties.getUserDirectory() + "/" + Config.getOptionAsString("templateLocation") + "/" + configuration.getTemplateName(), serviceLocation.getPath());
        }

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(configuration.getServerType().getStartCommand(configuration.getInitialHeap(), configuration.getMaxRam(), port, configuration.getMaxPlayers()).split(" "));
            processBuilder.directory(serviceLocation);
            process = processBuilder.start();
            System.out.println(processBuilder.command().toString());
            System.out.printf("Service %s has been started on port %s%n", configuration.getTemplateName() + "-" + serviceIdentifier, port);
            System.out.println(serviceLocation);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        Service service = new Service(configuration.getTemplateName() + "-" + serviceIdentifier, process, configuration, node);
        service.registerService();
        LymmzyCloud.serviceNames.add(service);

        ServiceRegistry.resize();
        //Main.services.put(configuration.getTemplateName() + "-" + serviceIdentifier, process);
    }
}