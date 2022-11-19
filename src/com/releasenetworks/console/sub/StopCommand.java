package com.releasenetworks.console.sub;

import com.releasenetworks.console.Command;
import com.releasenetworks.utils.SystemProperties;
import de.gommzy.cloud.Main;
import de.gommzy.cloud.cloud.cloud.Node;
import de.gommzy.cloud.config.Config;

@com.releasenetworks.executor.annotations.Command(command = "stop")
public class StopCommand extends Command {

    public StopCommand(String[] args) {
        super("stop", args, "Stops the server");

        System.out.printf("Closing %s nodes!%n", Main.connectedNodes.size());
        for (Node node : Main.connectedNodes) {
            node.closeNode();
        }
        System.out.printf("Closing Proxychannel on port %s%n", Config.getOptionAsInt("httpport"));
        System.out.printf("Stopping LymmzyCloud %s mode, Goodbye %s <3%n", Main.executionType.name().toLowerCase(), SystemProperties.getUsername());
        System.exit(0);
    }
}
