package ca.noae.User;

import ca.noae.Actions.EmailClient;
import ca.noae.Actions.Mailbox;
import ca.noae.Objects.CodeElements.Generated;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.List;

public final class Utils {
    /**
     *
     * This is a utility class containing only static methods and cannot be
     * instantiated.
     */
    @Generated({"Utility class cannot be instantiated"})
    private Utils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     *
     * This method displays the selected message along with its details such as
     * subject, sender, and date
     * as well as the body of the message obtained by calling the
     * getTextFromMessage() method.
     *
     * @param messageSelection an integer representing the selected message from the
     *                         list of latest messages
     * @param latestMessages   a List of String arrays containing the details of the
     *                         latest messages
     * @throws MessagingException if there is an issue with the message being
     *                            displayed
     * @throws IOException        if there is an issue with the input/output stream
     */
    public static void displayMessage(final int messageSelection, final List<String[]> latestMessages)
            throws MessagingException, IOException {
        System.out.println("------------------------------------------------");
        System.out.println("Message: " + messageSelection);
        System.out.println("Subject: " + latestMessages.get(messageSelection - 1)[1]);
        System.out.println("From: " + latestMessages.get(messageSelection - 1)[2]);
        System.out.println("Date: " + latestMessages.get(messageSelection - 1)[3]);
        System.out.println("Body: " + getTextFromMessage(Mailbox.getMessages()[messageSelection - 1]));
        System.out.println("------------------------------------------------");
    }

    /**
     *
     * This method extracts the text content of an email message. If the message is
     * in plain text format,
     * the method simply returns the content as a string. If the message is in
     * multipart format,
     * the method will extract the text content from the first text/plain or
     * text/html part of the message
     * and return it as a string.
     *
     * @param message the email message to extract the text content from
     * @return the text content of the message as a string
     * @throws MessagingException if an error occurs while accessing the message
     * @throws IOException        if an error occurs while reading the message
     *                            content
     */
    public static String getTextFromMessage(final Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = EmailClient.getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }
}
