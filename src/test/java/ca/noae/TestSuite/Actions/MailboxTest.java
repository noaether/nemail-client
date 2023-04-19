package ca.noae.TestSuite.Actions;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import java.util.List;

import javax.mail.Folder;
import javax.mail.FolderNotFoundException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ca.noae.Actions.Mailbox;

public final class MailboxTest {

  /**
   * The mock store object.
   */
  @Mock
  private Store store;

  /**
   * The mock inbox folder object.
   */
  @Mock
  private Folder inbox;

  /**
   * First mock message.
   */
  @Mock
  private Message message1;

  /**
   * Second mock message.
   */
  @Mock
  private Message message2;

  /**
   *
   * This method sets up the mock objects and behavior necessary for testing the
   * {@code Mailbox} class.
   * It initializes the mock {@code Store} object, mocks the behavior of its
   * {@code getFolder()} method to return the
   * {@code inbox} folder, mocks the behavior of the {@code inbox} folder to
   * return a count of 2 messages, mocks the
   * behavior of the {@code getMessages()} method of the {@code inbox} folder to
   * return an array containing {@code message1}
   * and {@code message2}, and mocks the behavior of the {@code getSubject()},
   * {@code getFrom()}, {@code getSentDate()},
   * and {@code getContent()} methods of each message to return the appropriate
   * data. Finally, it calls the
   * {@code getLatestMessages()} method of the {@code Mailbox} class with the mock
   * {@code Store} object to reset the inbox
   * and obtain the latest messages.
   *
   * @throws Exception if there is an error setting up the mock objects and
   *                   behavior
   */
  @BeforeEach
  public void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);

    // set up mocks
    Mockito.when(store.getFolder(Mockito.anyString())).thenReturn(inbox);
    Mockito.when(inbox.exists()).thenReturn(true);
    Mockito.when(inbox.getMessageCount()).thenReturn(2);
    Mockito.when(inbox.getMessages(Mockito.anyInt(), Mockito.anyInt()))
        .thenReturn(new Message[] {
            message1, message2
        });
    Mockito.when(message1.getSubject()).thenReturn("Subject1");
    Mockito.when(message1.getFrom())
        .thenReturn(new javax.mail.Address[] {
            new javax.mail.internet.InternetAddress("sender1@example.com")
        });
    Mockito.when(message1.getSentDate()).thenReturn(new java.util.Date());
    Mockito.when(message1.getContent()).thenReturn("Body1");
    Mockito.when(message2.getSubject()).thenReturn("Subject2");
    Mockito.when(message2.getFrom())
        .thenReturn(new javax.mail.Address[] {
            new javax.mail.internet.InternetAddress("sender2@example.com")
        });
    Mockito.when(message2.getSentDate()).thenReturn(new java.util.Date());
    Mockito.when(message2.getContent()).thenReturn("Body2");

    // reset mailbox
    Mailbox.queryLatest("inbox", store);
  }

  /**
   * Test method for {@link Mailbox#getLatestMessages(String, Store)}.
   *
   * This method tests the retrieval of the latest messages from the mailbox.
   * It retrieves a list of messages using the Mailbox.getLatestMessages() method
   * and checks that the list was retrieved correctly by comparing its size and
   * contents with the expected values.
   *
   * @throws Exception
   */
  @Test
  public void testGetLatestMessages() throws Exception {
    List<String[]> latestMessages = Mailbox.queryLatest("inbox", store);

    // check that messages were retrieved correctly
    assertEquals(2, latestMessages.size());
    assertEquals("Subject1", latestMessages.get(0)[1]);
    assertEquals("Subject2", latestMessages.get(1)[1]);
  }

  /**
   * Test method for {@link Mailbox#getLatestMessages(String, Store)}.
   *
   * This method tests the retrieval of the latest messages from the mailbox when
   * the folder does not exist. It calls the Mailbox.getLatestMessages() method
   * with a folder name that does not exist and checks that the method throws a
   * FolderNotFoundException.
   *
   * @throws Exception
   */
  @Test
  public void testGetLatestMessagesFolderNotFound() throws Exception {
    Mockito.when(inbox.exists()).thenReturn(false);

    assertThrows(FolderNotFoundException.class, () -> Mailbox.queryLatest("inbox", store));
  }

  /**
   * Test method for {@link Mailbox#getLatestMessages(String, Store)}.
   *
   * This method tests the retrieval of the latest messages from the mailbox when
   * the folder is not open. It calls the Mailbox.getLatestMessages() method with
   * a folder name that is not open and checks that the method throws a
   * MessagingException.
   *
   * @throws Exception
   */
  @Test
  public void testGetSubjects() {
    List<String> subjects = Mailbox.getSubjects();

    // check that subjects were retrieved correctly
    assertEquals(2, subjects.size());
    assertEquals("Subject1", subjects.get(0));
    assertEquals("Subject2", subjects.get(1));
  }

  /**
   * Test method for {@link Mailbox#getFroms()}.
   *
   * Retrieves senders from the mailbox and asserts that they were retrieved
   * correctly by checking their addresses.
   *
   * @throws MessagingException if an error occurs while retrieving the senders
   */
  @Test
  public void testGetFroms() {
    List<String> froms = Mailbox.getFroms();

    // check that senders were retrieved correctly
    assertEquals(2, froms.size());
    assertEquals("sender1@example.com", froms.get(0));
    assertEquals("sender2@example.com", froms.get(1));
  }

  /**
   * Test method for {@link Mailbox#getMessages()}.
   *
   * Retrieves messages from the mailbox and asserts that they were retrieved
   * correctly by checking their subjects.
   *
   * @throws MessagingException if an error occurs while retrieving the messages
   */
  @Test
  public void testGetMessages() throws MessagingException {
    Message[] messages = Mailbox.getMessages();

    // check that messages were retrieved correctly
    assertEquals(2, messages.length);
    assertEquals("Subject1", messages[0].getSubject());
    assertEquals("Subject2", messages[1].getSubject());
  }

  /**
   * Test method for {@link Mailbox#setMessages(Message[])}.
   *
   * Sets messages in the mailbox and asserts that they were set correctly by
   * checking their subjects.
   *
   * @throws MessagingException if an error occurs while setting the messages
   */
  @Test
  public void testSetMessagesSuccess() throws Exception {
    Message[] messages = new Message[] {
        Mockito.mock(Message.class),
        Mockito.mock(Message.class)
    };

    // Set the messages
    Message[] result = Mailbox.setMessages(messages);

    // Verify that the messages were set correctly
    assertArrayEquals(messages, result);
    assertArrayEquals(messages, Mailbox.getMessages());
  }

  /**
   * Test method for {@link Mailbox#setMessages(Message[])}.
   *
   * Sets messages in the mailbox to null and asserts that an
   * IllegalArgumentException is thrown.
   *
   * @throws MessagingException if an error occurs while setting the messages
   */
  @Test
  public void testSetMessagesNull() throws Exception {
    try {
      // Try to set null messages
      Mailbox.setMessages(null);
      fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      // Verify that the exception message is correct
      assertEquals("Message cannot be null", e.getMessage());
    }
  }
}
