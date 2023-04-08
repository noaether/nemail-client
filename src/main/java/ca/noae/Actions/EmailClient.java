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

  /**
   * Sends an email to the specified recipient with the given subject and body.
   *
   * @param to      the email address of the recipient
   * @param from    the email address of the sender
   * @param subject the subject line of the email
   * @param body    the body of the email
   * @throws MessagingException if there is an error sending the email
   */
  public static void sendEmail(String to, String from, String subject, String body) throws MessagingException {
    Message message = new MimeMessage(Session.getDefaultInstance(new Properties()));
    message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
    message.setFrom(new InternetAddress(from));
    message.setSubject(subject);
    message.setText(body);
    transport.sendMessage(message, message.getAllRecipients());
  }

  /**
   * Retrieves the text content from a MimeMultipart object.
   *
   * @param mimeMultipart the MimeMultipart object to extract text content from
   * @return the text content of the MimeMultipart object
   * @throws MessagingException if there is an error extracting the text content
   * @throws IOException        if there is an error reading the body part
   */
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

  /**
   * Parses a BodyPart object and returns its content as plain text.
   *
   * @param bodyPart the BodyPart object to parse
   * @return the content of the BodyPart object as plain text
   * @throws MessagingException if there is an error parsing the BodyPart
   * @throws IOException        if there is an error reading the content of the
   *                            BodyPart
   */
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
