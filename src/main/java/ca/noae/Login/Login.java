package ca.noae.Login;

import java.util.Scanner;

import ca.noae.Objects.UserInfo;

public final class Login {
  /**
   *
   * This is a utility class containing only static methods and cannot be
   * instantiated.
   */
  private Login() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  /**
   * Prompts the user to enter their email address, password, and select a mailbox
   * to connect to.
   * Uses the ConfigManager to retrieve properties or prompt the user for input.
   *
   * @param scanner the Scanner object to use for input
   * @return a UserInfo object containing the user's email, password, selected
   *         mailbox, and server information
   */
  public static UserInfo startAuthentication(final Scanner scanner) {
    String email = ConfigManager.getPropOrQuery("email");
    String password = ConfigManager.getPropOrQuery("password");
    String mailboxInt = ConfigManager.getPropOrQuery("mailbox",
        "Select a mailbox:\n1. Inbox\n2. Sent\n3. Trash\nEnter your selection: ");
    String mailbox = mailboxInt.equals("1") ? "inbox"
        : mailboxInt.equals("2") ? "sent" : mailboxInt.equals("3") ? "trash" : "inbox";

    String[] servers = ServerManager.getServerArray(email);

    return new UserInfo(email, password, mailbox, servers[0], servers[1], servers[2], servers[3], servers[4],
        servers[5]);
  }
}
