package ca.noae.Login;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Scanner;

import ca.noae.Objects.UserInfo;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTest {

  @Test
  void testStartAuthentication() throws IOException {
    Scanner scanner = Mockito.mock(Scanner.class);

    assertDoesNotThrow(() -> {
          ConfigManager.initConfigManager(scanner, "app.properties");
    });

    Mockito.when(scanner.nextLine())
        .thenReturn("test@outlook.com")
        .thenReturn("password")
        .thenReturn("1");

    assertDoesNotThrow(() -> {
      Login.startAuthentication(scanner);
    });

    UserInfo userInfo = Login.startAuthentication(scanner);

        assertEquals("test@outlook.com", userInfo.getEmailAddress());
    assertEquals("password", userInfo.getPassword());
    assertEquals("inbox", userInfo.getMailbox());
    // Assert other properties
  }
}
