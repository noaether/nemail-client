package ca.noae.Connections;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;

import ca.noae.Objects.UserInfo;

public final class Authentication {
  /**
   *
   * This is a utility class containing only static methods and cannot be
   * instantiated.
   */
  private Authentication() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  /** The host name of the SMTP server. */
  public static String smtpHost;

  /** The port of the SMTP server. */
  public static String smtpPort;

  /** The host name of the POP3 server. */
  public static String pop3Host;

  /** The port of the POP3 server. */
  public static String pop3Port;

  /** The host name of the IMAP server. */
  public static String imapHost;

  /** The port of the IMAP server. */
  public static String imapPort;

  /** The email address of the user. */
  public static String emailAddress;

  /** The password of the user. */
  public static String password;

  /**
   *
   * Initializes the authentication parameters required for sending and receiving
   * emails.
   *
   * @param user the UserInfo object containing user's email settings
   */
  public static void init(final UserInfo user) {
    Authentication.smtpHost = user.getSmtpServerAddress();
    Authentication.smtpPort = user.getSmtpServerPort();
    Authentication.pop3Host = user.getPopServerAddress();
    Authentication.pop3Port = user.getPopServerPort();
    Authentication.imapHost = user.getImapServerAddress();
    Authentication.imapPort = user.getImapServerPort();
    Authentication.emailAddress = user.getEmailAddress();
    Authentication.password = user.getPassword();
  }

  /**
   * Returns a Transport object for sending email messages using SMTP.
   *
   * @return a Transport object configured to use SMTP for sending email messages
   * @throws MessagingException if there is an error configuring or connecting the
   *                            Transport
   */
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

  /**
   * Returns a configured IMAP store for connecting to an email server using IMAP
   * protocol.
   * This method sets properties for the IMAP host, port, and SSL enablement, and
   * uses the
   * provided email address and password for authentication.
   *
   * @return a configured IMAP Store object for connecting to an email server
   *         using IMAP protocol
   * @throws Exception if an error occurs while connecting to the email server
   */
  public static Store getIMAPStore() throws Exception {
    Properties properties = new Properties();
    properties.setProperty("mail.store.protocol", "imap");
    properties.setProperty("mail.imap.host", imapHost);
    properties.setProperty("mail.imap.port", imapPort);
    try {
      properties.setProperty("mail.imap.ssl.enable", "true");
      Session session = Session.getInstance(properties);
      Store store = session.getStore("imap");
      store.connect(imapHost, emailAddress, password);

      return store.isConnected() ? store : null;
    } catch(MessagingException e) {
      properties.setProperty("mail.imap.ssl.enable", "false");
      Session session = Session.getInstance(properties);
      Store store = session.getStore("imap");
      store.connect(imapHost, emailAddress, password);
      return store;
    }
  }

  /**
   *
   * Returns a Store object configured to connect to a POP3 server using the
   * provided parameters.
   *
   * @return the configured Store object
   * @throws Exception if an error occurs while connecting to the server
   */
  public static Store getPOP3Store() throws Exception {
    Properties properties = new Properties();
    properties.setProperty("mail.store.protocol", "pop3");
    properties.setProperty("mail.pop3.host", pop3Host);
    properties.setProperty("mail.pop3.port", pop3Port);
    try {
      properties.setProperty("mail.pop3.ssl.enable", "true");
      Session session = Session.getInstance(properties);
      Store store = session.getStore("pop3");
      store.connect(pop3Host, emailAddress, password);
      return store;
    } catch (MessagingException e) {
      properties.setProperty("mail.pop3.ssl.enable", "false");
      Session session = Session.getInstance(properties);
      Store store = session.getStore("pop3");
      store.connect(pop3Host, emailAddress, password);
      return store;    }
  }

  /**
   * Returns a Store instance for the email account using either IMAP or POP3
   * protocols.
   *
   * @return a Store instance for the email account.
   * @throws MessagingException if unable to connect to the email server using
   *                            either protocol.
   */
  public static Store getStore() throws Exception {
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
