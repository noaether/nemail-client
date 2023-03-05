package ca.noae;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.Transport;

import ca.noae.Actions.EmailClient;
import ca.noae.Actions.Mailbox;
import ca.noae.Connections.Authentication;
import ca.noae.Connections.EmailServerFinder;
import ca.noae.User.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            String password = null;
            String emailAddress = null;
            String mailbox = "inbox";

            String smtpServerAddress;
            String smtpServerPort = "587";
            String imapServerAddress;
            String imapServerPort = "993";
            String popServerAddress;
            String popServerPort = "995";

            // Prompt the user for their email credentials
            System.out.print("Enter your email address: ");
            emailAddress = scanner.nextLine();
            System.out.print("Enter your password: ");
            password = scanner.nextLine();

            try {
                String[] servers = EmailServerFinder.check(emailAddress);
                smtpServerAddress = servers[0];
                imapServerAddress = servers[1];
                popServerAddress = servers[2];
            } catch(UnknownHostException e) {
                System.out.println("Enter SMTP server address and port [smtp.yourdomain.com:587]: ");
                String smtpServer = scanner.nextLine();
                String[] smtpServerSplit = smtpServer.split(":");
                smtpServerAddress = smtpServerSplit[0];
                smtpServerPort = smtpServerSplit[1];

                System.out.println("Enter IMAP server address and port [imap.yourdomain.com:993]: ");
                String imapServer = scanner.nextLine();
                String[] imapServerSplit = imapServer.split(":");
                imapServerAddress = imapServerSplit[0];
                imapServerPort = imapServerSplit[1];

                System.out.println("Enter POP3 server address and port [pop.yourdomain.com:995]: ");
                String popServer = scanner.nextLine();
                String[] popServerSplit = popServer.split(":");
                popServerAddress = popServerSplit[0];
                popServerPort = popServerSplit[1];
            }

            // Prompt the user for the mailbox they want to access
            System.out.println("Select a mailbox:");
            System.out.println("1. Inbox");
            System.out.println("2. Sent");
            System.out.println("3. Trash");
            System.out.print("Enter your selection: ");
            int mailboxSelection = scanner.nextInt();
            switch (mailboxSelection) {
                case 1:
                    mailbox = "inbox";
                    break;
                case 2:
                    mailbox = "sent";
                    break;
                case 3:
                    mailbox = "trash";
                    break;
                case -1:
                    System.exit(0);
                default:
                    System.out.println("Invalid selection. Defaulting to inbox.");
                    mailbox = "inbox";
                    break;
            }
            new Authentication(smtpServerAddress, smtpServerPort, popServerAddress, popServerPort, imapServerAddress, imapServerPort,
                    emailAddress, password);

            // Authenticate the user's credentials
            try {
                Transport smtpTransport = Authentication.getSMTPTransport();
                System.out.println(smtpTransport);
                Store finalStore = null;
                try {
                    finalStore = Authentication.getIMAPStore();
                } catch (Exception e) {
                    try {
                        finalStore = Authentication.getPOP3Store();
                    } catch (Exception e1) {
                        System.out.println("Unable to connect to the server. Please try again later.");
                        System.exit(0);
                    }
                };
                System.out.println(finalStore);
                new Mailbox(finalStore);
                new EmailClient(smtpTransport);
                List<String[]> latestMessages = Mailbox.getLatestMessages(mailbox);
                List<String> latestSubjects = Mailbox.getSubjects();
                List<String> latestSenders = Mailbox.getFroms();
                List<String> latestDates = Mailbox.getDates();
                List<String> latestBodies = Mailbox.getBodies();

                boolean cleanQuit = false;

                while (!cleanQuit) {
                    ConsoleUI.clearScreen();
                    ConsoleUI.createTable(new String[] { "#", "Subject", "From", "Dates" },
                            latestMessages.toArray(new String[0][0]));
                    // Ask for read, reply or quit
                    System.out.println("Select an option:");
                    System.out.println("1. Read");
                    System.out.println("2. Reply");
                    System.out.print("Enter your selection. Enter -1 to quit at any time: ");

                    int rrqOption = scanner.nextInt();
                    switch (rrqOption) {
                        case 1:
                            System.out.print("Enter the number of the message you want to read: ");
                            int read_messageOption = scanner.nextInt();
                            if (read_messageOption == -1) {
                                cleanQuit = true;
                            }
                            int read_messageSelection = read_messageOption;
                            displayMessage(read_messageSelection, latestMessages);

                            // Go back to list of emails, reply, or quit
                            System.out.println("Select an option:");
                            System.out.println("1. Go back to list of emails");
                            System.out.println("2. Reply");
                            System.out.print("Enter your selection: ");
                            int read_grpOption = scanner.nextInt();

                            switch (read_grpOption) {
                                case 1:
                                    break;
                                case 2:
                                    String read_reply_Reply = null;
                                    System.out.println("Enter your reply: ");
                                    while (true) {
                                        read_reply_Reply = scanner.nextLine();
                                        if (!read_reply_Reply.isEmpty()) {
                                            break;
                                        }
                                    }
                                    // Send the email
                                    EmailClient.sendEmail(latestSenders.get(read_messageSelection - 1), emailAddress,
                                            "RE: " + latestSubjects.get(read_messageSelection - 1), read_reply_Reply);
                                    break;

                                case -1:
                                    // Quit
                                    cleanQuit = true;
                                    break;
                                default:
                                    System.out.println("Invalid selection. Defaulting to inbox.");
                                    break;
                            }
                            break;
                        case 2:
                            System.out.print("Enter the number of the message you want to reply to: ");
                            int reply_messageOption = scanner.nextInt();
                            if (reply_messageOption == -1) {
                                cleanQuit = true;
                            }
                            int reply_messageSelection = reply_messageOption;
                            String reply_Reply = null;
                            System.out.println("Enter your reply: ");
                            while (true) {
                                reply_Reply = scanner.nextLine();
                                if (!reply_Reply.isEmpty()) {
                                    break;
                                }
                            }
                            // Send the email
                            EmailClient.sendEmail(latestSenders.get(reply_messageSelection - 1), emailAddress,
                                    "RE: " + latestSubjects.get(reply_messageSelection - 1), reply_Reply);
                            break;
                        case -1:
                            // Quit
                            cleanQuit = true;
                            break;
                        default:
                            System.out.println("Invalid selection. Defaulting to inbox.");
                            break;
                    }
                }

                finalStore.close();
            } catch (Exception e) {
                System.out.println("Authentication failed.");
                e.printStackTrace();
            }
        }
    }

    public static void displayMessage(int messageSelection, List<String[]> latestMessages) throws MessagingException {
        System.out.println("------------------------------------------------");
        System.out.println("Message: " + messageSelection);
        System.out.println("Subject: " + latestMessages.get(messageSelection - 1)[1]);
        System.out.println("From: " + latestMessages.get(messageSelection - 1)[2]);
        System.out.println("Date: " + latestMessages.get(messageSelection - 1)[3]);
        System.out.println("Body: " + latestMessages.get(messageSelection - 1)[4]);
        System.out.println("------------------------------------------------");
    }
}
