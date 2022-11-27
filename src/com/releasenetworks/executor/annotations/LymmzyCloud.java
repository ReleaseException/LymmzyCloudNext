package com.releasenetworks.executor.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Executes each annotated class in the construction process. Is called after the initialisation of the configuration. Subserver classes that are executed with this annotation are ignored.
 *
 * @author Dante Raj
 * @since LymmzyCloud 0.0.4
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LymmzyCloud {

}
