package ca.noae.TestSuite.Actions;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

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

public class MailboxTest {

  @Mock
  private Store store;

  @Mock
  private Folder inbox;

  @Mock
  private Message message1;

  @Mock
  private Message message2;

  private int multiplier = 1;

  @BeforeEach
  public void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);

    // set up mocks
    Mockito.when(store.getFolder(Mockito.anyString())).thenReturn(inbox);
    Mockito.when(inbox.exists()).thenReturn(true);
    Mockito.when(inbox.getMessageCount()).thenReturn(2);
    Mockito.when(inbox.getMessages(Mockito.anyInt(), Mockito.anyInt()))
        .thenReturn(new Message[] { message1, message2 });
    Mockito.when(message1.getSubject()).thenReturn("Subject1");
    Mockito.when(message1.getFrom())
        .thenReturn(new javax.mail.Address[] { new javax.mail.internet.InternetAddress("sender1@example.com") });
    Mockito.when(message1.getSentDate()).thenReturn(new java.util.Date());
    Mockito.when(message1.getContent()).thenReturn("Body1");
    Mockito.when(message2.getSubject()).thenReturn("Subject2");
    Mockito.when(message2.getFrom())
        .thenReturn(new javax.mail.Address[] { new javax.mail.internet.InternetAddress("sender2@example.com") });
    Mockito.when(message2.getSentDate()).thenReturn(new java.util.Date());
    Mockito.when(message2.getContent()).thenReturn("Body2");

    // reset mailbox
    Mailbox.getLatestMessages("inbox", store);
  }

  @Test
  public void testGetLatestMessages() throws Exception {
    List<String[]> latestMessages = Mailbox.getLatestMessages("inbox", store);

    // check that messages were retrieved correctly
    assertEquals(2, latestMessages.size());
    assertEquals("Subject1", latestMessages.get(0)[1]);
    assertEquals("Subject2", latestMessages.get(1)[1]);
  }

  @Test
  public void testGetLatestMessagesFolderNotFound() throws Exception {
    Mockito.when(inbox.exists()).thenReturn(false);

    assertThrows(FolderNotFoundException.class, () -> Mailbox.getLatestMessages("inbox", store));
  }

  @Test
  public void testGetSubjects() {
    List<String> subjects = Mailbox.getSubjects();

    // check that subjects were retrieved correctly
    assertEquals(2 * multiplier, subjects.size());
    assertEquals("Subject1", subjects.get(0));
    assertEquals("Subject2", subjects.get(1));
  }

  @Test
  public void testGetFroms() {
    List<String> froms = Mailbox.getFroms();

    // check that senders were retrieved correctly
    assertEquals(2, froms.size());
    assertEquals("sender1@example.com", froms.get(0));
    assertEquals("sender2@example.com", froms.get(1));
  }

  @Test
  public void testGetMessages() throws MessagingException {
    Message[] messages = Mailbox.getMessages();

    // check that messages were retrieved correctly
    assertEquals(2, messages.length);
    assertEquals("Subject1", messages[0].getSubject());
    assertEquals("Subject2", messages[1].getSubject());
  }

}
