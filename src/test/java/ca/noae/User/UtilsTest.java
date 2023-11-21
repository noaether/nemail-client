package ca.noae.User;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import ca.noae.Actions.Mailbox;

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

    /**
     * The MimeMultipart object to be used in the tests.
     */
    private MimeMultipart mimeMultipart;

    /**
     * The ByteArrayOutputStream object to be used in the tests.
     */
    private static ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    /**
     * The PrintStream object to be used in the tests.
     */
    private static PrintStream originalOut = System.out;

    /**
     * Sets up the output stream.
     */
    @BeforeAll
    public static void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    /**
     * Restores the output stream.
     */
    @AfterAll
    public static void restoreStreams() {
        System.setOut(originalOut);
    }

    /**
     * Sets up the MimeMultipart object.
     *
     * @throws MessagingException
     * @throws IOException
     */
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

    /**
     * Tests the {@link Utils#displayMessage} method with
     * a Message object with a plain text body.
     */
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

        if (System.getProperty("os.name").contains("Windows")) {
        assertEquals(
                "------------------------------------------------\r\n"
                + "Message: 1\r\n"
                + "Subject: Test Subject\r\n"
                + "From: test@test.com\r\n"
                + "Date: 2023-04-18\r\n"
                + "Body: Hello World!\r\n"
                + "------------------------------------------------\r\n", outContent.toString());
        } else {
        assertEquals(
                "------------------------------------------------\n"
                + "Message: 1\n"
                + "Subject: Test Subject\n"
                + "From: test@test.com\n"
                + "Date: 2023-04-18\n"
                + "Body: Hello World!\n"
                + "------------------------------------------------\n", outContent.toString());
        }
    }

    /**
     * Tests the {@link Utils#getTextFromMessage(Message)} method with a Message
     * object with a plain text body.
     *
     * @throws MessagingException if there is an error with the mocked Message object
     * @throws IOException        if there is an error with the mocked Message object
     */
    @Test
    public void testGetTextFromMessage() throws MessagingException, IOException {
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.isMimeType("text/plain")).thenReturn(true);
        Mockito.when(message.getContent()).thenReturn("test plain text");
        String result = Utils.getTextFromMessage(message);
        Assertions.assertEquals("test plain text", result);
    }
}
