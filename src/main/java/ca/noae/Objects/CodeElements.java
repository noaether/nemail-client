package ca.noae.Objects;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class CodeElements {
  /**
   * Annotation used to indicate to Jacoco that the annotated element
   * should be ignored when calculating code coverage.
   */
  @Documented
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
  public @interface Generated {
  }
}
