package ca.noae.Connections;

import java.io.IOException;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.imap.IMAPStore;
import com.sun.mail.pop3.POP3Store;

public class Authentication {
  private static String smtpHost;
  private static String smtpPort;
  private static String pop3Host;
  private static String pop3Port;
  private static String imapHost;
  private static String imapPort;
  private static String emailAddress;
  private static String password;

  public Authentication(String smtpHost, String smtpPort, String pop3Host, String pop3Port, String imapHost,
      String imapPort, String emailAddress, String password) {
    Authentication.smtpHost = smtpHost;
    Authentication.smtpPort = smtpPort;
    Authentication.pop3Host = pop3Host;
    Authentication.pop3Port = pop3Port;
    Authentication.imapHost = imapHost;
    Authentication.imapPort = imapPort;
    Authentication.emailAddress = emailAddress;
    Authentication.password = password;
  }

  // Authenticate the user's credentials against the SMTP server
  public static Transport getSMTPTransport() throws MessagingException {
    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.socketFactory.class",
        "javax.net.ssl.SSLSocketFactory");
    props.put("mail.smtp.host", smtpHost);
    props.put("mail.smtp.port", smtpPort);

    Session session = Session.getInstance(props, new Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(emailAddress, password);
      }
    });

    Transport transport = session.getTransport("smtp");
    transport.connect();
    return transport;
  }

  public static Store getIMAPStore() throws Exception {
    Properties properties = new Properties();
    properties.setProperty("mail.store.protocol", "imap");
    properties.setProperty("mail.imap.host", imapHost);
    properties.setProperty("mail.imap.port", imapPort);
    properties.setProperty("mail.imap.ssl.enable", "true");
    Session session = Session.getInstance(properties);
    Store store = session.getStore("imap");
    store.connect(imapHost, emailAddress, password);
    return store;
  }

  public static Store getPOP3Store() throws Exception {
    Properties properties = new Properties();
    properties.setProperty("mail.store.protocol", "pop3");
    properties.setProperty("mail.pop3.host", pop3Host);
    properties.setProperty("mail.pop3.port", pop3Port);
    properties.setProperty("mail.pop3.ssl.enable", "true");
    Session session = Session.getInstance(properties);
    Store store = session.getStore("pop3");
    store.connect(pop3Host, emailAddress, password);
    return store;
  }

  public static Store getStore() throws Exception { /* TODO : Implement error throwing */
    Store finalStore = null;
    try {
      finalStore = getIMAPStore();
    } catch (Exception noImap) {
      try {
        finalStore = getPOP3Store();
      } catch (Exception e) {
        System.out.println("Unable to connect to server. Please try again later.");
        System.exit(0);
      }
    }
    return finalStore;
  }
}
