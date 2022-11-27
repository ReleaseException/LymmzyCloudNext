package com.releasenetworks.executor;

import com.releasenetworks.logger.Logger;
import com.releasenetworks.utils.FolderUtils;
import de.gommzy.cloud.LymmzyCloud;
import de.gommzy.cloud.cloud.service.Service;
import de.gommzy.cloud.cloud.templates.configuration.TemplateConfiguration;
import de.gommzy.cloud.config.Config;

import java.io.File;

public class ShutdownHook extends Thread {

    @Override
    public void run() {
        try {
            FolderUtils.deleteDirectory("temp");

            if (Config.getOptionAsBoolean("rebootReset")) {
                new File("configuration.lymmzycloud").delete();
            }
            for (TemplateConfiguration configuration : LymmzyCloud.services.keySet()) {
                for (Service server : LymmzyCloud.services.get(configuration)) {
                    if (server.getProcess() != null) {
                        server.closeService();
                        Logger.log("%s has been closed!", Logger.Level.INFO, server.getServiceName());
                    }
                }
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}