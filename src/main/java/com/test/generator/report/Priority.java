package com.test.generator.report;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to specify the priority level of a test method.
 * Can be used to categorize tests by their importance or criticality.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Priority {
    /**
     * The priority level of the test.
     */
    PriorityLevel value();
} 