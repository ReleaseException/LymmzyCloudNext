package com.releasenetworks.console.sub;

import com.releasenetworks.console.Command;

@Deprecated(since = "0.0.4")
@com.releasenetworks.executor.annotations.Command(command = "network")
public class NetworkCommand extends Command {

    private final String[] args;

    public NetworkCommand(String[] args) {
        super("network", args, "The contollernode can manage the network with this command");
        this.args = args;

        if (args.length > 1) {
            switch (args[1]) {
                case "push" -> {

                }
                case "info", "information" -> {

                }
            }
        }
    }
}