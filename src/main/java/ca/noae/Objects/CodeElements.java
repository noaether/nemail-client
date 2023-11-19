package ca.noae.Objects;

import java.io.IOException;
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
    /**
     * The value element MUST have one or more values.
     *
     * @return the value element
     */
    String[] value();
  }

  /**
   * Exception thrown when a login attempt fails.
   */
  public static class InvalidLoginException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs an InvalidLoginException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidLoginException(final String message) {
      super(message);
    }
  }

  public static class UnreacheableHostException extends IOException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs an UnreacheableServerException with the specified detail message.
     *
     * @param message the detail message
     */
    public UnreacheableHostException(final String message) {
      super(message);
    }
  }
}
