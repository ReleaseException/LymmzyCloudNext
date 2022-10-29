package de.gommzy.cloud.cloud.cloud;

import de.gommzy.cloud.cloud.minecraft.Server;
import de.gommzy.cloud.cloud.socket.ClientHandler;

public class Cloud {
    public String ip;
    public ClientHandler clientHandler;
    public Server bungee;
    public int avaibleScore  = 100;

    public Cloud(String ip, ClientHandler clientHandler) {
        this.ip = ip;
        this.clientHandler = clientHandler;
    }
}
