package ca.noae;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import javax.mail.Store;
import javax.mail.Transport;

import ca.noae.Actions.EmailClient;
import ca.noae.Actions.FileHandler;
import ca.noae.Actions.Mailbox;
import ca.noae.Connections.Authentication;
import ca.noae.Login.ConfigManager;
import ca.noae.Login.Login;
import ca.noae.Objects.UserInfo;
import ca.noae.Objects.CodeElements.Generated;
import ca.noae.User.ConsoleUI;
import ca.noae.User.Utils;

@SuppressWarnings("checkstyle:innerassignment")
public final class Main {
    /**
     *
     * This is a utility class containing only static methods and cannot be
     * instantiated.
     */
    @Generated
    private Main() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     *
     * The main method of the email client application.
     * Initializes the configuration manager, authenticates the user, and displays
     * the user's latest email messages. It then prompts the user to select an
     * option,
     * such as reading or replying to a message. It also includes methods to handle
     * user input and send emails.
     *
     * @param args The command line arguments passed to the application.
     * @throws IOException If an I/O error occurs while reading input from the user.
     */
    public static void main(final String[] args) throws IOException {
        try (Scanner scanner = new Scanner(System.in)) {
            // Initialize
            ConfigManager.initConfigManager(scanner, "app.properties");
            UserInfo user = Login.startAuthentication(scanner);
            Authentication.init(user);

            // Authenticate the user's credentials
            try {
                Transport smtpTransport = Authentication.getSMTPTransport();
                // System.out.println(smtpTransport); TODO : Implement verbose mode

                Store finalStore = Authentication.getStore();

                EmailClient.init(smtpTransport);
                List<String[]> latestMessages = Mailbox.queryLatest(user.getMailbox(), finalStore);
                List<String> latestSubjects = Mailbox.getSubjects();
                List<String> latestSenders = Mailbox.getFroms();
                // List<String> latestDates = Mailbox.getDates();
                // List<String> latestBodies = Mailbox.getBodies();

                int cleanQuit = 69; // 69 is a magic number but I dont care because it works (as long as its not -1
                                    // its fine)

                while (cleanQuit != -1) {
                    ConsoleUI.clearScreen();
                    ConsoleUI.createTable(new String[] {
                            "#", "Subject", "From", "Dates" },
                            latestMessages.toArray(new String[0][0]));
                    // Ask for read, reply or quit
                    System.out.print("Select an option: " + "\n"
                            + "1. Read" + "\n" + "2. Reply" + "\n"
                            + "Enter your selection. Enter -1 to quit at any time: ");

                    int readReadReplyOption = scanner.nextInt();
                    switch (readReadReplyOption) {
                        case 1 -> {
                            System.out.print("Enter the number of the message you want to read: ");
                            int readMessageOption = scanner.nextInt() - 1;
                            cleanQuit = readMessageOption + 1;
                            Utils.displayMessage(readMessageOption, latestMessages);

                            // Go back to list of emails, reply, or quit
                            System.out.print("Select an option:" + "\n"
                                    + "1. Go back to list of emails" + "\n"
                                    + "2. Reply" + "\n"
                                    + "3. Save to file" + "\n"
                                    + "Enter your selection: ");
                            int readGoReplySaveOption = scanner.nextInt();
                            switch (readGoReplySaveOption) {
                                case 1:
                                    break;
                                case 2:
                                    String replyMessage;
                                    System.out.println("Enter your reply: ");
                                    do {
                                        replyMessage = scanner.nextLine();
                                    } while (replyMessage.isEmpty());
                                    // Send the email
                                    EmailClient.sendEmail(latestSenders.get(readMessageOption), user.getEmailAddress(),
                                            "RE: " + latestSubjects.get(readMessageOption), replyMessage);
                                    break;

                                case 3:
                                    FileHandler.saveEmail(Mailbox.getMessages()[readMessageOption]);
                                    break;
                                case -1:
                                    // Quit
                                    cleanQuit = -1;
                                    break;
                                default:
                                    System.out.println("Invalid selection. Defaulting to inbox.");
                                    break;
                            }
                        }
                        case 2 -> {
                            System.out.print("Enter the number of the message you want to reply to: ");
                            int replyMessageOption = scanner.nextInt();
                            cleanQuit = replyMessageOption;
                            String replyChoice;
                            System.out.println("Enter your reply: ");
                            do {
                                replyChoice = scanner.nextLine();
                            } while (replyChoice.isEmpty());
                            // Send the email
                            EmailClient.sendEmail(latestSenders.get(replyMessageOption - 1), user.getEmailAddress(),
                                    "RE: " + latestSubjects.get(replyMessageOption - 1), replyChoice);
                        }
                        case -1 ->
                            cleanQuit = readReadReplyOption;
                        default -> System.out.println("Invalid selection. Defaulting to inbox.");
                    }
                }

                finalStore.close();
            } catch (Exception e) {
                System.out.println("Authentication failed.");
                e.printStackTrace();
            }
        }
    }
}
