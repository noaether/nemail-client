package ca.noae.Login;

import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

import ca.noae.Objects.UserInfo;

public class Login {
  public static UserInfo startAuthentication(Scanner scanner) {
    // ROADMAP
    // -> config/query email
    // -> config/query password
    // -> config/query mailbox

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
