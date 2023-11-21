package ca.noae.Connections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

import java.io.IOException;
import java.lang.Exception;

import javax.naming.TimeLimitExceededException;

public class EmailServerFinderTest {

  private static GreenMail greenMail;

  @BeforeAll
  static void setUp() {
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


  @AfterAll
  static void tearDown() {
    greenMail.stop();
  }

  @Nested
  class testCheck {
    @Test
    public void testCheckValidEmail() throws Exception {
      String email = "john.doe@gmail.com";
      String[] servers = EmailServerFinder.check(email);
      assertNotNull(servers[0]);
      assertNotNull(servers[1]);
      assertNotNull(servers[2]);
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

    @Test
    public void testCheckAutodetect() throws TimeLimitExceededException, IllegalArgumentException, IOException {
      String email = "john.doe@migadu.com";
      String[] servers = EmailServerFinder.check(email);

      assertEquals("smtp.migadu.com", servers[0]);
      assertEquals("imap.migadu.com", servers[1]);
    }
  }

  @Nested
  class testCheckResponse {
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
  class testGetCommand {
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
  class testProbePorts {
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
    public void testProbePorts_NULL() {
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
  class testProbeCapabilities {
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
    public void testProbeCapabilities_NULL() {
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