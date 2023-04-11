package ca.noae.Actions;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.FileOutputStream;
import java.io.IOException; // Import the IOException class to handle errors

public final class FileHandler {

    /**
     *
     * This is a utility class containing only static methods and cannot be
     * instantiated.
     */
    private FileHandler() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Saves the selected email to a file named "mail.eml".
     *
     * @param messageSelection the index of the email to save in the
     *                         Mailbox.messages array
     * @throws RuntimeException if there is an error while saving the email
     */
    public static void saveEmail(final Message message) {
        try {
            message.writeTo(new FileOutputStream("mail.eml"));
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
