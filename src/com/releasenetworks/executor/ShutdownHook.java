package com.releasenetworks.executor;

import com.releasenetworks.utils.FolderUtils;
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

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}