package com.releasenetworks.console.sub;

import com.mysql.cj.log.Log;
import com.releasenetworks.console.Command;
import com.releasenetworks.executor.exceptions.LymmzyCloudException;
import com.releasenetworks.logger.Logger;
import de.gommzy.cloud.cloud.cloud.ServerType;
import de.gommzy.cloud.cloud.service.ServiceRegistry;
import de.gommzy.cloud.cloud.templates.TemplateExecutor;
import de.gommzy.cloud.config.Config;

import java.io.File;
import java.io.IOException;

@com.releasenetworks.executor.annotations.Command(command = "template")
public class TemplateCommand extends Command {

    private final String[] args;

    public TemplateCommand(String[] args) throws LymmzyCloudException, IOException, InterruptedException {
        super("template", args, "The built-in template command!");
        this.args = args;
        if (args.length > 1) {
            switch (args[1]) {
                case "create", "generate" -> {
                    generate();
                }
                case "delete", "remove" -> {
                    delete();
                }
                case "list" -> {
                    list();
                }
                case "reload" -> {

                }
                case "resize" -> {
                    ServiceRegistry.resize();
                    Logger.log("Resize complete", Logger.Level.DEBUG);
                }
                default -> {
                    Logger.log("Please use template create/remove/list", Logger.Level.INFO);
                }
            }
        } else {
            Logger.log("Please use template create/remove/list", Logger.Level.INFO);
        }
    }

    private void generate() throws LymmzyCloudException, IOException, InterruptedException {
        if (args.length == 12) {
            String name = args[2];
            int players = Integer.parseInt(args[3]);
            ServerType type = ServerType.valueOf(args[4]);
            long startPortRange = Integer.parseInt(args[5]);
            int initialHeap = Integer.parseInt(args[6]);
            int maxRam = Integer.parseInt(args[7]);
            String targetVMVersion = args[8];
            boolean staticService = Boolean.parseBoolean(args[9]);
            int minServiceCount = Integer.parseInt(args[10]);
            boolean fallbackHost = Boolean.parseBoolean(args[11]);

            TemplateExecutor.createTemplate(
                    name,
                    players,
                    type,
                    startPortRange,
                    initialHeap,
                    maxRam,
                    targetVMVersion,
                    staticService,
                    minServiceCount,
                    fallbackHost
            );


        } else {
            Logger.log("Please use: template create <name> <maxPlayers> <BUNGEECORD/VELOCITY/PAPER> <startPortrange> <initialHeap> <maxHeap> <targetVMVersion> <staticservice> <minserviceCount> <fallbackhost boolean>", Logger.Level.INFO);
        }
    }

    private void delete() {
        if (args.length == 3) {
            TemplateExecutor.deleteTemplate(args[2]);
        } else {
            Logger.log("Please use: template remove <name>", Logger.Level.INFO);
        }
    }

    private void list() {
        File[] templateNames = new File(Config.getOptionAsString("templateLocation")).listFiles();
        if (templateNames != null) {
            if (templateNames.length == 0) {
                System.out.println("You have not created any templates yet!");
            } else {
                Logger.log("There are %s templates existing!", Logger.Level.INFO, templateNames.length);
                for (int i = 0; i < templateNames.length; i++) {
                    Logger.log("%s. %s", Logger.Level.INFO, i + 1, templateNames[i].getAbsoluteFile().getName());
                }
            }
        }
    }
}