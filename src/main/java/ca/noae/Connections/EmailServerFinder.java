package ca.noae.Connections;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public final class EmailServerFinder {
  /**
   *
   * This is a utility class containing only static methods and cannot be
   * instantiated.
   */
  private EmailServerFinder() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  /**
   *
   * A mapping of email provider names to arrays of their associated email server
   * hosts and ports.
   */
  private static final Map<String, String[]> EMAIL_PROVIDERS = new HashMap<>();

  static {
    EMAIL_PROVIDERS.put(
        "gmail.com", new String[] {
            "smtp.gmail.com", "imap.gmail.com", "pop.gmail.com"
        });
    EMAIL_PROVIDERS.put("outlook.com",
        new String[] {
            "smtp-mail.outlook.com", "outlook.office365.com", "pop-mail.outlook.com"
        });
    EMAIL_PROVIDERS.put("yahoo.com",
        new String[] {
            "smtp.mail.yahoo.com", "imap.mail.yahoo.com", "pop.mail.yahoo.com"
        });
    EMAIL_PROVIDERS.put("hotmail.com",
        new String[] {
            "smtp-mail.outlook.com", "outlook.office365.com", "pop-mail.outlook.com"
        });
    EMAIL_PROVIDERS.put(
        "noae.ca", new String[] {
            "smtp.migadu.com", "imap.migadu.com", "pop.migadu.com"
        });
  }

  /** The SMTP ports to check. */
  private static final String[] SMTP_PORTS = {
      "25", "465", "587" };

  /** The IMAP ports to check. */
  private static final String[] IMAP_PORTS = {
      "143", "993" };

  /** The POP3 ports to check. */
  private static final String[] POP3_PORTS = {
      "110", "995" };

  /**
   *
   * Checks the email server connectivity and identifies the appropriate SMTP and
   * IMAP servers for a given email address.
   *
   * @param email the email address to check the servers for.
   * @return an array of string containing the SMTP server name, IMAP server name
   *         and POP3 server name (in this order).
   * @throws UnknownHostException     if the SMTP or IMAP/POP3 servers are not
   *                                  found for the given email address.
   * @throws IllegalArgumentException if the email address is invalid.
   */
  public static String[] check(final String email) throws UnknownHostException {
    String[] respStrings = new String[3];
    String[] parts = email.split("@");

    if (parts.length != 2) {
      throw new IllegalArgumentException("Invalid email address.");
    }

    String domain = parts[1].toLowerCase();
    String[] servers = EMAIL_PROVIDERS.get(domain);

    if (servers != null) {
      String smtpServer = servers[0];
      String imapServer = servers[1];
      String pop3Server = servers[2];

      System.out.println("SMTP server: " + smtpServer);
      System.out.println("IMAP server: " + imapServer);
      System.out.println("POP3 server: " + pop3Server);

      respStrings[0] = smtpServer;
      respStrings[1] = imapServer;
      respStrings[2] = pop3Server;
      return respStrings;
    }

    // Guess hostnames
    String[] possibleSMTPHosts = {
        "smtp." + domain, "mail." + domain };
    String[] possibleIMAPHosts = {
        "imap." + domain, "mail." + domain, "pop." + domain, "pop3." + domain };
    String[] possiblePOP3Hosts = {
        "pop." + domain, "pop3." + domain, "imap." + domain, "mail." + domain };

    // Probe known ports
    String smtpServer = probePorts(possibleSMTPHosts, SMTP_PORTS);
    String imapServer = probePorts(possibleIMAPHosts, IMAP_PORTS);
    String pop3Server = probePorts(possiblePOP3Hosts, POP3_PORTS);

    // Try to open connections and check capabilities
    if (smtpServer == null) {
      smtpServer = probeCapabilities(possibleSMTPHosts, SMTP_PORTS, "SMTP");
    }

    if (imapServer == null) {
      imapServer = probeCapabilities(possibleIMAPHosts, IMAP_PORTS, "IMAP");
    }

    if (pop3Server == null) {
      pop3Server = probeCapabilities(possiblePOP3Hosts, POP3_PORTS, "POP3");
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
      respStrings[2] = imapServer;
      return respStrings;
    }

    if (pop3Server != null) {
      System.out.println("POP3 server: " + pop3Server);
      respStrings[1] = pop3Server;
      respStrings[2] = pop3Server;
      return respStrings;
    }

    throw new UnknownHostException("IMAP and POP3 servers not found.");
  }

  /**
   *
   * Probes a list of possible hosts and ports to check if any of them are
   * reachable.
   *
   * @param possibleHosts an array of possible hostnames
   * @param ports         an array of port numbers to check
   * @return the first reachable hostname or null if none are reachable
   */
  public static String probePorts(final String[] possibleHosts, final String[] ports) {
    for (String host : possibleHosts) {
      for (String port : ports) {
        try {
          Socket socket = new Socket();
          socket.connect(
              new InetSocketAddress(host, Integer.parseInt(port)), 1000);
          socket.close();
          return host;
        } catch (IOException ignored) {
        }
      }
    }

    return null;
  }

  /**
   *
   * Tries to connect to the specified hosts and ports using the given protocol,
   * and checks their
   * capabilities by sending a command and analyzing the response. Returns the
   * first host that responds
   * positively or null if none do.
   *
   * @param possibleHosts an array of possible hostnames to connect to
   * @param ports         an array of ports to try
   * @param protocol      the protocol to probe, e.g. "SMTP" or "IMAP"
   * @return the hostname of the first server that responds positively or null if
   *         none do
   */
  public static String probeCapabilities(
      final String[] possibleHosts, final String[] ports, final String protocol) {
    for (String host : possibleHosts) {
      for (String port : ports) {
        try {
          Socket socket = new Socket();
          socket.connect(
              new InetSocketAddress(host, Integer.parseInt(port)), 1000);
          String command = getCommand(protocol);
          if (command != null) {
            socket.getOutputStream().write(command.getBytes());
            byte[] buffer = new byte[1048];
            int length = socket.getInputStream().read(buffer);
            String response = new String(buffer, 0, length);
            socket.close();
            if (checkResponse(protocol, response)) {
              return host;
            }
          }
        } catch (IOException ignored) {
        }
      }
    }
    return null;
  }

  /**
   *
   * Returns the command to be sent over the socket to check for protocol
   * capabilities.
   *
   * @param protocol the protocol to check capabilities for (IMAP, SMTP, or POP3)
   * @return the command to be sent over the socket, or null if protocol is
   *         invalid
   */
  private static String getCommand(final String protocol) {
    if (protocol.equals("IMAP")) {
      return "a001 CAPABILITY\r\n";
    } else if (protocol.equals("SMTP")) {
      return "EHLO localhost\r\n";
    } else if (protocol.equals("POP3")) {
      return "CAPA\r\n";
    }
    return null;
  }

  /**
   *
   * Checks if the response received from the server contains the expected
   * capabilities for the given protocol.
   *
   * @param protocol a string indicating the protocol being checked (IMAP, SMTP,
   *                 POP3)
   * @param response the response string received from the server
   * @return {@code true} if the response contains the expected capabilities for
   *         the given protocol, {@code false} otherwise
   */
  private static boolean checkResponse(final String protocol, final String response) {
    if (protocol.equals("IMAP")) {
      return response.contains("IMAP4rev1") || response.contains("IMAP4")
          || response.contains("IMAP") || response.contains("OK");
    } else if (protocol.equals("SMTP")) {
      return response.contains("250-STARTTLS")
          || response.contains("250 STARTTLS") || response.contains("220")
          || response.contains("ESMTP MAIL");
    } else if (protocol.equals("POP3")) {
      return response.contains("STLS") || response.contains("OK");
    }
    return false;
  }
}
