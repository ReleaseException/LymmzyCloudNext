package com.releasenetworks.console;

import com.releasenetworks.executor.CloudExecutor;
import com.releasenetworks.executor.annotations.Command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Console extends Thread {

    private final Map<String, Class<?>> commands = new ConcurrentHashMap<>();

    public Console() {
        super();
        for (Class<?> clazz : CloudExecutor.all()) {
            if (clazz.isAnnotationPresent(Command.class) && clazz.getSuperclass().equals(com.releasenetworks.console.Command.class)) {
                commands.put(clazz.getDeclaredAnnotation(Command.class).command(), clazz);
            }
        }
    }


    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] args = line.split(" ");
                for (String name : commands.keySet()) {
                    if (args[0].equalsIgnoreCase(name)) {
                        commands.get(name).getConstructors()[0].newInstance((Object) args);
                    }
                }

            }
        } catch (IOException | InvocationTargetException | IllegalAccessException | InstantiationException exception) {
            throw new RuntimeException(exception);
        }
    }
}
