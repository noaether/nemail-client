package ca.noae.TestSuite.Actions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.noae.Actions.FileHandler;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class FileHandlerTest {

  private static final String TEST_FILE_NAME = "mail.eml";

  private Message testMessage;

  @BeforeEach
  public void setup() throws MessagingException {
    Properties props = new Properties();
    testMessage = new MimeMessage(Session.getDefaultInstance(props));
    testMessage.setText("This is a test email message");
  }

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

  @Test
  public void testSaveEmailWithNullMessage() {
    Assertions.assertThrows(NullPointerException.class, () -> {
      FileHandler.saveEmail(null);
    });
  }

}
