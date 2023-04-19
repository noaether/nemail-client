package ca.noae.TestSuite.Login;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import ca.noae.Login.ConfigManager;
import ca.noae.Login.ServerManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

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
    ConfigManager.initConfigManager(scanner, "app.properties");
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
