package ca.noae.Actions;

import javax.mail.*;
import javax.mail.internet.*;

import java.io.IOException;
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

  private String getTextFromMessage(Message message) throws MessagingException, IOException {
    if (message.isMimeType("text/plain")) {
      return message.getContent().toString();
    }
    if (message.isMimeType("multipart/*")) {
      MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
      return getTextFromMimeMultipart(mimeMultipart);
    }
    return "";
  }

  private String getTextFromMimeMultipart(
      MimeMultipart mimeMultipart) throws MessagingException, IOException {
    String result = "";
    for (int i = 0; i < mimeMultipart.getCount(); i++) {
      BodyPart bodyPart = mimeMultipart.getBodyPart(i);
      if (bodyPart.isMimeType("text/plain")) {
        return result + "\n" + bodyPart.getContent(); // without return, same text appears twice in my tests
      }
      result += this.parseBodyPart(bodyPart);
    }
    return result;
  }

  private String parseBodyPart(BodyPart bodyPart) throws MessagingException, IOException {
    if (bodyPart.isMimeType("text/html")) {
      return "\n" + org.jsoup.Jsoup
          .parse(bodyPart.getContent().toString())
          .text();
    } else if (bodyPart.getContent() instanceof MimeMultipart) {
      return getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
    } else {
      return bodyPart.toString();
    }
  };
}
