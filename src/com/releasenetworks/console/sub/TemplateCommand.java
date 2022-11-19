package com.releasenetworks.console.sub;

import com.releasenetworks.console.Command;
import de.gommzy.cloud.cloud.cloud.ServerType;
import de.gommzy.cloud.cloud.templates.TemplateExecutor;
import de.gommzy.cloud.config.Config;

import java.io.File;

@com.releasenetworks.executor.annotations.Command(command = "template")
public class TemplateCommand extends Command {

    private final String[] args;

    public TemplateCommand(String[] args) {
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
                default -> {
                    System.out.println("Please use template create/remove/list");
                }
            }
        } else {
            System.out.println("Please use template create/remove/list");
        }
    }

    private void generate() {
        if (args.length == 11) {
            String name = args[2];
            int players = Integer.parseInt(args[3]);
            ServerType type = ServerType.valueOf(args[4]);
            long startPortRange = Integer.parseInt(args[5]);
            int initialHeap = Integer.parseInt(args[6]);
            int maxRam = Integer.parseInt(args[7]);
            String targetVMVersion = args[8];
            boolean staticService = Boolean.parseBoolean(args[9]);
            int minServiceCount = Integer.parseInt(args[10]);

            TemplateExecutor.createTemplate(
                    name,
                    players,
                    type,
                    startPortRange,
                    initialHeap,
                    maxRam,
                    targetVMVersion,
                    staticService,
                    minServiceCount
            );


        } else {
            System.out.println("Please use: template create <name> <maxPlayers> <BUNGEECORD/VELOCITY/PAPER> <startPortrange> <initialHeap> <maxHeap> <targetVMVersion> <staticservice> <minserviceCount>");
        }
    }

    private void delete() {
        if (args.length == 3) {
            TemplateExecutor.deleteTemplate(args[2]);
        } else {
            System.out.println("Please use: template remove <name>");
        }
    }

    private void list() {
        File[] templateNames = new File(Config.getOptionAsString("templateLocation")).listFiles();
        if (templateNames != null) {
            if (templateNames.length == 0) {
                System.out.println("You have not created any templates yet!");
            } else {
                System.out.printf("There are %s templates existing!%n", templateNames.length);
                for (int i = 0; i < templateNames.length; i++) {
                    System.out.printf("%s. %s%n", i + 1, templateNames[i].getAbsoluteFile().getName());
                }
            }
        }
    }
}