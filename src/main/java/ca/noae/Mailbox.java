package ca.noae;

import java.util.ArrayList;
import java.util.List;
import javax.mail.*;
import javax.mail.internet.*;

public class Mailbox {

  private static final int MAX_MESSAGES = 25;

  private static Store store;
  public static Folder inbox;
  public static List<String[]> latestMessages = new ArrayList<>();

  public Mailbox(Store store) {
    Mailbox.store = store;
  }

  public static List<String[]> getLatestMessages(String mailbox)
      throws Exception {

    Boolean exists = store.getFolder(mailbox).exists();
    if (exists == false) {
      throw new FolderNotFoundException(store.getFolder(mailbox), "Mailbox does not exist");
    }

    inbox = store.getFolder(mailbox);
    inbox.open(Folder.READ_ONLY);

    int messageCount = inbox.getMessageCount();
    int start = Math.max(1, messageCount - MAX_MESSAGES + 1);
    Message[] messages = inbox.getMessages(start, messageCount);

    System.out.println("Total Messages:- " + messageCount);
    System.out.println("------------------------------");
    System.out.println("Showing messages " + start + " to " + messageCount);
    for (Message message : messages) {
      latestMessages.add(new String[] {message.getSubject(), message.getFrom()[0].toString(), message.getSentDate().toString(), message.getContent().toString()});
    }

    return latestMessages;
  }

  public static List<String> getSubjects() {
    List<String> subjects = new ArrayList<>();
    for(String[] latestMessage : latestMessages) {
      subjects.add(latestMessage[0]);
    }
    return subjects;
  }

  public static List<String> getFroms() {
    List<String> froms = new ArrayList<>();
    for(String[] latestMessage : latestMessages) {
      froms.add(latestMessage[1]);
    }
    return froms;
  }

  public static List<String> getDates() {
    List<String> dates = new ArrayList<>();
    for(String[] latestMessage : latestMessages) {
      dates.add(latestMessage[2]);
    }
    return dates;
  }

  public static List<String> getBodies() {
    List<String> bodies = new ArrayList<>();
    for(String[] latestMessage : latestMessages) {
      bodies.add(latestMessage[3]);
    }
    return bodies;
  }
}
