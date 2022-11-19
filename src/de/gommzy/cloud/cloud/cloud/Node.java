package de.gommzy.cloud.cloud.cloud;

import de.gommzy.cloud.cloud.socket.ClientHandler;

import java.io.PrintWriter;
import java.net.Socket;
import java.nio.channels.SocketChannel;

public class Node {

    private final ClientHandler clientHandler;

    public Node(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    public Socket getSocket() {
        return this.clientHandler.getSocket();
    }

    public PrintWriter getPrintWriter() {
        return this.clientHandler.getWriter();
    }

    public SocketChannel getChannel() {
        return this.clientHandler.getSocket().getChannel();
    }

    public void closeNode() {
        this.getClientHandler().closeNode();
    }
}