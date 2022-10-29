package de.gommzy.cloud.cloud.templates.configuration;

import de.gommzy.cloud.cloud.cloud.ServerType;
import org.json.JSONObject;

public class DefaultTemplateConfiguration {

    private final JSONObject jsonObject;

    public DefaultTemplateConfiguration(String templateName, int maxPlayers, ServerType serverType, long starPortRange, int maxRam) {
        jsonObject = new JSONObject();
        jsonObject.put("templateName", templateName);
        jsonObject.put("maxPlayers", maxPlayers);
        jsonObject.put("ServerType", serverType);
        jsonObject.put("startPortRange", starPortRange);
        jsonObject.put("maxRamInM", maxRam);
    }

    public String getTemplateName() {
        return jsonObject.getString("templateName");
    }

    public int getMaxPlayers() {
        return jsonObject.getInt("maxPlayers");
    }

    public ServerType getServerType() {
        return (ServerType) jsonObject.get("ServerType");
    }

    public long getStartPortRange() {
        return jsonObject.getLong("startPortRange");
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }
}