package me.vasujain.studentsyncapi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to explicitly track method performance.
 * This can be applied to any method that requires detailed performance monitoring.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TrackPerformance {
    /**
     * Custom metric name for the tracked method
     */
    String value() default "";

    /**
     * Additional tags for the metric
     */
    String[] tags() default {};

    /**
     * Whether to log the execution time
     */
    boolean logExecutionTime() default true;

    /**
     * Description for the metric
     */
    String description() default "";
}