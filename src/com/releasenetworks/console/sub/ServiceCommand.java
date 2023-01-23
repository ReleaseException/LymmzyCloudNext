package com.releasenetworks.console.sub;

import com.releasenetworks.console.Command;
import com.releasenetworks.executor.exceptions.LymmzyCloudException;
import com.releasenetworks.logger.Logger;
import de.gommzy.cloud.LymmzyCloud;
import de.gommzy.cloud.cloud.service.Service;
import de.gommzy.cloud.cloud.service.ServiceExecutor;
import de.gommzy.cloud.cloud.service.ServiceUtils;
import de.gommzy.cloud.cloud.templates.TemplateUtils;
import de.gommzy.cloud.cloud.templates.configuration.TemplateConfiguration;

import java.io.IOException;

@com.releasenetworks.executor.annotations.Command(command = "service")
public class ServiceCommand extends Command {

    public ServiceCommand(String[] args) throws IOException, LymmzyCloudException, InterruptedException {
        super("service" , args, "The built in service command");
        switch (args[1]) {
            case "list" -> {
                Logger.log("Current services:", Logger.Level.INFO);
                for (TemplateConfiguration configuration : LymmzyCloud.services.keySet()) {
                    for (Service service : LymmzyCloud.services.get(configuration)) {
                        System.out.println("=> " + service.getServiceName());
                    }
                }
            }
            case "kill", "destroy", "close", "stop" -> {
                if (args.length == 3) {
                    ServiceUtils.closeService(args[2]);
                } else {
                    Logger.log("Please use: service close <servicename>", Logger.Level.INFO);
                }
            }
            case "delete", "remove" -> {
                if (args.length == 3) {

                } else {
                    Logger.log("Please use: service remove <servicename>", Logger.Level.INFO);
                }
            }
            case "create" -> {
                ServiceExecutor.createService(TemplateUtils.getTemplateByName(args[2]), null);
            }
        }
    }
}
