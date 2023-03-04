package ca.noae;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String username = null;
        String password = null;

        // Prompt the user for their email credentials
        System.out.print("Enter your email address: ");
        String emailAddress = scanner.nextLine();
        System.out.print("Enter your password: ");
        password = scanner.nextLine();

        // Authenticate the user's credentials
        if (Authentication.authenticate(emailAddress, password)) {
            System.out.println("Authentication successful!");

            // Select the mailbox to access
            System.out.println("Select a mailbox to access:");
            System.out.println("1. Inbox");
            System.out.println("2. Sent");
            System.out.println("3. Drafts");
            int mailboxOption = scanner.nextInt();
            scanner.nextLine();

            switch (mailboxOption) {
                case 1:
                    Mailbox.selectMailbox("inbox");
                    break;
                case 2:
                    Mailbox.selectMailbox("sent");
                    break;
                case 3:
                    Mailbox.selectMailbox("drafts");
                    break;
                default:
                    System.out.println("Invalid option");
                    System.exit(1);
            }

            // Prompt the user for the email subject and body
            System.out.print("Enter the email subject: ");
            String subject = scanner.nextLine();
            System.out.print("Enter the email body: ");
            String body = scanner.nextLine();

            // Compose and send the email
            EmailClient.sendEmail(emailAddress, subject, body);

            // Retrieve the last 5 email messages from the selected mailbox
            System.out.println("Last 5 email messages:");
            String[] messages = Mailbox.retrieveEmails(5);
            for (String message : messages) {
                System.out.println(message);
            }

        } else {
            System.out.println("Authentication failed.");
        }
    }
}
