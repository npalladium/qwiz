package com.quantumdisruption.qwiz.QWiz.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks annotated site as a must see part of the code base
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ ElementType.PACKAGE, ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
public @interface GuidedTour {

    String name();

    String description() default "";

    int rank() default 0;
}