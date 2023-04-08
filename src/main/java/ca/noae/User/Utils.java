package ca.noae.User;

import ca.noae.Actions.Mailbox;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.List;

public class Utils {
    public static String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart) throws MessagingException, IOException {
        StringBuilder result = new StringBuilder();
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result.append("\n").append(bodyPart.getContent());
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result.append("\n").append(org.jsoup.Jsoup.parse(html).text());
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
            }
        }
        return result.toString();
    }

    public static void displayMessage(int messageSelection, List<String[]> latestMessages)
            throws MessagingException, IOException {
        System.out.println("------------------------------------------------");
        System.out.println("Message: " + messageSelection);
        System.out.println("Subject: " + latestMessages.get(messageSelection - 1)[1]);
        System.out.println("From: " + latestMessages.get(messageSelection - 1)[2]);
        System.out.println("Date: " + latestMessages.get(messageSelection - 1)[3]);
        System.out.println("Body: " + getTextFromMessage(Mailbox.getMessages()[messageSelection - 1]));
        System.out.println("------------------------------------------------");
    }

    public static String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }
}
