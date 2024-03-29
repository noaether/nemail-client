package ca.noae.Objects;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class UserInfoTest {

  /**
   * Tests the
   * {@link UserInfo#UserInfo(String, String, String, String, String, String, String, String, String)}
   * method
   *
   * Provides a full set of parameters to the constructor. Expected result is that
   * the constructor will create a new
   * UserInfo object with the provided parameters.
   */
  @Test
  public void createFullUser() {
    UserInfo user = new UserInfo("email", "password", "mailbox", "smtp", "pop", "imap", "smtpPort", "popPort",
        "imapPort");

    assertEquals("email", user.getEmailAddress());
    assertEquals("password", user.getPassword());
    assertEquals("mailbox", user.getMailbox());
    assertEquals("smtp", user.getSmtpServerAddress());
    assertEquals("pop", user.getPopServerAddress());
    assertEquals("imap", user.getImapServerAddress());
    assertEquals("smtpPort", user.getSmtpServerPort());
    assertEquals("popPort", user.getPopServerPort());
    assertEquals("imapPort", user.getImapServerPort());
  }

  /**
   * Tests the
   * {@link UserInfo#UserInfo(String, String, String, String, String, String, String, String, String)}
   * method
   *
   * Provides a null parameter to the constructor. Expected result is that the
   * constructor will throw a
   * NullPointerException.
   */
  @Test
  public void createNullUser() {
    assertThrows(NullPointerException.class, () -> {
      new UserInfo(null, null, null, null, null, null, null, null, null);
    });
  }

  /**
   * Tests the
   * {@link UserInfo#UserInfo(String, String, String, String, String, String, String, String, String)}
   * method
   *
   * Provides an empty parameter to the constructor. Expected result is that the
   * constructor will throw an
   * AssertionError.
   */
  @Test
  public void createEmptyUser() {
    assertThrows(AssertionError.class, () -> {
      new UserInfo("", "", "", "", "", "", "", "", "");
    });
  }

  /**
   * Tests the
   * {@link UserInfo#UserInfo(String, String, String, String, String, String, String, String, String)}
   * method
   *
   * Provides a partial set of parameters to the constructor. Expected result is
   * that the constructor will create a new
   * UserInfo object with the provided parameters and default values for the
   * remaining parameters.
   */
  @Test
  public void createPartialUserWithEmpty() {
    UserInfo user = new UserInfo("email", "password", "", "smtp", "pop", "imap", "", "", "");

    String[] expected = { "email", "password", "inbox", "smtp", "pop", "imap", "587", "995", "993" };
    String[] actual = { user.getEmailAddress(), user.getPassword(), user.getMailbox(), user.getSmtpServerAddress(),
        user.getPopServerAddress(), user.getImapServerAddress(), user.getSmtpServerPort(), user.getPopServerPort(),
        user.getImapServerPort() };

    assertArrayEquals(expected, actual);
  }

  /**
   * Tests the
   * {@link UserInfo#UserInfo(String, String, String, String, String, String, String, String, String)}
   * method
   *
   * Provides a partial set of parameters to the constructor and null values.
   * Expected result is that the constructor will create a new
   * UserInfo object with the provided parameters and default values for the
   * remaining parameters.
   */
  @Test
  public void createPartialUserWithNull() {
    UserInfo user = new UserInfo("email", "password", null, "smtp", "pop", "imap", null, null, null);

    assertEquals("email", user.getEmailAddress());
    assertEquals("password", user.getPassword());
    assertEquals("inbox", user.getMailbox());
    assertEquals("smtp", user.getSmtpServerAddress());
    assertEquals("pop", user.getPopServerAddress());
    assertEquals("imap", user.getImapServerAddress());
    assertEquals("587", user.getSmtpServerPort());
    assertEquals("995", user.getPopServerPort());
    assertEquals("993", user.getImapServerPort());
  }

  /**
   * Tests the
   * {@link UserInfo#UserInfo(String, String, String, String, String, String, String, String, String)}
   * method
   *
   * Provides a partial set of parameters to the constructor. Expected result is
   * that the constructor will create a new
   * UserInfo object with the provided parameters and default values for the
   * remaining parameters.
   */
  @Test
  public void createPartialUserWithNullAndEmpty() {
    UserInfo user = new UserInfo("email", "password", "", "smtp", "pop", "imap", null, null, null);

    String[] expected = { "email", "password", "inbox", "smtp", "pop", "imap", "587", "995", "993" };
    String[] actual = { user.getEmailAddress(), user.getPassword(), user.getMailbox(), user.getSmtpServerAddress(),
        user.getPopServerAddress(), user.getImapServerAddress(), user.getSmtpServerPort(), user.getPopServerPort(),
        user.getImapServerPort() };

    assertArrayEquals(expected, actual);
  }

  /**
   * Tests the
   * {@link UserInfo#UserInfo(String, String, String, String, String, String, String, String, String)}
   * method
   *
   * Provides a partial set of parameters to the constructor. Expected result is
   * that the constructor will create a new
   * UserInfo object with the provided parameters and default values for the
   * remaining parameters.
   */
  @Test
  public void createPartialUserWithEmptyAndNull() {
    UserInfo user = new UserInfo("email", "password", null, "smtp", "pop", "imap", "", "", "");

    String[] expected = { "email", "password", "inbox", "smtp", "pop", "imap", "587", "995", "993" };
    String[] actual = { user.getEmailAddress(), user.getPassword(), user.getMailbox(), user.getSmtpServerAddress(),
        user.getPopServerAddress(), user.getImapServerAddress(), user.getSmtpServerPort(), user.getPopServerPort(),
        user.getImapServerPort() };

    assertArrayEquals(expected, actual);
  }
}
