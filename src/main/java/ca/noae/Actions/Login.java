package ca.noae.Actions;

import ca.noae.Connections.EmailServerFinder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.Scanner;

public class Login {
    public static String[] startAuthentication(Scanner scanner) {
        Properties prop = new Properties();
        String fileName = "app.config";
        try (FileInputStream fis = new FileInputStream(fileName)) {
            String[] returnArray = new String[9];
            prop.load(fis);

            String emailAddress = prop.getProperty("email");
            System.out.print(emailAddress == null ? "Enter your email address: " : "Loading email from config file... \n");
            if(emailAddress == null) {
                emailAddress = scanner.nextLine();
            }
            returnArray[0] = emailAddress;

            String password = prop.getProperty("password");
            System.out.print(password == null ? "Enter your password: " : "Loading password from config file... \n");
            if(password == null) {
                password = scanner.nextLine();
            }
            returnArray[1] = password;

            String mailbox = prop.getProperty("mailbox");
            if(mailbox == null) {
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
            returnArray[2] = mailbox;

            try {
                String[] servers = EmailServerFinder.check(emailAddress);
                returnArray[3] = servers[0];
                returnArray[4] = servers[1];
                returnArray[5] = servers[2];
                returnArray[6] = "587";
                returnArray[7] = "993";
                returnArray[8] = "995";
            } catch(UnknownHostException e) {
                System.out.println("Enter SMTP server address and port [smtp.yourdomain.com:587]: ");
                String smtpServer = scanner.nextLine();
                String[] smtpServerSplit = smtpServer.split(":");
                returnArray[3] = smtpServerSplit[0];
                returnArray[6] = smtpServerSplit[1];

                System.out.println("Enter IMAP server address and port [imap.yourdomain.com:993]: ");
                String imapServer = scanner.nextLine();
                String[] imapServerSplit = imapServer.split(":");
                returnArray[4] = imapServerSplit[0];
                returnArray[7] = imapServerSplit[1];

                System.out.println("Enter POP3 server address and port [pop.yourdomain.com:995]: ");
                String popServer = scanner.nextLine();
                String[] popServerSplit = popServer.split(":");
                returnArray[5] = popServerSplit[0];
                returnArray[8] = popServerSplit[1];
            }

            return returnArray;
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

            return returnArray;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
