package ca.noae.Login;

import java.net.UnknownHostException;

import ca.noae.Connections.EmailServerFinder;

public final class ServerManager {
  /**
   *
   * This is a utility class containing only static methods and cannot be
   * instantiated.
   */
  private ServerManager() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  /**
   * Attempts to automatically find the email server information for the given
   * email address.
   * If successful, returns an array of six strings containing the SMTP, POP, and
   * IMAP servers and ports.
   * If unsuccessful, checks the config or prompts the user for the server
   * information and returns an array with six empty strings.
   *
   * @param email the email address to find server information for
   * @return an array of six strings containing the SMTP, POP, and IMAP servers
   *         and ports
   */
  public static String[] getServerArray(final String email) {
    String[] serverArray = new String[6];

    try {
      String[] foundServers = EmailServerFinder.check(email);
      serverArray[0] = foundServers[0]; // SMTP
      serverArray[1] = foundServers[1]; // POP
      serverArray[2] = foundServers[2]; // IMAP
      serverArray[3] = "587"; // SMTP Port
      serverArray[4] = "995"; // POP Port
      serverArray[5] = "993"; // IMAP Port
    } catch (UnknownHostException e) {
      serverArray[0] = ConfigManager.getPropOrQuery("smtpServer", "Enter your SMTP server: ");
      serverArray[1] = ConfigManager.getPropOrQuery("popServer", "Enter your POP server: ");
      serverArray[2] = ConfigManager.getPropOrQuery("imapServer", "Enter your IMAP server:");

      serverArray[3] = ConfigManager.getPropOrQuery("smtpPort", "Enter your SMTP port: ");
      serverArray[4] = ConfigManager.getPropOrQuery("popPort", "Enter your POP port: ");
      serverArray[5] = ConfigManager.getPropOrQuery("imapPort", "Enter your IMAP port: ");
    }
    return serverArray;
  }
}
