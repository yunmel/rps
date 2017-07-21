package com.yunmel.rps.db.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(TYPE)
@Retention(RUNTIME)
public @interface Table {

  /**
   * (Optional) The name of the table.
   * <p>
   * Defaults to the entity name.
   */
  String name() default "";

  String pk() default "id";

}
