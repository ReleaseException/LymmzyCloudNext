package de.gommzy.cloud.cloud.minecraft;

import de.gommzy.cloud.cloud.cloud.Cloud;
import de.gommzy.cloud.cloud.cloud.ServerType;

import java.util.HashMap;
import java.util.UUID;

public class Minecraft {
    public static HashMap<String, Cloud> clouds = new HashMap<>();

    public static void startMinecraft() {
        LobbySystem.start();
    }

    public static String addCloud(Cloud cloud) {
        String cloudUUID = null;
        while (cloudUUID == null || clouds.containsKey(cloudUUID)) {
            cloudUUID = UUID.randomUUID().toString();
        }
        clouds.put(cloudUUID, cloud);

        cloud.bungee = ServerManager.createServer(cloud.clientHandler,"bungee","bungee", ServerType.BUNGEECORD);

        return cloudUUID;
    }

    public static void removeCloud(String cloudUUID) {
        clouds.remove(cloudUUID);
    }
}
