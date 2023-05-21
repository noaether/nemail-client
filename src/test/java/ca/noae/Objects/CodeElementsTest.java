package ca.noae.Objects;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import ca.noae.Objects.CodeElements.Generated;
import ca.noae.Objects.CodeElements.InvalidLoginException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.annotation.Annotation;

@RunWith(MockitoJUnitRunner.class)
public class CodeElementsTest {
  /**
   * Tests the {@link CodeElements#InvalidLoginException} exception.
   */
  @Test
  public void testInvalidLoginException() {
    String errorMessage = "Invalid login credentials";
    InvalidLoginException exception = new CodeElements.InvalidLoginException(errorMessage);

    assertNotNull(exception);
    assertEquals(errorMessage, exception.getMessage());
  }

  /**
   * Tests the {@link CodeElements#Generated} method.
   */
  @Test
  public void testGeneratedAnnotation() {
    String[] values = { "value1", "value2" };
    Generated annotation = new CodeElements.Generated() {
      @Override
      public String[] value() {
        return values;
      }

      @Override
      public Class<? extends Annotation> annotationType() {
        return Generated.class;
      }
    };

    assertNotNull(annotation);
    assertArrayEquals(values, annotation.value());
  }

  /**
   * Tests the {@link CodeElements#Generated} method.
   *
   * @throws Exception
   */
  @Test
  public void testGeneratedAnnotationOnMethod() throws Exception {
    Generated generated = TestClass.class.getAnnotation(Generated.class);
    assertEquals("Jacoco", TestClass.class.getAnnotation(Generated.class).value()[0]);
    assertEquals(1, generated.value().length);
  }

  /**
   * Used to test the {@link CodeElements#Generated} annotation.
   */
  @SuppressWarnings("checkstyle:nowhitespaceafter")
  @Generated({ "Jacoco" })
  static class TestClass {
    public void myMethod() {
      // do something
    }
  }
}
