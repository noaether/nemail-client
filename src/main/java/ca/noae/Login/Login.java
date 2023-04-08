package ca.noae.Login;

import ca.noae.Connections.EmailServerFinder;
import ca.noae.Objects.UserInfo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.Scanner;

public class Login {
    public static UserInfo startAuthentication(Scanner scanner) {
        Properties prop = new Properties();
        String fileName = "app.config";
        try () {
            String[] returnArray = new String[9];

            String emailAddress = prop.getProperty("email");
            System.out.print(
                    emailAddress == null ? "Enter your email address: " : "Loading email from config file... \n");
            if (emailAddress == null) {
                emailAddress = scanner.nextLine();
            }

            String password = prop.getProperty("password");
            System.out.print(password == null ? "Enter your password: " : "Loading password from config file... \n");
            if (password == null) {
                password = scanner.nextLine();
            }

            String mailbox = prop.getProperty("mailbox");
            if (mailbox == null) {
                System.out.println("Select a mailbox:");
                System.out.println("1. Inbox");
                System.out.println("2. Sent");
                System.out.println("3. Trash");
                System.out.print("Enter your selection. From now on, input \"-1\" to safely quit: ");
                int mailboxSelection = scanner.nextInt();
                switch (mailboxSelection) {
                    case 1 -> mailbox = "inbox";
                    case 2 -> mailbox = "sent";
                    case 3 -> mailbox = "trash";
                    case -1 -> System.exit(0);
                    default -> {
                        System.out.println("Invalid selection. Defaulting to inbox.");
                        mailbox = "inbox";
                    }
                }
            } else {
                System.out.println("Loading mailbox from config file... \n ");
            }

            String[] serverArray = new String[6];

            try {
                String[] servers = EmailServerFinder.check(emailAddress);
                serverArray[0] = servers[0]; // SMTP
                serverArray[1] = servers[1]; // POP
                serverArray[2] = servers[2]; // IMAP
                serverArray[3] = "587"; // SMTP
                serverArray[4] = "993"; // POP
                serverArray[5] = "995"; // IMAP
            } catch (UnknownHostException e) {
                System.out.println("Enter SMTP server address and port [smtp.yourdomain.com:587]: ");
                String smtpServer = scanner.nextLine();
                String[] smtpServerSplit = smtpServer.split(":");
                serverArray[0] = smtpServerSplit[0];
                serverArray[3] = smtpServerSplit[1];

                System.out.println("Enter IMAP server address and port [imap.yourdomain.com:993]: ");
                String imapServer = scanner.nextLine();
                String[] imapServerSplit = imapServer.split(":");
                serverArray[1] = imapServerSplit[0];
                serverArray[4] = imapServerSplit[1];

                System.out.println("Enter POP3 server address and port [pop.yourdomain.com:995]: ");
                String popServer = scanner.nextLine();
                String[] popServerSplit = popServer.split(":");
                serverArray[2] = popServerSplit[0];
                serverArray[5] = popServerSplit[1];
            }

            UserInfo user = new UserInfo(emailAddress, password, mailbox, serverArray[0], serverArray[1],
                    serverArray[2], serverArray[3], serverArray[4], serverArray[5]);

            return user;
        } catch (FileNotFoundException e) {
            // Ask user for config values
            System.out.println("Config file not found. Please enter your email address, password, and mailbox.");
            String[] returnArray = new String[3];

            System.out.println("Enter your email address: ");
            String emailAddress = scanner.nextLine();
            returnArray[0] = emailAddress;

            System.out.println("Enter your password: ");
            String password = scanner.nextLine();
            returnArray[1] = password;

            System.out.println("Enter your mailbox: ");
            String mailbox = scanner.nextLine();
            returnArray[2] = mailbox;

            String[] serverArray = new String[6];

            try {
                String[] servers = EmailServerFinder.check(emailAddress);
                serverArray[0] = servers[0]; // SMTP
                serverArray[1] = servers[1]; // POP
                serverArray[2] = servers[2]; // IMAP
                serverArray[3] = "587"; // SMTP
                serverArray[4] = "993"; // POP
                serverArray[5] = "995"; // IMAP
            } catch (UnknownHostException e1) {
                System.out.println("Enter SMTP server address and port [smtp.yourdomain.com:587]: ");
                String smtpServer = scanner.nextLine();
                String[] smtpServerSplit = smtpServer.split(":");
                serverArray[0] = smtpServerSplit[0];
                serverArray[3] = smtpServerSplit[1];

                System.out.println("Enter IMAP server address and port [imap.yourdomain.com:993]: ");
                String imapServer = scanner.nextLine();
                String[] imapServerSplit = imapServer.split(":");
                serverArray[1] = imapServerSplit[0];
                serverArray[4] = imapServerSplit[1];

                System.out.println("Enter POP3 server address and port [pop.yourdomain.com:995]: ");
                String popServer = scanner.nextLine();
                String[] popServerSplit = popServer.split(":");
                serverArray[2] = popServerSplit[0];
                serverArray[5] = popServerSplit[1];
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}