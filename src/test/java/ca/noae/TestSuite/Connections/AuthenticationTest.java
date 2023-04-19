package ca.noae.TestSuite.Connections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.mail.util.MailConnectException;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

import ca.noae.Connections.Authentication;
import ca.noae.Objects.UserInfo;

public class AuthenticationTest {

  private UserInfo testUserInfo;

  private GreenMail greenMail;

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
    assertThrows(MailConnectException.class, () -> Authentication.getIMAPStore()); // test that exception throws when
                                                                                   // IMAP is not supported
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