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

  public static String getEmailAddress() {
    return emailAddress;
  }

  public static String getPassword() {
    return password;
  }

  public static String getMailbox() {
    return mailbox;
  }

  public static String getSmtpServerAddress() {
    return smtpServerAddress;
  }

  public static String getPopServerAddress() {
    return popServerAddress;
  }

  public static String getImapServerAddress() {
    return imapServerAddress;
  }

  public static String getSmtpServerPort() {
    return smtpServerPort;
  }

  public static String getPopServerPort() {
    return popServerPort;
  }

  public static String getImapServerPort() {
    return imapServerPort;
  }
}
