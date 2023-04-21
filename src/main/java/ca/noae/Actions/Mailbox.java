package ca.noae.Actions;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Folder;
import javax.mail.FolderNotFoundException;
import javax.mail.Message;
import javax.mail.Store;

import ca.noae.Objects.CodeElements.Generated;

public final class Mailbox {
  /**
   *
   * This is a utility class containing only static methods and cannot be
   * instantiated.
   */
  @Generated({"Utility class cannot be instantiated"})
  private Mailbox() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  /**
   * The maximum number of messages to retrieve from the mail server.
   */
  private static final int MAX_MESSAGES = 25;

  /**
   *
   * The inbox folder for the currently authenticated email account.
   */
  private static Folder inbox;

  /**
   * The messages retrieved from the mail server.
   */
  private static Message[] messages;

  /**
   * The latest messages retrieved from the mail server.
   */
  private static List<String[]> latestMessages = new ArrayList<>();

  /**
   * Retrieves the latest messages from the specified mailbox and returns them as
   * a List of String arrays.
   *
   * @param mailbox the name of the mailbox to retrieve messages from
   * @param store   the Store object to use to retrieve messages
   * @return a List of String arrays containing message information, including
   *         message number, subject, sender, date, and content
   * @throws Exception if the specified mailbox does not exist or there is an
   *                   error while retrieving messages
   */
  public static List<String[]> queryLatest(final String mailbox, final Store store)
      throws Exception {

    boolean exists = store.getFolder(mailbox).exists();
    if (!exists) {
      throw new FolderNotFoundException(store.getFolder(mailbox), "Mailbox does not exist");
    }

    inbox = store.getFolder(mailbox);
    inbox.open(Folder.READ_ONLY);

    int messageCount = inbox.getMessageCount();
    int start = Math.max(1, messageCount - MAX_MESSAGES + 1);
    messages = inbox.getMessages(start, messageCount);

    System.out.println("Total Messages: " + messageCount);
    System.out.println("------------------------------");
    System.out.println("Showing messages " + start + " to " + messageCount);
    latestMessages.clear(); // Clears array to avoid duplicates
    int messageNo = 1;
    for (Message message : messages) {
      latestMessages.add(new String[] {
          String.valueOf(messageNo), message.getSubject(),
          message.getFrom()[0].toString(), message.getSentDate().toString(), message.getContent().toString()
      });
      messageNo++;
    }

    return latestMessages;
  }

  /**
   * Retrieves the subjects of the latest messages and returns them as a List of
   * Strings.
   *
   * @return a List of Strings containing the subjects of the latest messages
   */
  public static List<String> getSubjects() {
    List<String> subjects = new ArrayList<>();
    for (String[] latestMessage : latestMessages) {
      subjects.add(latestMessage[1]);
    }
    return subjects;
  }

  /**
   * Retrieves the senders of the latest messages and returns them as a List of
   * Strings.
   *
   * @return a List of Strings containing the senders of the latest messages
   */
  public static List<String> getFroms() {
    List<String> froms = new ArrayList<>();
    for (String[] latestMessage : latestMessages) {
      froms.add(latestMessage[2]);
    }
    return froms;
  }

  /*
   * public static List<String> getDates() {
   * List<String> dates = new ArrayList<>();
   * for (String[] latestMessage : latestMessages) {
   * dates.add(latestMessage[3]); // 3 is the index of the date in the String[]
   * }
   * return dates;
   * }
   */

  /*
   * public static List<String> getBodies() {
   * List<String> bodies = new ArrayList<>();
   * for (String[] latestMessage : latestMessages) {
   * bodies.add(latestMessage[4]);
   * }
   * return bodies;
   * }
   */

  /**
   * Returns the messages retrieved from the mail server.
   *
   * @return the messages retrieved from the mail server
   */
  public static Message[] getMessages() {
    return messages;
  }

  /**
   * Sets the messages "retrieved" from the mail server.
   *
   * @param newMessage the messages to set
   * @return the message added if successful
   * @throws IllegalArgumentException if the message is null
   */
  public static Message[] setMessages(final Message[] newMessage) throws IllegalArgumentException {
    if (newMessage == null) {
      throw new IllegalArgumentException("Message cannot be null");
    }
    Mailbox.messages = newMessage;
    return messages;
  }

  /**
   * Returns the List of latest messages retrieved from the mail server.
   *
   * @return the List of latest messages retrieved from the mail server
   */
  public static List<String[]> getLatestMessages() {
    return latestMessages;
  }
}
