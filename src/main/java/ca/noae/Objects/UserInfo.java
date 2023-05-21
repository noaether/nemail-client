package ca.noae.Objects;

public class UserInfo {
  /**
   *
   * Constructor for the UserInfo class that initializes all the required fields
   * for email authentication.
   *
   * @param initEmailAddress      the email address of the user
   * @param initPassword          the password of the user's email account
   * @param initMailbox           the mailbox to be accessed (inbox, sent, trash)
   * @param initSmtpServerAddress the address of the SMTP server
   * @param initPopServerAddress  the address of the POP server
   * @param initImapServerAddress the address of the IMAP server
   * @param initSmtpServerPort    the port number of the SMTP server
   * @param initPopServerPort     the port number of the POP server
   * @param initImapServerPort    the port number of the IMAP server
   */
  public UserInfo(final String initEmailAddress, final String initPassword, final String initMailbox,
      final String initSmtpServerAddress,
      final String initPopServerAddress, final String initImapServerAddress, final String initSmtpServerPort,
      final String initPopServerPort,
      final String initImapServerPort) {
    assert !initEmailAddress.isEmpty() : "Email address cannot be null";
    assert !initPassword.isEmpty() : "Password cannot be null";
    assert !initSmtpServerAddress.isEmpty() : "SMTP server address cannot be null";
    assert !initPopServerAddress.isEmpty() : "POP server address cannot be null";
    assert !initImapServerAddress.isEmpty() : "IMAP server address cannot be null";

    UserInfo.emailAddress = initEmailAddress;
    UserInfo.password = initPassword;
    UserInfo.mailbox = initMailbox == null || initMailbox.isBlank() ? "inbox" : initMailbox;
    UserInfo.smtpServerAddress = initSmtpServerAddress;
    UserInfo.popServerAddress = initPopServerAddress;
    UserInfo.imapServerAddress = initImapServerAddress;
    UserInfo.smtpServerPort = initSmtpServerPort == null || initSmtpServerPort.isBlank()  ? "587" : initSmtpServerPort;
    UserInfo.popServerPort = initPopServerPort == null || initPopServerPort.isBlank()  ? "995" : initPopServerPort;
    UserInfo.imapServerPort = initImapServerPort == null || initImapServerPort.isBlank() ? "993" : initImapServerPort;
  }

  /** The user's email address. */
  private static String emailAddress;
  /** The user's password. */
  private static String password;
  /** The user's mailbox. */
  private static String mailbox;

  /** The user's SMTP server address. */
  private static String smtpServerAddress;
  /** The user's POP3 server address. */
  private static String popServerAddress;
  /** The user's IMAP server address. */
  private static String imapServerAddress;

  /** The default SMTP server port. */
  private static String smtpServerPort;
  /** The default POP3 server port. */
  private static String popServerPort;
  /** The default IMAP server port. */
  private static String imapServerPort;

  /**
   * Returns the email address associated with this user.
   *
   * @return the email address associated with this user
   */
  public final String getEmailAddress() {
    return emailAddress;
  }

  /**
   * Returns the password associated with this user.
   *
   * @return the password associated with this user
   */
  public final String getPassword() {
    return password;
  }

  /**
   * Returns the mailbox associated with this user.
   *
   * @return the mailbox associated with this user
   */
  public final String getMailbox() {
    return mailbox;
  }

  /**
   * Returns the SMTP server address associated with this user.
   *
   * @return the SMTP server address associated with this user
   */
  public final String getSmtpServerAddress() {
    return smtpServerAddress;
  }

  /**
   * Returns the POP3 server address associated with this user.
   *
   * @return the POP3 server address associated with this user
   */
  public final String getPopServerAddress() {
    return popServerAddress;
  }

  /**
   * Returns the IMAP server address associated with this user.
   *
   * @return the IMAP server address associated with this user
   */
  public final String getImapServerAddress() {
    return imapServerAddress;
  }

  /**
   * Returns the SMTP server port associated with this user.
   *
   * @return the SMTP server port associated with this user
   */
  public final String getSmtpServerPort() {
    return smtpServerPort;
  }

  /**
   * Returns the POP3 server port associated with this user.
   *
   * @return the POP3 server port associated with this user
   */
  public final String getPopServerPort() {
    return popServerPort;
  }

  /**
   * Returns the IMAP server port associated with this user.
   *
   * @return the IMAP server port associated with this user
   */
  public final String getImapServerPort() {
    return imapServerPort;
  }
}
