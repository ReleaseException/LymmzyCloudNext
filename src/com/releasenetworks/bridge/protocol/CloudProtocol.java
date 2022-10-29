package com.releasenetworks.bridge.protocol;

import java.util.Arrays;

public enum CloudProtocol {

    CHANNEL_REQUEST("request_channel", false, false),
    BROADCAST("255", false, false);

    private final String protocol;
    private final boolean subscribable;
    private final boolean requiresReply;

    CloudProtocol(String protocol, boolean subscribable, boolean requiresReply) {
        this.protocol = protocol;
        this.subscribable = subscribable;
        this.requiresReply = requiresReply;
    }

    public String getProtocol() {
        return protocol;
    }

    public boolean isSubscribable() {
        return subscribable;
    }

    public boolean isRequiresReply() {
        return requiresReply;
    }

    public static CloudProtocol getProtocol(String protocol) {
        return Arrays.stream(CloudProtocol.values()).filter(resuls -> protocol.equalsIgnoreCase(resuls.name())).findFirst().orElseThrow(() ->
            new IllegalArgumentException("No matching protocol found"));
    }
}