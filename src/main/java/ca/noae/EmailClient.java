package ca.noae;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

public class EmailClient {

    private static Transport transport;

    public EmailClient(Transport transport) {
      EmailClient.transport = transport;
    }

    public static void sendEmail(String to, String from, String subject, String body) throws MessagingException {
        Message message = new MimeMessage(Session.getDefaultInstance(new Properties()));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setFrom(new InternetAddress(from));
        message.setSubject(subject);
        message.setText(body);
        transport.sendMessage(message, message.getAllRecipients());
    }
}

