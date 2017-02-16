package org._24601.kasper.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/**
 * Identifies a method that will be
 * wrapped by a MethodProxy executable
 * 
 * @author je bailey
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
	String[] value();
	Class<?>[] classes() default {};
	int numOptional() default 0;
}
