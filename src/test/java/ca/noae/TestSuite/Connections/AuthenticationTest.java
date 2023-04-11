package ca.noae.TestSuite.Connections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

import ca.noae.Connections.Authentication;
import ca.noae.Objects.UserInfo;

public class AuthenticationTest {

  private UserInfo testUserInfo;

  public GreenMail greenMail;

  @BeforeEach
  void setUp() {
    greenMail = new GreenMail(new ServerSetup[] {
        new ServerSetup(3025, "localhost", ServerSetup.PROTOCOL_SMTP),
        new ServerSetup(3110, "localhost", ServerSetup.PROTOCOL_POP3),
        new ServerSetup(3993, "localhost", ServerSetup.PROTOCOL_IMAP)
    });
    greenMail.setUser("test@example.com", "testpassword");
    greenMail.start();
    testUserInfo = new UserInfo("test@example.com", "testpassword", "inbox", "localhost", "localhost",
        "localhost", "3025", "3110", "3993");
    Authentication.init(testUserInfo);
  }

  @AfterEach
  void tearDown() {
    Authentication.smtpHost = null;
    Authentication.smtpPort = null;
    Authentication.pop3Host = null;
    Authentication.pop3Port = null;
    Authentication.imapHost = null;
    Authentication.imapPort = null;
    Authentication.emailAddress = null;
    Authentication.password = null;
    greenMail.stop();
  }

  @Test
  void testGetSMTPTransport() {
    assertDoesNotThrow(() -> Authentication.getSMTPTransport());
  }

  @Test
  void testGetIMAPStore() {
    assertDoesNotThrow(() -> Authentication.getIMAPStore());
  }

  @Test
  void testGetPOP3Store() {
    assertDoesNotThrow(() -> Authentication.getPOP3Store());
  }

  @Test
  void testGetStore() {
    assertDoesNotThrow(() -> Authentication.getStore());
  }

}
