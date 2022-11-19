package com.releasenetworks.bridge.protocol;

import com.releasenetworks.executor.exceptions.LymmzyCloudException;
import com.releasenetworks.utils.FolderUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ProtocolPacket {

    private final CloudProtocol protocol;
    private final JSONObject data;
    private final JSONObject finalPacket;

    public ProtocolPacket(CloudProtocol protocol, JSONObject data) {
        this.protocol = protocol;
        this.data = data;
        this.finalPacket = new JSONObject();

        finalPacket.put("protocol", new JSONArray()
                .put(0, protocol.getProtocol())
                .put(1, protocol.isRequiresReply())
                .put(2, protocol.isSubscribable()));
        finalPacket.put("data", data);
    }

    public CloudProtocol getProtocol() {
        return protocol;
    }

    public JSONObject getData() {
        return data;
    }

    public JSONObject getFinalPacket() {
        return finalPacket;
    }

    public String asString() {
        return finalPacket.toString();
    }

    public static ProtocolPacket parsePacket(String data) {
        JSONObject object = new JSONObject(data);
        return new ProtocolPacket(CloudProtocol.getProtocol(object.getJSONArray("protocol").getString(0)), object.getJSONObject("data"));
    }

    public static void main(String[] args) throws IOException, LymmzyCloudException {
        ProtocolPacket packet = new ProtocolPacket(CloudProtocol.B0, new JSONObject().put("template", FolderUtils.read(new File("D:\\Desktop\\ReleaseCloud\\LymmzyCloudNextSpace\\global\\templates.zip"))));
        Files.write(Path.of("D:\\Desktop\\ReleaseCloud\\LymmzyCloudNextSpace\\global\\templates\\test.zip"), (byte[]) packet.getData().get("template"));
    }
}