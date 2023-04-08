package ca.noae.Objects;

public class UserInfo {
  public static String emailAddress;
  public static String password;
  public static String mailbox;

  public static String smtpServerAddress;
  public static String popServerAddress;
  public static String imapServerAddress;

  public static String smtpServerPort = "587";
  public static String popServerPort = "995";
  public static String imapServerPort = "993";

  public UserInfo(String emailAddress, String password, String mailbox, String smtpServerAddress, String popServerAddress, String imapServerAddress, String smtpServerPort, String popServerPort, String imapServerPort) {
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
}
