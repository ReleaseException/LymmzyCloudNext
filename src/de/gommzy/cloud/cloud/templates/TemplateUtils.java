package de.gommzy.cloud.cloud.templates;

import de.gommzy.cloud.LymmzyCloud;
import de.gommzy.cloud.cloud.templates.configuration.TemplateConfiguration;

public class TemplateUtils {

    //TODO: Create ifTemplateExits method boolean

    public static TemplateConfiguration getTemplateByName(String name) {
        for (TemplateConfiguration configuration : LymmzyCloud.services.keySet()) {
            if (configuration.getTemplateName().equals(name)) {
                return configuration;
            }
        }
        return null;
    }

}
