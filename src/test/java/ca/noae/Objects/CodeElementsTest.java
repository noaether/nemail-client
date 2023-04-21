package ca.noae.Objects;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import ca.noae.Objects.CodeElements.Generated;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class CodeElementsTest {

  /**
   * Tests the {@link CodeElements#InvalidLoginException(String)} method
   * @throws Exception
   */
  @Test
  public void testInvalidLoginException() throws Exception {
    String message = "Invalid login attempt";
    CodeElements.InvalidLoginException exception = new CodeElements.InvalidLoginException(message);
    assertEquals(message, exception.getMessage());
  }

  /**
   * Tests the {@link CodeElements#Generated} method
   * @throws Exception
   */
  @Test
  public void testGeneratedAnnotation() throws Exception {
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
