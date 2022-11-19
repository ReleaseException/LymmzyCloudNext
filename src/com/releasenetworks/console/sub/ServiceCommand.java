package com.releasenetworks.console.sub;

import com.releasenetworks.console.Command;
import com.releasenetworks.executor.exceptions.LymmzyCloudException;
import de.gommzy.cloud.LymmzyCloud;
import de.gommzy.cloud.cloud.cloud.ServerType;
import de.gommzy.cloud.cloud.templates.configuration.TemplateConfiguration;

import java.io.IOException;

import static de.gommzy.cloud.cloud.service.ServiceExecutor.createService;

@com.releasenetworks.executor.annotations.Command(command = "service")
public class ServiceCommand extends Command {

    public ServiceCommand(String[] args) throws LymmzyCloudException, IOException {
        super("service" , args, "The built in service command");
        //System.out.println(LymmzyCloud.services.keySet().stream().toList().get(0));
        for (TemplateConfiguration configuration : LymmzyCloud.services.keySet()) {
            if (configuration.getTemplateName().equalsIgnoreCase(args[1])) {
                createService(configuration, null);
                System.out.println(LymmzyCloud.services.get(configuration).size());
                break;
            }
        }
        //createService(new TemplateConfiguration("Proxy", 100, ServerType.VELOCITY, 25565, 256,512, "jdk8", true, 1), null);
    }
}
