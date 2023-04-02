package ca.noae.Actions;
import javax.mail.MessagingException;
import java.io.File;  // Import the File class
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;  // Import the IOException class to handle errors

public class FileHandler {
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
