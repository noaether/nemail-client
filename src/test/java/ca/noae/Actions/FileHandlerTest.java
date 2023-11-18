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
import java.util.Date;
import java.util.Properties;

public final class FileHandlerTest {

  /**
   * The test message.
   */
  private Message testMessage;

  /**
   * The subject of the test message.
   */
  private String subject = "Test Subject";

  /**
   * The body of the test message.
   */
  private String body = "This is a test email message";

  /**
   * The date of the test message.
   * Set date to 27-08-2021 9:54:00 AM
   */
  private Date date = new Date(1630072440000L);

  /**
   * The name of the test file.
   */
  private String testFileName;

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
    testMessage.setText(body);
    testMessage.addFrom(new javax.mail.Address[] {
      new javax.mail.internet.InternetAddress("john@foo.bar")
    });
    testMessage.setSubject(subject);
    testMessage.setSentDate(date);

    testFileName = FileHandler.getFileName(testMessage);
  }

  /**
   *
   * The testSaveEmail method tests the saveEmail method of the FileHandler class
   * by verifying
   * that the email is correctly saved to a file and that the file content
   * contains the email message.
   *
   * @throws IOException        if an error occurs while reading from the file.
   * @throws MessagingException
   */
  @Test
  public void testSaveEmail() throws IOException, MessagingException {
    FileHandler.saveEmail(testMessage);
    File testFile = new File(testFileName);
    Assertions.assertTrue(testFile.exists());
    FileInputStream inputStream = new FileInputStream(testFileName);
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

  /**
   * The testDeleteFile method tests the deleteFile method of FileHandler.
   */
  @Test
  void testDeleteFile() {
    // Create a temporary file for testing
    File tempFile = null;
    try {
      tempFile = File.createTempFile("test", ".txt");
    } catch (Exception e) {
      Assertions.fail("Failed to create a temporary file for testing");
    }

    // Delete the file using the deleteFile() method
    String deletedFileName = FileHandler.deleteFile(tempFile.getAbsolutePath());

    // Verify that the file was deleted
    Assertions.assertFalse(tempFile.exists());
    Assertions.assertEquals(tempFile.getAbsolutePath(), deletedFileName);
  }

  /**
   * The testDeleteFile method tests the deleteFile method of FileHandler
   * with a null file name and a file that doesn't exist.
   */
  @Test
  void testDeleteFilenull() {
    // Create a temporary file for testing
    File tempFile = null;
    try {
      tempFile = File.createTempFile("test", ".txt");
    } catch (Exception e) {
      Assertions.fail("Failed to create a temporary file for testing");
    }

    Assertions.assertThrows(RuntimeException.class, () -> {
      FileHandler.deleteFile("notanactualfile.txt");
    });

    // Verify that the file was deleted
    Assertions.assertTrue(tempFile.exists());
  }

}
