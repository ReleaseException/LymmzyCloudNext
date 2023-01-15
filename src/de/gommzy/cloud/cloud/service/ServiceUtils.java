package de.gommzy.cloud.cloud.service;

import com.releasenetworks.executor.exceptions.LymmzyCloudException;
import com.releasenetworks.logger.Logger;
import de.gommzy.cloud.LymmzyCloud;
import de.gommzy.cloud.cloud.templates.configuration.TemplateConfiguration;

import java.io.IOException;
import java.util.List;

public class ServiceUtils {

    public static void closeService(String name) throws IOException, LymmzyCloudException, InterruptedException {
        for (TemplateConfiguration configuration : LymmzyCloud.services.keySet()) {
            List<Service> serviceList = LymmzyCloud.services.get(configuration);
            for (Service service : serviceList) {
                if (service.getServiceName().equals(name)) {
                    service.closeService(false);
                    Logger.log("Closing %s", Logger.Level.INFO, service.getServiceName());
                    break;
                }
            }
        }
    }
}
