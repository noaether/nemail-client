package ca.noae.Objects;

public class UserInfo {
  private static String emailAddress;
  private static String password;
  private static String mailbox;

  private static String smtpServerAddress;
  private static String popServerAddress;
  private static String imapServerAddress;

  private static String smtpServerPort = "587";
  private static String popServerPort = "995";
  private static String imapServerPort = "993";

  public UserInfo(String emailAddress, String password, String mailbox, String smtpServerAddress,
      String popServerAddress, String imapServerAddress, String smtpServerPort, String popServerPort,
      String imapServerPort) {
    UserInfo.emailAddress = emailAddress;
    UserInfo.password = password;
    UserInfo.mailbox = mailbox;
    UserInfo.smtpServerAddress = smtpServerAddress;
    UserInfo.popServerAddress = popServerAddress;
    UserInfo.imapServerAddress = imapServerAddress;
    UserInfo.smtpServerPort = smtpServerPort;
    UserInfo.popServerPort = popServerPort;
    UserInfo.imapServerPort = imapServerPort;
  }

  public final String getEmailAddress() {
    return emailAddress;
  }

  public final String getPassword() {
    return password;
  }

  public final String getMailbox() {
    return mailbox;
  }

  public final String getSmtpServerAddress() {
    return smtpServerAddress;
  }

  public final String getPopServerAddress() {
    return popServerAddress;
  }

  public final String getImapServerAddress() {
    return imapServerAddress;
  }

  public final String getSmtpServerPort() {
    return smtpServerPort;
  }

  public final String getPopServerPort() {
    return popServerPort;
  }

  public final String getImapServerPort() {
    return imapServerPort;
  }
}
