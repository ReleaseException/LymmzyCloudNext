package com.releasenetworks.bridge.packet;

import com.releasenetworks.bridge.protocol.CloudProtocol;

import javax.annotation.Nonnull;

public class BridgePacket {

    private final String serverName;
    private final String content;
    private final boolean minecraft;
    private final boolean proxy;

    private final CloudProtocol protocol;

    public BridgePacket(String serverName, String content, boolean minecraft, boolean proxy) {
        this.serverName = serverName;
        this.content = content;
        this.minecraft = minecraft;
        this.proxy = proxy;
        protocol = null;
    }

    public BridgePacket(String serverName, String content, boolean minecraft, boolean proxy, @Nonnull CloudProtocol protocol) {
        this.serverName = serverName;
        this.content = content;
        this.minecraft = minecraft;
        this.proxy = proxy;
        this.protocol = protocol;
    }


    public String getServerName() {
        return serverName;
    }

    public String getContent() {
        return content;
    }

    public boolean isMinecraft() {
        return minecraft;
    }

    public boolean isProxy() {
        return proxy;
    }

    public CloudProtocol getProtocol() {
        return protocol;
    }
}
