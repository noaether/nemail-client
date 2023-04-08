package ca.noae;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputFilter;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;

import ca.noae.Actions.EmailClient;
import ca.noae.Actions.FileHandler;
import ca.noae.Actions.Mailbox;
import ca.noae.Connections.Authentication;
import ca.noae.Connections.EmailServerFinder;
import ca.noae.Login.Login;
import ca.noae.User.ConsoleUI;
import ca.noae.User.Utils;

public class Main {

    // TODO : Actually do the work in this branch

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            String[] loginDetails = Login.startAuthentication(scanner);

            String emailAddress = loginDetails[0];
            String password = loginDetails[1];
            String mailbox = loginDetails[2];

            String smtpServerAddress = loginDetails[3];
            String smtpServerPort = loginDetails[6];
            String imapServerAddress = loginDetails[4];
            String imapServerPort = loginDetails[7];
            String popServerAddress = loginDetails[5];
            String popServerPort = loginDetails[8];

            // Prompt the user for the mailbox they want to access
            new Authentication(smtpServerAddress, smtpServerPort, popServerAddress, popServerPort, imapServerAddress,
                    imapServerPort,
                    emailAddress, password);

            // Authenticate the user's credentials
            try {
                Transport smtpTransport = Authentication.getSMTPTransport();
                // System.out.println(smtpTransport); TODO : Implement verbose mode

                Store finalStore = Authentication.getStore();

                new Mailbox(finalStore);
                new EmailClient(smtpTransport);
                List<String[]> latestMessages = Mailbox.getLatestMessages(mailbox);
                List<String> latestSubjects = Mailbox.getSubjects();
                List<String> latestSenders = Mailbox.getFroms();
                // List<String> latestDates = Mailbox.getDates();
                // List<String> latestBodies = Mailbox.getBodies();

                int cleanQuit = 69;

                while (cleanQuit != -1) {
                    ConsoleUI.clearScreen();
                    ConsoleUI.createTable(new String[] { "#", "Subject", "From", "Dates" },
                            latestMessages.toArray(new String[0][0]));
                    // Ask for read, reply or quit
                    System.out.print("Select an option: " + "\n" +
                            "1. Read" + "\n" + "2. Reply" + "\n" +
                            "Enter your selection. Enter -1 to quit at any time: ");

                    int rrqOption = scanner.nextInt();
                    switch (rrqOption) {
                        case 1 -> {
                            System.out.print("Enter the number of the message you want to read: ");
                            int read_messageOption = scanner.nextInt() - 1;
                            cleanQuit = read_messageOption + 1;
                            Utils.displayMessage(read_messageOption, latestMessages);

                            // Go back to list of emails, reply, or quit
                            System.out.print("Select an option:" + "\n" +
                                    "1. Go back to list of emails" + "\n" +
                                    "2. Reply" + "\n" +
                                    "3. Save to file" + "\n" +
                                    "Enter your selection: ");
                            int read_grpOption = scanner.nextInt();
                            switch (read_grpOption) {
                                case 1:
                                    break;
                                case 2:
                                    String read_reply_Reply;
                                    System.out.println("Enter your reply: ");
                                    do {
                                        read_reply_Reply = scanner.nextLine();
                                    } while (read_reply_Reply.isEmpty());
                                    // Send the email
                                    EmailClient.sendEmail(latestSenders.get(read_messageOption), emailAddress,
                                            "RE: " + latestSubjects.get(read_messageOption), read_reply_Reply);
                                    break;

                                case 3:
                                    FileHandler.saveEmail(read_messageOption + 1);
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
                            int reply_messageOption = scanner.nextInt();
                            cleanQuit = reply_messageOption;
                            String reply_Reply;
                            System.out.println("Enter your reply: ");
                            do {
                                reply_Reply = scanner.nextLine();
                            } while (reply_Reply.isEmpty());
                            // Send the email
                            EmailClient.sendEmail(latestSenders.get(reply_messageOption - 1), emailAddress,
                                    "RE: " + latestSubjects.get(reply_messageOption - 1), reply_Reply);
                        }
                        case -1 ->
                            // Quit
                            cleanQuit = -1;
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
