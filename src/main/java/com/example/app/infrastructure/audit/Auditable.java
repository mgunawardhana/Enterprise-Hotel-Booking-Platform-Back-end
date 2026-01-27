package com.example.app.infrastructure.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark methods for audit logging
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auditable {
    
    /**
     * Action type (CREATE, UPDATE, DELETE, LOGIN, etc.)
     */
    String action();
    
    /**
     * Entity name being audited
     */
    String entityName() default "";
}
