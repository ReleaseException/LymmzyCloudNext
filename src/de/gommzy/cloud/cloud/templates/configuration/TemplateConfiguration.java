package de.gommzy.cloud.cloud.templates.configuration;

import de.gommzy.cloud.cloud.cloud.ServerType;
import org.json.JSONObject;

public class TemplateConfiguration {

    private final JSONObject jsonObject;

    public TemplateConfiguration(String templateName, int maxPlayers, ServerType serverType, long starPortRange, int maxRam, int initialHeap, String targetVmVersion, boolean staticService, int minServiceCount) {
        jsonObject = new JSONObject();
        jsonObject.put("templateName", templateName);
        jsonObject.put("maxPlayers", maxPlayers);
        jsonObject.put("ServerType", serverType);
        jsonObject.put("startPortRange", starPortRange);
        jsonObject.put("maxHeap", maxRam);
        jsonObject.put("targetVMVersion", targetVmVersion);
        jsonObject.put("initialHeap", initialHeap);
        jsonObject.put("staticService", staticService);
        jsonObject.put("minServiceCount", minServiceCount);
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

    public int getInitialHeap() {
        return jsonObject.getInt("initialHeap");
    }

    public int getMaxRam() {
        return jsonObject.getInt("maxHeap");
    }

    public long getStartPortRange() {
        return jsonObject.getLong("startPortRange");
    }

    public String getTargetVMVersion() {
        return jsonObject.getString("targetVMVersion");
    }

    public boolean isStaticService() {
        return jsonObject.getBoolean("staticService");
    }

    public int getMinServiceCount() {
        return jsonObject.getInt("minServiceCount");
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    @Override
    public String toString() {
        return "TemplateConfiguration{" +
                "jsonObject=" + jsonObject.toString() +
                '}';
    }
}