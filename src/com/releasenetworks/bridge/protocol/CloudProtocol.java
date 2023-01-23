package com.releasenetworks.bridge.protocol;

import java.util.Arrays;

public enum CloudProtocol {

    CHANNEL_REQUEST("request_channel", false, false),
    CLOSING_NODE("closing_node", false, true),
    OPENING_NODE("opening_node", false, true),
    CLOUD_LOGIN("cloud_login", false, true),
    REGISTER_CLOUD("register_cloud", false, true),
    STATS_NODE("stats_node", false, false),
    TEMPLATE_TRANSFER("template_transfer", false, false),
    SERVER_START_REQUEST("service_start_request", false, false),
    SERVER_CLOSE_REQUEST("service_close_request", false, false),
    SERVER_RESTART_REQUEST("service_restart_request", false, false),
    SERVER_CLOSED("service_closed", false, false),
    BROADCAST("255", false, false),
    PLAYER_QUIT("player_quit", true, false),
    PROXY_PING("proxyPing", true, false),
    PROXY_JOIN("proxyJoin", true, false),
    PROXY_QUIT("proxyQuit", true, false),
    PROXY_SWITCH("proxySubSwitch", false, false),
    PROXY_STATS("proxyStats", false, false),
    PROXY_LOGIN("bridge_login", false, false),
    PING("PING", false, false),
    SERVER_STARTED("server_started", false, false),
    B0("0", false, false),
    B1("127", false, false);

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
        return Arrays.stream(CloudProtocol.values()).filter(resuls -> protocol.equalsIgnoreCase(resuls.protocol)).findFirst().orElseThrow(() ->
            new IllegalArgumentException("No matching protocol found"));
    }
}