package de.gommzy.cloud.config;

import com.releasenetworks.logger.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Config {
    private static JSONObject configuration;

    public Config() {
        try {
            if (!new File(System.getProperty("user.dir") + "/config.lymmzycloud").exists()) { // if file already exists will do nothing
                JSONObject configuration = new JSONObject();
                configuration.put("mode", "combined");
                configuration.put("logLevel", Logger.Level.INFO);
                configuration.put("cloudhost", "127.0.0.1");
                configuration.put("cloudport", 2000);
                configuration.put("httpport", 2304);
                configuration.put("cloudpassword","SeZ59Y6tcfRd7");
                configuration.put("proxypassword", "mAMi7C6%QD#rCq%ueJ841@Fw^x");
                configuration.put("templateLocation","global/templates");
                //configuration.put("localServiceLocation", "local/services");
                configuration.put("tempFolder","temp/services");
                configuration.put("staticServiceLocation","global/services");
                configuration.put("cacheLocation","jars");
                configuration.put("rebootReset", false);
                configuration.put("jvmVersions", new JSONArray()
                        .put(0, "jdk8")
                        .put(1,"jdk11")
                        .put(2, "jdk17")
                        .put(3, "jdk18"));
                configuration.put("startOrder", new JSONArray()
                        .put(0, "Proxy"));
                try {
                    FileWriter file = new FileWriter(System.getProperty("user.dir") + "/config.lymmzycloud");
                    file.write(configuration.toString());
                    file.close();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        String string = null;
        try (Scanner sc = new Scanner(new File(System.getProperty("user.dir") + "/config.lymmzycloud"))) {
            while(sc.hasNextLine()){
                string = sc.nextLine();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        if (string != null) {
            configuration = new JSONObject(string);
        } else {
            throw new NullPointerException();
        }

    }

    public static String getOptionAsString(String path) {
        return configuration.getString(path);
    }

    public static int getOptionAsInt(String path) {
        return configuration.getInt(path);
    }

    public static boolean getOptionAsBoolean(String path) {
        return configuration.getBoolean(path);
    }

    public static List<Object> getOptionAsArray(String path) {
        return configuration.getJSONArray(path).toList();
    }
}
