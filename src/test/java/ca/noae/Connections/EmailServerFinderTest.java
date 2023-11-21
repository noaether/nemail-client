package ca.noae.Connections;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

import java.net.UnknownHostException;

import javax.naming.TimeLimitExceededException;

@SuppressWarnings("checkstyle:designforextension")
public class EmailServerFinderTest {

  /**
   * The GreenMail object to be used in the tests.
   */
  private static GreenMail greenMail;

  @BeforeEach
  void setUp() {
    EmailServerFinder.initMaps();

    int smtpPort = 9000;
    int pop3Port = 10000;
    int imapPort = 11000;
    greenMail = new GreenMail(new ServerSetup[] {
        new ServerSetup(smtpPort, "localhost", ServerSetup.PROTOCOL_SMTP),
        new ServerSetup(pop3Port, "localhost", ServerSetup.PROTOCOL_POP3),
        new ServerSetup(imapPort, "localhost", ServerSetup.PROTOCOL_IMAP)
    });
    greenMail.setUser("test@example.com", "testpassword");
    greenMail.start();
  }

  @AfterEach
  void tearDown() {
    greenMail.stop();
  }

  @Nested
  class TestCheck {
    @Test
    public void testCheckAutodetectInMap() {
      String email = "john.doe@gmail.com";
      assertDoesNotThrow(() -> {
        String[] servers = EmailServerFinder.check(email);
        assertNotNull(servers[0]);
        assertNotNull(servers[1]);
        assertNotNull(servers[2]);
      });
    }

    @Test
    public void testCheckAutodetectNotInMap() {
      String email = "john.doe@migadu.com";

      assertDoesNotThrow(() -> {
        String[] servers = EmailServerFinder.check(email);

        assertEquals("smtp.migadu.com", servers[0]);
        assertEquals("imap.migadu.com", servers[1]);
        assertEquals("pop.migadu.com", servers[2]);
      });
    }

    @Test
    public void testCheckAutodetectNoSMTP() {
      greenMail.getSmtp().stopService();

      String email = "test@localhost";
      assertThrows(UnknownHostException.class, () -> {
        EmailServerFinder.check(email);
      });
    }

    @Test
    public void testCheckAutodetectNoPOP() {
      greenMail.getPop3().stopService();

      String email = "test@localhost";
      assertDoesNotThrow(() -> {
        String[] servers = EmailServerFinder.check(email);
        String[] expected = new String[] {
            "localhost", null, "localhost"
        };
        assertArrayEquals(expected, servers);
      });
    }

    @Test
    public void testCheckAutodetectNoIMAP() {
      greenMail.getImap().stopService();

      String email = "test@localhost";
      assertDoesNotThrow(() -> {
        String[] servers = EmailServerFinder.check(email);
        String[] expected = new String[] {
            "localhost", "localhost", null
        };
        assertArrayEquals(expected, servers);
      });
    }

    @Test
    public void testCheckAutodetectNoPOPnoIMAP() {
      greenMail.getPop3().stopService();
      greenMail.getImap().stopService();

      String email = "test@localhost";
      assertThrows(UnknownHostException.class, () -> {
        EmailServerFinder.check(email);
      });
    }

    /**
     * Tests the {@link EmailServerFinder#check(String)} method.
     */
    @Test
    public void testCheckInvalidEmail() {
      String email = "invalid-email";
      assertThrows(IllegalArgumentException.class, () -> {
        EmailServerFinder.check(email);
      });
    }

    @Test
    public void testCheckInvalidDomain() {
      String email = "validuser@invaliddomain.not";
      assertThrows(TimeLimitExceededException.class, () -> {
        EmailServerFinder.check(email);
      });
    }
  }

  @Nested
  class TestCheckResponse {
    @Test
    public void testCheckResponseSMTP() {
      String[] responses = new String[] {
          "250-STARTTLS", "250 STARTTLS", "220", "ESMTP MAIL"
      };
      for (String response : responses) {
        assertTrue(EmailServerFinder.checkResponse("SMTP", response));
      }
    }

    @Test
    public void testCheckResponsePOP3() {
      String[] responses = new String[] {
          "STLS", "OK"
      };
      for (String response : responses) {
        assertTrue(EmailServerFinder.checkResponse("POP3", response));
      }
    }

