package com.releasenetworks.utils;

import de.gommzy.cloud.cloud.service.Service;

public class SystemProperties {

    public static String getUserDirectory() {
        return System.getProperty("user.dir");
    }

    public static String getRuntimeVersion() {
        return System.getProperty("java.version");
    }

    public static String getSystemArchitecture() {
        return System.getProperty("os.arch");
    }

    public static String getSystemName() {
        return System.getProperty("os.name");
    }

    public static String getSystemVersion() {
        return System.getProperty("os.version");
    }

    public static String getUsername() {
        return System.getProperty("user.name");
    }
}