package ca.noae.Actions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class FileHandlerTest {

  /**
   * The name of the test file.
   */
  private static final String TEST_FILE_NAME = "mail.eml";

  /**
   * The test message.
   */
  private Message testMessage;

  /**
   *
   * The setup method initializes the testMessage variable with a new MimeMessage
   * object containing
   * a test email message.
   *
   * @throws MessagingException if an error occurs while creating the MimeMessage
   *                            object.
   */
  @BeforeEach
  public void setup() throws MessagingException {
    Properties props = new Properties();
    testMessage = new MimeMessage(Session.getDefaultInstance(props));
    testMessage.setText("This is a test email message");
  }

  /**
   *
   * The testSaveEmail method tests the saveEmail method of the FileHandler class
   * by verifying
   * that the email is correctly saved to a file and that the file content
   * contains the email message.
   *
   * @throws IOException if an error occurs while reading from the file.
   */
  @Test
  public void testSaveEmail() throws IOException {
    FileHandler.saveEmail(testMessage);
    File testFile = new File(TEST_FILE_NAME);
    Assertions.assertTrue(testFile.exists());
    FileInputStream inputStream = new FileInputStream(TEST_FILE_NAME);
    byte[] buffer = new byte[(int) testFile.length()];
    inputStream.read(buffer);
    String fileContent = new String(buffer);
    Assertions.assertTrue(fileContent.contains("This is a test email message"));
    inputStream.close();
    testFile.delete();
  }

  /**
   *
   * The testSaveEmailWithNullMessage method tests that a NullPointerException is
   * thrown when attempting
   * to save a null email message.
   */
  @Test
  public void testSaveEmailWithNullMessage() {
    Assertions.assertThrows(NullPointerException.class, () -> {
      FileHandler.saveEmail(null);
    });
  }

}