    @Test
    public void testCheckResponseIMAP() {
      String[] responses = new String[] {
          "IMAP4rev1", "IMAP4", "IMAP", "OK"
      };
      for (String response : responses) {
        assertTrue(EmailServerFinder.checkResponse("IMAP", response));
      }
    }

    @Test
    public void testCheckResponse_NULL() {
      assertThrows(NullPointerException.class, () -> {
        EmailServerFinder.checkResponse("NULL", "NULL");
      });
    }
  }

  @Nested
  class TestGetCommand {
    @Test
    public void testGetCommandSMTP() {
      assertEquals("EHLO localhost\r\n", EmailServerFinder.getCommand("SMTP"));
    }

    @Test
    public void testGetCommandPOP3() {
      assertEquals("CAPA\r\n", EmailServerFinder.getCommand("POP3"));
    }

    @Test
    public void testGetCommandIMAP() {
      assertEquals("a001 CAPABILITY\r\n", EmailServerFinder.getCommand("IMAP"));
    }

    @Test
    public void testGetCommand_NULL() {
      assertNull(EmailServerFinder.getCommand("NULL"));
    }
  }

  @Nested
  class TestProbePorts {
    @Test
    public void testProbeSMTPPorts() {
      String[] possibleHosts = new String[] {
          "localhost", "notahost.bullshit"
      };
      String[] possiblePorts = new String[] {
          "9000", "10000", "11000"
      };
      assertEquals("localhost", EmailServerFinder.probePorts(possibleHosts, possiblePorts));
    }

    @Test
    public void testProbePOP3Ports() {
      String[] possibleHosts = new String[] {
          "localhost", "notahost.bullshit"
      };
      String[] possiblePorts = new String[] {
          "9000", "10000", "11000"
      };
      assertEquals("localhost", EmailServerFinder.probePorts(possibleHosts, possiblePorts));
    }

    @Test
    public void testProbeIMAPPorts() {
      String[] possibleHosts = new String[] {
          "localhost", "notahost.bullshit"
      };
      String[] possiblePorts = new String[] {
          "9000", "10000", "11000"
      };
      assertEquals("localhost", EmailServerFinder.probePorts(possibleHosts, possiblePorts));
    }

    @Test
    public void testProbePortsNULL() {
      String[] possibleHosts = new String[] {
          "notahost.bullshit"
      };
      String[] possiblePorts = new String[] {
          "9000", "10000", "11000"
      };

      assertNull(EmailServerFinder.probePorts(possibleHosts, possiblePorts));
    }
  }

  @Nested
  class TestProbeCapabilities {
    @Test
    public void testProbeCapabilitiesSMTP() {
      String[] possibleHosts = new String[] {
          "localhost", "notahost.bullshit"
      };
      String[] possiblePorts = new String[] {
          "9000", "10000", "11000"
      };
      String protocol = "SMTP";
      String expected = "localhost";
      String actual = EmailServerFinder.probeCapabilities(possibleHosts, possiblePorts, protocol);
      assertEquals(expected, actual);
    }

    @Test
    public void testProbeCapabilitiesPOP3() {
      String[] possibleHosts = new String[] {
          "localhost", "notahost.bullshit"
      };
      String[] possiblePorts = new String[] {
          "9000", "10000", "11000"
      };
      String protocol = "POP3";
      String expected = "localhost";
      String actual = EmailServerFinder.probeCapabilities(possibleHosts, possiblePorts, protocol);
      assertEquals(expected, actual);
    }

    @Test
    public void testProbeCapabilitiesIMAP() {
      String[] possibleHosts = new String[] {
          "localhost", "notahost.bullshit"
      };
      String[] possiblePorts = new String[] {
          "9000", "10000", "11000"
      };
      String protocol = "IMAP";
      String expected = "localhost";
      String actual = EmailServerFinder.probeCapabilities(possibleHosts, possiblePorts, protocol);
      assertEquals(expected, actual);
    }

    @Test
    public void testProbeCapabilitiesNULL() {
      String[] possibleHosts = new String[] {
          "notahost.bullshit"
      };
      String[] possiblePorts = new String[] {
          "9000", "10000", "11000"
      };
      String protocol = "NULL";
      String actual = EmailServerFinder.probeCapabilities(possibleHosts, possiblePorts, protocol);
      assertNull(actual);
    }
  }
}
