package com.releasenetworks.executor.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author NotRelease
 * @since LymmzyCloud 0.0.4
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    String command() default  "";
}
