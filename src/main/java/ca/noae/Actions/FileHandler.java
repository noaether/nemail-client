package ca.noae.Actions;

import javax.mail.Message;
import javax.mail.MessagingException;

import ca.noae.Objects.CodeElements.Generated;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException; // Import the IOException class to handle errors

public final class FileHandler {
    /**
     *
     * This is a utility class containing only static methods and cannot be
     * instantiated.
     */
    @Generated({ "Utility class cannot be instantiated" })
    private FileHandler() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Saves the selected email to a file named "mail.eml".
     *
     * @param message the Message object to save
     * @throws MessagingException if there is an error while parsing the email
     * @throws FileNotFoundException if the file cannot be created
     *
     * @throws IOException if there is an error while saving the email
     */
    public static void saveEmail(final Message message) throws FileNotFoundException, IOException, MessagingException {
        String[] info = {message.getSubject(), message.getFrom()[0].toString(), message.getSentDate().toString()};
        String filename = new StringBuilder().append(info[0]).append(" ").append(info[1]).append(" ").append(info[2]).toString();
        message.writeTo(new FileOutputStream(filename + ".eml"));
    }
}
