package ca.noae.Actions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
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

  private String subject = "Test Subject";
  private String body = "This is a test email message";
  private Date date = new Date("2020/01/01");


  /**
   * The name of the test file.
   */
  private String TEST_FILE_NAME;
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
    testMessage.addFrom(new javax.mail.Address[] { new javax.mail.internet.InternetAddress("john@foo.bar") });
    testMessage.setSubject(subject);
    testMessage.setSentDate(date);

    String[] info = {testMessage.getSubject(), testMessage.getFrom()[0].toString(), testMessage.getSentDate().toString()};
    TEST_FILE_NAME = new StringBuilder().append(info[0]).append(" ").append(info[1]).append(" ").append(info[2]).append(".eml").toString();
  }

  /**
   *
   * The testSaveEmail method tests the saveEmail method of the FileHandler class
   * by verifying
   * that the email is correctly saved to a file and that the file content
   * contains the email message.
   *
   * @throws IOException if an error occurs while reading from the file.
   * @throws MessagingException
   */
  @Test
  public void testSaveEmail() throws IOException, MessagingException {
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
