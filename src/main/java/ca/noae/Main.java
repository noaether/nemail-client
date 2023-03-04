package ca.noae;

import java.util.List;
import java.util.Scanner;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.Transport;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            String password = null;
            String emailAddress = null;
            String mailbox = "inbox";
            String reply = null;

            // Prompt the user for their email credentials
            System.out.print("Enter your email address: ");
            emailAddress = scanner.nextLine();
            System.out.print("Enter your password: ");
            password = scanner.nextLine();

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
                default:
                    System.out.println("Invalid selection. Defaulting to inbox.");
                    mailbox = "inbox";
                    break;
            }
            new Authentication("smtp.migadu.com", "465", "pop.migadu.com", "995", "imap.migadu.com", "993",
                    emailAddress, password);

            // Authenticate the user's credentials
            try {
                Transport smtpTransport = Authentication.getSMTPTransport();
                System.out.println(smtpTransport);
                Store imapStore = Authentication.getIMAPStore();
                System.out.println(imapStore);
                new Mailbox(imapStore);
                new EmailClient(smtpTransport);
                List<String[]> latestMessages = Mailbox.getLatestMessages(mailbox);
                List<String> latestSubjects = Mailbox.getSubjects();
                List<String> latestSenders = Mailbox.getFroms();
                List<String> latestDates = Mailbox.getDates();
                List<String> latestBodies = Mailbox.getBodies();

                // Display the latest messages
                displayLatestMessages(latestMessages);
                // Prompt the user for the message they want to read
                System.out.print("Enter the number of the message you want to read: ");
                int messageSelection = scanner.nextInt();
                displayMessage(messageSelection, latestMessages);

                // Go back to list of emails, reply, or quit
                System.out.println("Select an option:");
                System.out.println("1. Go back to list of emails");
                System.out.println("2. Reply");
                System.out.println("3. Quit");
                System.out.print("Enter your selection: ");
                int optionSelection = scanner.nextInt();

                switch (optionSelection) {
                    case 1:
                        // Go back to list of emails
                        break;
                    case 2:
                        System.out.println("Enter your reply: ");
                        while (true) {
                            reply = scanner.nextLine();
                            if (!reply.isEmpty()) {
                                break;
                            }
                        }

                        // Send the email
                        EmailClient.sendEmail(latestSenders.get(messageSelection - 1), emailAddress,
                                "RE: " + latestSubjects.get(messageSelection - 1), reply);
                        break;

                    case 3:
                        // Quit
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid selection. Defaulting to inbox.");
                        break;
                }

                imapStore.close();
                System.out.println("Authentication successful!");
            } catch (Exception e) {
                System.out.println("Authentication failed.");
                e.printStackTrace();
            }
        }
    }

    public static void displayLatestMessages(List<String[]> latestMessages) {
        System.out.println("------------------------------------------------");
        for (int i = 0; i < latestMessages.size(); i++) {
            System.out.println("Message: " + (i + 1));
            System.out.println("Subject: " + latestMessages.get(i)[0]);
            System.out.println("From: " + latestMessages.get(i)[1]);
            System.out.println("Date: " + latestMessages.get(i)[2]);
            System.out.println("------------------------------------------------");
        }
    }

    public static void displayMessage(int messageSelection, List<String[]> latestMessages) throws MessagingException {
        System.out.println("------------------------------------------------");
        System.out.println("Message: " + messageSelection);
        System.out.println("Subject: " + latestMessages.get(messageSelection - 1)[0]);
        System.out.println("From: " + latestMessages.get(messageSelection - 1)[1]);
        System.out.println("Date: " + latestMessages.get(messageSelection - 1)[2]);
        System.out.println("Body: " + latestMessages.get(messageSelection - 1)[3]);
        System.out.println("------------------------------------------------");
    }
}
