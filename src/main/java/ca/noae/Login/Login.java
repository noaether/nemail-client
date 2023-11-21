package ca.noae.Login;

import java.io.IOException;
import java.util.Scanner;

import javax.naming.TimeLimitExceededException;

import ca.noae.Objects.UserInfo;
import ca.noae.Objects.CodeElements.Generated;
import ca.noae.User.ConsoleUI;

public final class Login {
  /**
   *
   * This is a utility class containing only static methods and cannot be
   * instantiated.
   */
  @Generated({ "Utility class cannot be instantiated" })
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
   * @throws IOException
   */
  public static UserInfo startAuthentication(final Scanner scanner) throws IOException {
    String email = ConfigManager.getPropOrQuery("email");

    while (!isValidEmail(email)) {
      System.out.println("Invalid email address. Please check your information and try again.");
      ConsoleUI.clearScreen();
      email = ConfigManager.getPropOrQuery("email", "Invalid input. Enter your email: ");
    }

    String password = ConfigManager.getPropOrQuery("password");

    String mailboxInt = ConfigManager.getPropOrQuery("mailbox",
        "Select a mailbox:\n1. Inbox\n2. Sent\n3. Trash\nEnter your selection: ");

    while (!isValidOption(mailboxInt, new String[] { "1", "2", "3" })) {
      System.out.println("Invalid option. Please check your information and try again.");
      ConsoleUI.clearScreen();
      mailboxInt = ConfigManager.getPropOrQuery("mailbox",
          "Select a mailbox:\n1. Inbox\n2. Sent\n3. Trash\nEnter your selection: ");
    }

    String mailbox = mailboxInt.equals("1") ? "inbox"
        : mailboxInt.equals("2") ? "sent" : mailboxInt.equals("3") ? "trash" : "inbox";

    String[] servers;
    try {
      servers = ServerManager.getServerArray(email);
      return new UserInfo(email, password, mailbox, servers[0], servers[1], servers[2], servers[3], servers[4],
          servers[5]);
    } catch (TimeLimitExceededException e) {
      System.out.println("This domain doesn't seem to exist. Please check your information and try again.");
      ConsoleUI.clearScreen();
      return startAuthentication(scanner);
    }
  }

  /**
   * Checks if the given string is a valid email address.
   *
   * @param email the email address to check
   * @return true if the email address is valid, false otherwise
   */
  public static boolean isValidEmail(final String email) {
    return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
  }

  /**
   * Checks if the given string is a valid option.
   *
   * @param chosen  the option to check
   * @param options the list of valid options
   * @return true if the option is valid, false otherwise
   */
  public static boolean isValidOption(final String chosen, final String[] options) {
    for (String option : options) {
      if (chosen.equals(option)) {
        return true;
      }
    }
    return false;
  }
}
