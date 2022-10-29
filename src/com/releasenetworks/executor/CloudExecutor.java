package com.releasenetworks.executor;


import com.releasenetworks.executor.annotations.LymmzyCloud;
import de.gommzy.cloud.config.Config;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CloudExecutor {

    static Set<Class<?>> classesPresentedByLymmzyCloud = new HashSet<>();

    public static void execute() throws InstantiationException, IllegalAccessException {
        new Config();
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
        for (Class<?> clazz : all()) {
            if (clazz.isAnnotationPresent(LymmzyCloud.class)) {
                clazz.newInstance();
                classesPresentedByLymmzyCloud.add(clazz);
            }
        }
    }

    public static Set<Class<?>> all() {
        Set<Class<?>> result = new HashSet<>();
        result.addAll(new Reflections("com.releasenetworks", new SubTypesScanner(false)).getSubTypesOf(Object.class).stream().collect(Collectors.toList()));
        result.addAll(new Reflections("de.gommzy", new SubTypesScanner(false)).getSubTypesOf(Object.class).stream().collect(Collectors.toList()));
        return result;
    }

}
