package de.gommzy.cloud.cloud.socket;

import com.releasenetworks.bridge.protocol.CloudProtocol;
import com.releasenetworks.bridge.protocol.ProtocolPacket;
import de.gommzy.cloud.cloud.cloud.Cloud;
import de.gommzy.cloud.cloud.minecraft.Minecraft;
import de.gommzy.cloud.cloud.minecraft.ServerManager;
import de.gommzy.cloud.config.Config;
import org.json.JSONObject;

import java.net.Socket;

public class ClientReciver {
    private String cloudUUID;

    ClientReciver(ClientHandler clientHandler, Socket socket) {
        try {
            while(socket.isConnected()) {
                String read = clientHandler.read();
                if (read == null) {
                    break;
                }
                new Thread(() -> {
                    System.out.println(read);

                    JSONObject packet = new JSONObject(read);
                    switch (CloudProtocol.getProtocol(packet.getJSONArray("protocol").getString(0).toUpperCase())) {
                        case CLOUD_LOGIN -> {
                            String password = packet.getJSONObject("data").getString("password");
                            System.out.println(Config.getOptionAsString("cloudpassword").equals(password));
                            if (!password.equals(Config.getOptionAsString("cloudpassword"))) {
                                System.out.println("[Wrong-Pswd] " + clientHandler.getAddress());
                                clientHandler.unregister();
                            } else {
                                System.out.println("[Register] " + clientHandler.getAddress());
                                System.out.println("Password correct");
                            }
                        }
                        case REGISTER_CLOUD -> {
                            Cloud cloud = new Cloud(clientHandler.getAddress(), clientHandler);
                            cloudUUID = Minecraft.addCloud(cloud);
                            clientHandler.write("registercloud " + cloudUUID);
                        }
                    }
                }).start();
           }
            clientHandler.unregister();
        } catch (final Exception exc) {
            exc.printStackTrace();
            clientHandler.unregister();
        }
    }
}
