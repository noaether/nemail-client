package ca.noae.Login;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;

class ServerManagerTest {

  @Test
  void testGetServerArrayWithValidEmail() throws UnknownHostException {
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
  void testGetServerArrayWithInvalidEmail() throws UnknownHostException {
    Scanner scanner = Mockito.mock(Scanner.class);
    assertDoesNotThrow(() -> {
      ConfigManager.initConfigManager(scanner, "app.properties");
    });

    // generate a 15char string random
    String generated = new Random().ints(97, 122 + 1)
    .limit(15)
    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
    .toString();

    String email = "example@" + generated + ".com";

    assertThrows(UnknownHostException.class, () -> {
      ServerManager.getServerArray(email);
    });
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
