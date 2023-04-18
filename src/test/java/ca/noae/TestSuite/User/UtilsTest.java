package ca.noae.TestSuite.User;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import ca.noae.Actions.Mailbox;
import ca.noae.User.Utils;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class UtilsTest {

    private MimeMultipart mimeMultipart;

    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;

    @BeforeAll
    public static void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterAll
    public static void restoreStreams() {
        System.setOut(originalOut);
    }

    @BeforeEach
    public void setup() throws MessagingException, IOException {
        mimeMultipart = Mockito.mock(MimeMultipart.class);
        BodyPart bodyPart = Mockito.mock(BodyPart.class);
        Mockito.when(mimeMultipart.getCount()).thenReturn(2);
        Mockito.when(mimeMultipart.getBodyPart(0)).thenReturn(bodyPart);
        Mockito.when(mimeMultipart.getBodyPart(1)).thenReturn(bodyPart);
        Mockito.when(bodyPart.isMimeType("text/plain")).thenReturn(true);
        Mockito.when(bodyPart.getContent()).thenReturn("test plain text");
    }

    @Test
    public void testGetTextFromMimeMultipart() throws MessagingException, IOException {
        String result = Utils.getTextFromMimeMultipart(mimeMultipart);
        Assertions.assertEquals("\n" + "test plain text", result);
    }

    @Test
    public void testDisplayMessage() throws Exception {
        // Create mock message object
        Message message = mock(Message.class);
        when(message.isMimeType("text/plain")).thenReturn(true);
        when(message.getContent()).thenReturn("Hello World!");

        // Create mock latestMessages list
        List<String[]> latestMessages = new ArrayList<>();
        String[] message1 = {"1", "Test Subject", "test@test.com", "2023-04-18"};
        latestMessages.add(message1);

        // Set the mailbox to the mock message
        Mailbox.setMessages(new Message[]{message});

        // Call the method under test
        Utils.displayMessage(1, latestMessages);

        // Verify output
        assertEquals(
                "------------------------------------------------\n" +
                "Message: 1\n" +
                "Subject: Test Subject\n" +
                "From: test@test.com\n" +
                "Date: 2023-04-18\n" +
                "Body: Hello World!\n" +
                "------------------------------------------------\n", outContent.toString());
    }

    @Test
    public void testGetTextFromMessage() throws MessagingException, IOException {
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.isMimeType("text/plain")).thenReturn(true);
        Mockito.when(message.getContent()).thenReturn("test plain text");
        String result = Utils.getTextFromMessage(message);
        Assertions.assertEquals("test plain text", result);
    }
}
