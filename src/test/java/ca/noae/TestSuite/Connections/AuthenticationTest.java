package ca.noae.TestSuite.Connections;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.mail.util.MailConnectException;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

import ca.noae.Connections.Authentication;
import ca.noae.Objects.UserInfo;

public class AuthenticationTest {

  /**
   * The UserInfo object to be used in the tests.
   */
  private UserInfo testUserInfo;

  /**
   * The GreenMail object to be used in the tests.
   */
  private GreenMail greenMail;

  /**
   * Sets up the test environment.
   */
  @BeforeEach
  void setUp() {
    int smtpPort = 9000;
    int pop3Port = 10000;
    int imapPort = 11000;
    greenMail = new GreenMail(new ServerSetup[] {
        new ServerSetup(smtpPort, "localhost", ServerSetup.PROTOCOL_SMTP),
        new ServerSetup(pop3Port, "localhost", ServerSetup.PROTOCOL_POP3),
    });
    greenMail.setUser("test@example.com", "testpassword");
    greenMail.start();
    testUserInfo = new UserInfo("test@example.com", "testpassword", "inbox", "localhost", "localhost",
        "localhost", Integer.toString(smtpPort), Integer.toString(pop3Port), Integer.toString(imapPort));
    Authentication.init(testUserInfo);
  }

  /**
   * Tears down the test environment.
   */
  @AfterEach
  void tearDown() {
    Authentication.init(null);
    greenMail.stop();
  }

  /**
   * Tests the {@link Authentication#getSMTPTransport()} method.
   */
  @Test
  void testGetSMTPTransport() {
    assertDoesNotThrow(() -> Authentication.getSMTPTransport());
  }

  /**
   * Tests the {@link Authentication#getIMAPStore()} method.
   */
  @Test
  void testGetIMAPStore() {
    assertThrows(MailConnectException.class, () -> Authentication.getIMAPStore()); // test that exception throws when
                                                                                   // IMAP is not supported
  }

  /**
   * Tests the {@link Authentication#getPOP3Store()} method.
   */
  @Test
  void testGetPOP3Store() {
    assertDoesNotThrow(() -> Authentication.getPOP3Store());
  }

  /**
   * Tests the {@link Authentication#getStore()} method.
   */
  @Test
  void testGetStore() {
    assertDoesNotThrow(() -> Authentication.getStore());
  }

}
