package de.gommzy.cloud.cloud.socket;

import com.releasenetworks.bridge.protocol.ProtocolPacket;
import com.releasenetworks.utils.Hex;
import de.gommzy.cloud.Main;
import de.gommzy.cloud.cloud.cloud.Node;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class ClientHandler {
    private PrintWriter writer;
    private BufferedReader reader;
    private String address;
    private Socket socket;
    private Thread clientReciverThread;

    public ClientHandler(Socket socket) {
        try {
            this.address = (((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress()).toString().replace("/", "");
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream());
            this.socket = socket;

            final ClientHandler thisInstance = this;
            this.clientReciverThread = new Thread(() -> new ClientReciver(thisInstance, socket));
            clientReciverThread.setName("Node - " + Main.connectedNodes.size() + 1);
            clientReciverThread.start();

            System.out.println("[Connection] " + address);
            this.register();
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
    }

    @Deprecated(forRemoval = true)
    public void unregister() {
        try {
            System.out.println("[Unregister] " + clientReciverThread.getName());
            this.socket.close();
            this.clientReciverThread.interrupt();
            this.clientReciverThread.stop();

        } catch (final Exception exc) {
            exc.printStackTrace();
        }
    }

    public void closeNode() {
        try {
            System.out.println("Closing Node :: " + getClientReciverThread().getName());
            getSocket().close();
            getClientReciverThread().interrupt();
            getClientReciverThread().stop();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void register() {
        try {
            System.out.println("Registering Node :: " + getClientReciverThread().getName());
            Main.connectedNodes.add(new Node(this));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    public String read() {
        try {
            return this.reader.readLine();
        } catch (final Exception exception) {
            unregister();
        }
        return "error";
    }

    @Deprecated(forRemoval = true)
    public void write(final String msg) {
        this.writer.println(msg);
        this.writer.flush();
    }

    public void writeAsPacket(final ProtocolPacket packet) throws IOException {
        this.writer.println(packet.asString());
        this.writer.flush();
    }

    public void writeTemplate(File template) throws IOException {
        this.writer.println("template_incoming");
        OutputStream outputStream = this.socket.getOutputStream();
        FileInputStream fileInputStream = new FileInputStream(template);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        byte[] filebytes = new byte[(int)template.length()];
        bufferedInputStream.read(filebytes, 0, filebytes.length);
        outputStream.write(filebytes, 0, filebytes.length);
        outputStream.flush();
        this.writer.flush();
    }

    public String getAddress() {
        return address;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public Socket getSocket() {
        return socket;
    }

    public Thread getClientReciverThread() {
        return clientReciverThread;
    }

}
