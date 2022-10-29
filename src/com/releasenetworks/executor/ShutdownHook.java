package com.releasenetworks.executor;

import com.releasenetworks.utils.FolderUtils;

public class ShutdownHook extends Thread {

    @Override
    public void run() {
        try {
            FolderUtils.deleteDirectory("temp");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}