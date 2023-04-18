package ca.noae.TestSuite.Login;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Scanner;

import ca.noae.Login.ConfigManager;
import ca.noae.Login.Login;
import ca.noae.Objects.UserInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTest {

  @Test
  void testStartAuthentication() {
    Scanner scanner = Mockito.mock(Scanner.class);

    ConfigManager.initConfigManager(scanner, "app.properties");

    Mockito.when(scanner.nextLine())
        .thenReturn("test@example.com")
        .thenReturn("password")
        .thenReturn("1");

    UserInfo userInfo = Login.startAuthentication(scanner);

    assertEquals("test@example.com", userInfo.getEmailAddress());
    assertEquals("password", userInfo.getPassword());
    assertEquals("inbox", userInfo.getMailbox());
    // Assert other properties
  }
}
