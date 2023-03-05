package ca.noae;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class EmailServerFinder {
  private static final Map<String, String[]> EMAIL_PROVIDERS = new HashMap<>();

  static {
    EMAIL_PROVIDERS.put("gmail.com", new String[] { "smtp.gmail.com", "imap.gmail.com" });
    EMAIL_PROVIDERS.put("outlook.com", new String[] { "smtp-mail.outlook.com", "imap-mail.outlook.com" });
    EMAIL_PROVIDERS.put("yahoo.com", new String[] { "smtp.mail.yahoo.com", "imap.mail.yahoo.com" });
    EMAIL_PROVIDERS.put("hotmail.com", new String[] { "smtp.live.com", "imap-mail.outlook.com" });
    EMAIL_PROVIDERS.put("noae.ca", new String[] { "smtp.migadu.com", "imap.migadu.com" });
  }

  private static final String[] SMTP_PORTS = { "25", "465", "587" };
  private static final String[] IMAP_PORTS = { "143", "993" };
  private static final String[] POP3_PORTS = { "110", "995" };

  public static String[] check(String email) throws UnknownHostException {
    String[] respStrings = new String[2];
    String[] parts = email.split("@");

    if (parts.length != 2) {
      throw new IllegalArgumentException("Invalid email address.");
    }

    String domain = parts[1].toLowerCase();
    String[] servers = EMAIL_PROVIDERS.get(domain);

    if (servers != null) {
      String smtpServer = servers[0];
      String imapServer = servers[1];

      System.out.println("SMTP server: " + smtpServer);
      System.out.println("IMAP server: " + imapServer);
      respStrings[0] = smtpServer;
      respStrings[1] = imapServer;
      return respStrings;
    }

    // Guess hostnames
    String[] possibleSMTPHosts = { "smtp." + domain, "mail." + domain };
    String[] possibleIMAPHosts = { "imap." + domain, "mail." + domain, "pop." + domain, "pop3." + domain };

    // Probe known ports
    String smtpServer = probePorts(possibleSMTPHosts, SMTP_PORTS);
    String imapServer = probePorts(possibleIMAPHosts, IMAP_PORTS);
    String pop3Server = probePorts(possibleIMAPHosts, POP3_PORTS);

    // Try to open connections and check capabilities
    if (smtpServer == null) {
      smtpServer = probeCapabilities(possibleSMTPHosts, SMTP_PORTS, "SMTP");
    }

    if (imapServer == null) {
      imapServer = probeCapabilities(possibleIMAPHosts, IMAP_PORTS, "IMAP");
    }

    if (pop3Server == null) {
      pop3Server = probeCapabilities(possibleIMAPHosts, POP3_PORTS, "POP3");
    }

    if (smtpServer != null) {
      System.out.println("SMTP server: " + smtpServer);
      respStrings[0] = smtpServer;
    } else {
      throw new UnknownHostException("SMTP server not found.");
    }

    if (imapServer != null) {
      System.out.println("IMAP server: " + imapServer);
      respStrings[1] = imapServer;
      return respStrings;
    }

    if (pop3Server != null) {
      System.out.println("POP3 server: " + pop3Server);
      respStrings[1] = pop3Server;
      return respStrings;
    }

    if(imapServer == null && pop3Server == null){
      throw new UnknownHostException("IMAP and POP3 servers not found.");
    }

    return respStrings;
  }

  private static String probePorts(String[] possibleHosts, String[] ports) {
    for (String host : possibleHosts) {
      for (String port : ports) {
        try {
          Socket socket = new Socket();
          socket.connect(new InetSocketAddress(host, Integer.parseInt(port)), 1000);
          socket.close();
          return host;
        } catch (IOException ignored) {
        }
      }
    }

    return null;
  }

  private static String probeCapabilities(String[] possibleHosts, String[] ports, String protocol) {
    for (String host : possibleHosts) {
      for (String port : ports) {
        try {
          Socket socket = new Socket();
          socket.connect(new InetSocketAddress(host, Integer.parseInt(port)), 1000);

          if (protocol.equals("IMAP")) {
            socket.getOutputStream().write("a001 CAPABILITY\r\n".getBytes());
          } else if (protocol.equals("SMTP")) {
            socket.getOutputStream().write("EHLO localhost\r\n".getBytes());
          } else if (protocol.equals("POP3")) {
            socket.getOutputStream().write("CAPA\r\n".getBytes());
          }

          byte[] buffer = new byte[1024];
          int length = socket.getInputStream().read(buffer);
          String response = new String(buffer, 0, length);

          if (response.contains("STARTTLS") || response.contains("AUTH")) {
            socket.close();
            return host;
          }

          socket.close();
        } catch (IOException ignored) {
        }
      }
    }

    return null;
  }

}
