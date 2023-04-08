/**
 * This class provides methods for sending and receiving email messages.
*/
package ca.noae.Actions;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import java.io.IOException;
import java.util.Properties;

public class EmailClient {

  /**
   * The SMTP transport used to send email messages. This field is initialized
   * when the user logs in to their email account using SMTP authentication.
   * Once the transport is obtained, it can be reused to send multiple messages
   * without requiring authentication each time.
   */
  private static Transport transport;

  /**
   * Initializes the EmailClient class with the specified transport.
   *
   * @param initTransport the SMTP transport to use for sending email messages
   */
  public EmailClient(final Transport initTransport) {
    EmailClient.transport = initTransport;
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
  public static void sendEmail(final String to, final String from, final String subject, final String body)
      throws MessagingException {
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
      final MimeMultipart mimeMultipart) throws MessagingException, IOException {
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
  private String parseBodyPart(final BodyPart bodyPart) throws MessagingException, IOException {
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
