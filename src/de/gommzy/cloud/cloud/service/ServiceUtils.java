package de.gommzy.cloud.cloud.service;

import com.releasenetworks.executor.exceptions.LymmzyCloudException;
import com.releasenetworks.logger.Logger;
import de.gommzy.cloud.LymmzyCloud;
import de.gommzy.cloud.cloud.templates.configuration.TemplateConfiguration;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramSocket;
import java.net.SocketException;
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

    public static boolean isPortUsed(int port) {
        DatagramSocket sock = null;
        try {
            sock = new DatagramSocket(port);
            sock.close();
            return false;
        } catch (BindException ignored) {
            return true;
        } catch (SocketException exception) {
            exception.printStackTrace();
            return true;
        }
    }
}
