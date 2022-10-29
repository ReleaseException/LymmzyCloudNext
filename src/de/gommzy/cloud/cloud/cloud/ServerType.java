package de.gommzy.cloud.cloud.cloud;

import de.gommzy.cloud.config.Config;

public enum ServerType {

    PAPER("paper", Config.getOptionAsString("cacheLocation") + "/servers/paper", "java -Xms%ihM -Xmx%mhM -Dcom.mojang.eula.agree=true -jar server.jar --nogui --port %p --max-players %mp"),
    VELOCITY("velocity", Config.getOptionAsString("cacheLocation") + "/servers/velocity", "java -Xms%ihM -Xmx512M -jar server.jar --port %p"),
    BUNGEECORD("bungeecord", Config.getOptionAsString("cacheLocation") + "/servers/bungeecord", "java -Xms%ihM -Xmx%mhM -jar server.jar");

    private final String type;
    private final String location;
    private final String startCommand;

    ServerType(String type, String location, String startCommand) {
        this.type = type;
        this.location = location;
        this.startCommand = startCommand;
    }

    public String getType() {
        return type;
    }

    public String getLocation() {
        return location;
    }

    public String getStartCommand(int port, int maxPlayers, int initialHeap, int maxHeap) {
        return startCommand.replace("%p", String.valueOf(port)).replace("%mp", String.valueOf(maxPlayers)).replace("%ih", String.valueOf(initialHeap).replace("%mh", String.valueOf(maxHeap)));
    }

}
