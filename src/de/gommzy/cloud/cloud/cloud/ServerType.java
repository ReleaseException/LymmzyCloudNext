package de.gommzy.cloud.cloud.cloud;

import de.gommzy.cloud.config.Config;

public enum ServerType {

    PAPER("paper", Config.getOptionAsString("cacheLocation") + "/servers/paper", "java -Xms%sM -Xmx%sM -Dcom.mojang.eula.agree=true -jar server.jar --nogui --port %S --max-players %s",60000),
    VELOCITY("velocity", Config.getOptionAsString("cacheLocation") + "/servers/velocity", "java -Xms%sM -Xmx%sM -jar server.jar --port %s",7000),
    MINESTORM("minestorm", Config.getOptionAsString("cacheLocation") + "/servers/minestorm", "java -Xms%sM -Xmx%sM -Dcom.mojang.eula.agree=true -jar server.jar --nogui --port %s --max-players %s", 60000),
    BUNGEECORD("bungeecord", Config.getOptionAsString("cacheLocation") + "/servers/bungeecord", "java -Xms%sM -Xmx%sM -jar server.jar",15000),
    LymmzyActiveSyncproxy("syncproxy", Config.getOptionAsString("cacheLocation") + "/servers/syncproxy", "java -Xms%sM -Xmx%sM -jar server.jar %s", 5000);

    private final String type;
    private final String location;
    private final String startCommand;
    private final long startupTime;

    ServerType(String type, String location, String startCommand, long startupTime) {
        this.type = type;
        this.location = location;
        this.startCommand = startCommand;
        this.startupTime = startupTime;
    }

    public String getType() {
        return type;
    }

    public String getLocation() {
        return location;
    }

    public long getStartupTime() {
        return startupTime;
    }

    public String getStartCommand(int initialHeap, int maxHeap, int port, int maxPlayers) {
        return String.format(startCommand, initialHeap, maxHeap, port, maxPlayers);
    }

    public String getSyncProxyStartCommand(int initialHeap, int maxHeap, int value) {
        return String.format(startCommand, initialHeap, maxHeap, value);
    }
}
