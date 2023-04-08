package ca.noae.Actions;

import javax.mail.MessagingException;
import java.io.FileOutputStream;
import java.io.IOException; // Import the IOException class to handle errors

public class FileHandler {
    /**
     * Saves the selected email to a file named "mail.eml".
     *
     * @param messageSelection the index of the email to save in the
     *                         Mailbox.messages array
     * @throws RuntimeException if there is an error while saving the email
     */
    public static void saveEmail(int messageSelection) {
        try {
            Mailbox.messages[messageSelection - 1].writeTo(new FileOutputStream("mail.eml"));
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}