package com.releasenetworks.console;

public class Command {

    private final String command;
    private final String[] args;
    private final String description;

    public Command(String command, String[] args, String description) {
        this.command = command;
        this.description = description;
        this.args = args;
    }

    public String getCommand() {
        return command;
    }

    public String[] getArgs() {
        return args;
    }

    public String getDescription() {
        return description;
    }
}
