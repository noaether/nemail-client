package ca.noae.Actions;

import java.util.ArrayList;
import java.util.List;
import javax.mail.*;

public final class Mailbox {

  private static final int MAX_MESSAGES = 25;

  private static Store store;
  public static Folder inbox;

  public static Message[] messages;
  public static List<String[]> latestMessages = new ArrayList<>();

  public Mailbox(Store store) {
    Mailbox.store = store;
  }

  /**
   * Retrieves the latest messages from the specified mailbox and returns them as
   * a List of String arrays.
   *
   * @param mailbox the name of the mailbox to retrieve messages from
   * @return a List of String arrays containing message information, including
   *         message number, subject, sender, date, and content
   * @throws Exception if the specified mailbox does not exist or there is an
   *                   error while retrieving messages
   */
  public static List<String[]> getLatestMessages(String mailbox)
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
    int messageNo = 1;
    for (Message message : messages) {
      latestMessages.add(new String[] { String.valueOf(messageNo), message.getSubject(),
          message.getFrom()[0].toString(), message.getSentDate().toString(), message.getContent().toString() });
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

  public static List<String> getFroms() {
    List<String> froms = new ArrayList<>();
    for (String[] latestMessage : latestMessages) {
      froms.add(latestMessage[2]);
    }
    return froms;
  }

  /**
   * Retrieves the senders of the latest messages and returns them as a List of
   * Strings.
   *
   * @return a List of Strings containing the senders of the latest messages
   */
  public static List<String> getDates() {
    List<String> dates = new ArrayList<>();
    for (String[] latestMessage : latestMessages) {
      dates.add(latestMessage[3]);
    }
    return dates;
  }

  /**
   * Retrieves the bodies of the latest messages and returns them as a List of
   * Strings.
   *
   * @return a List of Strings containing the bodies of the latest messages
   */
  public static List<String> getBodies() {
    List<String> bodies = new ArrayList<>();
    for (String[] latestMessage : latestMessages) {
      bodies.add(latestMessage[4]);
    }
    return bodies;
  }
}
