package de.gommzy.cloud.wrapper.socket;

import com.releasenetworks.bridge.protocol.CloudProtocol;
import com.releasenetworks.bridge.protocol.ProtocolPacket;
import de.gommzy.cloud.wrapper.avaible.AvaibleLoop;
import de.gommzy.cloud.config.Config;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

public class Client {
    private BufferedReader reader;
    public PrintWriter writer;
    public Socket socket;
    private Thread clientReciverThread;
    private Thread avaibleLoop;
    public ClientReciver clientReciver;
    private boolean cooldown = false;
    private final String ip;
    private final int port;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void login() {
        try {
            logout();
        } catch (Exception ignored) {}
        try {
            if (cooldown) {
                return;
            }
            cooldown = true;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}
            cooldown = false;
            socket = new Socket(ip, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
            writeAsPacket(new ProtocolPacket(CloudProtocol.CLOUD_LOGIN, new JSONObject().put("password", Config.getOptionAsString("cloudpassword"))));
            //write("login "+ Config.getOptionAsString("cloudpassword"));
            //write("registercloud");
            writeAsPacket(new ProtocolPacket(CloudProtocol.REGISTER_CLOUD, new JSONObject().put("registercloud", "null")));
            if (clientReciverThread == null || !clientReciverThread.isAlive()) {
                final Client thisInstance = this;
                clientReciverThread = new Thread(() -> new ClientReciver(thisInstance));
                clientReciverThread.start();
                avaibleLoop =  new Thread(() -> new  AvaibleLoop(this));
                avaibleLoop.start();
            }
            System.out.println("[CLIENT] Successfully connected!");
        } catch (final IOException ignored) {
            System.out.println("[CLIENT] Can't connect! Reconnect in 1s");
            try {
                Thread.sleep(1000);
                cooldown = false;
                login();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void logout() {
        try {
            this.clientReciverThread.interrupt();
            this.avaibleLoop.interrupt();
        } catch (Exception ignored) {}
    }

    String read() {
        try {
            return this.reader.readLine();
        } catch (final Exception exception) {
            logout();
        }
        return "error";
    }

    public void write(final String msg) {
        this.writer.println(msg);
        this.writer.flush();
    }

    public void writeAsPacket(final ProtocolPacket packet) {
        this.writer.println(packet.asString());
        this.writer.flush();
    }
}
