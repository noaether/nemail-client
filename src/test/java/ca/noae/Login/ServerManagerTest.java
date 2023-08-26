package ca.noae.Login;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.Scanner;

class ServerManagerTest {

  @Test
  void testGetServerArrayWithValidEmail() {
    String email = "example@gmail.com";
    String[] serverArray = ServerManager.getServerArray(email);
    assertNotNull(serverArray);
    assertEquals("smtp.gmail.com", serverArray[0]);
    assertEquals("imap.gmail.com", serverArray[1]);
    assertEquals("pop.gmail.com", serverArray[2]);
    assertEquals("587", serverArray[3]);
    assertEquals("995", serverArray[4]);
    assertEquals("993", serverArray[5]);
  }

  @Test
  void testGetServerArrayWithInvalidEmail() {
    Scanner scanner = Mockito.mock(Scanner.class);
    assertDoesNotThrow(() -> {
      ConfigManager.initConfigManager(scanner, "app.properties");
    });
    String email = "example@non-existent-domain.com";
    String[] serverArray = ServerManager.getServerArray(email);

    assertNotNull(serverArray);
  }

  @Test
  void testGetServerArrayWithEmptyEmail() {
    String email = "";
    assertThrows(IllegalArgumentException.class, () -> {
      ServerManager.getServerArray(email);
    });
  }

  @Test
  void testGetServerArrayWithNullEmail() {
    String email = null;
    assertThrows(NullPointerException.class, () -> {
      ServerManager.getServerArray(email);
    });
  }
}
