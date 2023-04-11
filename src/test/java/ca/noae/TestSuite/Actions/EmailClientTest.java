package ca.noae.TestSuite.Actions;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import ca.noae.Actions.EmailClient;

public class EmailClientTest {

  /**
   *
   * This test case tests the functionality of
   * {@link EmailClient#getTextFromMimeMultipart(MimeMultipart)}
   * <p>
   * It creates a MimeMultipart object with a plain text body part and calls the
   * method to retrieve the text from the body part.
   *
   * @throws MessagingException if there is an error creating the MimeMultipart
   *                            object
   * @throws IOException        if there is an error adding the plain text body
   *                            part to the MimeMultipart object
   */
  @Test
  public void testGetTextFromMimeMultipart() throws MessagingException, IOException {
    // create a MimeMultipart object with a plain text body part
    MimeBodyPart plainTextPart = new MimeBodyPart();
    plainTextPart.setText("This is a plain text message.");
    MimeMultipart mimeMultipart = new MimeMultipart();
    mimeMultipart.addBodyPart(plainTextPart);

    // call the getTextFromMimeMultipart method and verify the output
    String expectedText = "\n" + "This is a plain text message."; // method doesnt work without the newline
    String actualText = EmailClient.getTextFromMimeMultipart(mimeMultipart);
    assertEquals(expectedText, actualText);
  }

  /**
   *
   * This test case tests the functionality of
   * {@link EmailClient#getTextFromMimeMultipart(MimeMultipart)}
   * <p>
   * It creates a MimeMultipart object with a plain text body part and an HTML
   * body part and calls the method to retrieve the text from the body parts.
   *
   * @throws MessagingException if there is an error creating the MimeMultipart
   *                            object
   * @throws IOException        if there is an error adding the plain text body
   *                            part to the MimeMultipart object
   */
  @Test
  public void testGetTextFromMimeMultipartWithHtml() throws MessagingException, IOException {
    // create a MimeMultipart object with a plain text body part
    MimeBodyPart plainTextPart = new MimeBodyPart();
    plainTextPart.setText("This is a plain text message.");
    MimeBodyPart htmlPart = new MimeBodyPart();
    htmlPart.setContent("<html><body><p>This is an HTML message.</p></body></html>", "text/html");
    MimeMultipart mimeMultipart = new MimeMultipart();
    mimeMultipart.addBodyPart(plainTextPart);
    mimeMultipart.addBodyPart(htmlPart);

    // call the getTextFromMimeMultipart method and verify the output
    String expectedText = "\n" + "This is a plain text message."; // method doesnt work without the newline
    String actualText = EmailClient.getTextFromMimeMultipart(mimeMultipart);
    assertEquals(expectedText, actualText);
  }

  /**
   *
   * This test case tests the functionality of
   * {@link EmailClient#getTextFromMimeMultipart(MimeMultipart)}
   * <p>
   * It creates a MimeMultipart object with a plain text body part and an
   * attachment
   * body part and calls the method to retrieve the text from the body parts.
   *
   * @throws MessagingException if there is an error creating the MimeMultipart
   *                            object
   * @throws IOException        if there is an error adding the plain text body
   *                            part to the MimeMultipart object
   */
  @Test
  public void testGetTextFromMimeMultipartWithAttachment() throws MessagingException, IOException {
    // create mock objects for the BodyPart and MimeMultipart
    BodyPart plainTextPart = mock(BodyPart.class);
    when(plainTextPart.isMimeType("text/plain")).thenReturn(true);
    when(plainTextPart.getContent()).thenReturn("This is a plain text message.");

    BodyPart attachmentPart = mock(BodyPart.class);
    when(attachmentPart.isMimeType("text/plain")).thenReturn(false);
    when(attachmentPart.getContent()).thenReturn(new MimeMultipart());

    MimeMultipart mimeMultipart = mock(MimeMultipart.class);
    when(mimeMultipart.getCount()).thenReturn(2);
    when(mimeMultipart.getBodyPart(0)).thenReturn(plainTextPart);
    when(mimeMultipart.getBodyPart(1)).thenReturn(attachmentPart);

    // call the getTextFromMimeMultipart method and verify the output
    String expectedText = "\n" + "This is a plain text message."; // method doesnt work without the newline
    String actualText = EmailClient.getTextFromMimeMultipart(mimeMultipart);
    assertEquals(expectedText, actualText);
  }

  /**
   *
   * Tests the {@link EmailClient#parseBodyPart(BodyPart)} method with a plain
   * text BodyPart object.
   * <p>
   * Creates a mock {@code MimeBodyPart} object and sets its content to a plain
   * text message.
   * The method {@code parseBodyPart} is called with this object and the output is
   * verified.
   *
   * @throws MessagingException if there is an error with the mocked BodyPart
   *                            object
   * @throws IOException        if there is an error with the mocked BodyPart
   *                            object
   */
  @Test
  public void testParseBodyPartWithPlainText() throws MessagingException, IOException {
    // create a plain text BodyPart object
    BodyPart plainTextPart = mock(MimeBodyPart.class);
    when(plainTextPart.isMimeType("text/plain")).thenReturn(true);
    when(plainTextPart.getContent()).thenReturn("This is a plain text message.");

    // call the parseBodyPart method and verify the output
    String expectedText = "\n" + "This is a plain text message.";
    String actualText = EmailClient.parseBodyPart(plainTextPart);
    assertEquals(expectedText, actualText);
  }

  /**
   *
   * Tests the {@link EmailClient#parseBodyPart(BodyPart)} method with an HTML
   * BodyPart object.
   * <p>
   * Creates a mock {@code MimeBodyPart} object and sets its content to an HTML
   * message.
   * The method {@code parseBodyPart} is called with this object and the output is
   * verified.
   *
   * @throws MessagingException if there is an error with the mocked BodyPart
   *                            object
   * @throws IOException        if there is an error with the mocked BodyPart
   *                            object
   */
  @Test
  public void testParseBodyPartWithHtml() throws MessagingException, IOException {
    // create an HTML BodyPart object
    BodyPart htmlPart = mock(MimeBodyPart.class);
    when(htmlPart.isMimeType("text/html")).thenReturn(true);
    when(htmlPart.getContent()).thenReturn("<html><body><h1>Hello, World!</h1></body></html>");

    // call the parseBodyPart method and verify the output
    String expectedHtmlText = "\nHello, World!";
    String actualHtmlText = EmailClient.parseBodyPart(htmlPart);
    assertEquals(expectedHtmlText, actualHtmlText);
  }

  /**
   *
   * Tests the {@link EmailClient#parseBodyPart(BodyPart)} method with a
   * MimeMultipart BodyPart object.
   * <p>
   * Creates a mock {@code MimeBodyPart} object and sets its content to a
   * MimeMultipart object with a plain text body part.
   * The method {@code parseBodyPart} is called with this object and the output is
   * verified.
   *
   * @throws MessagingException if there is an error with the mocked BodyPart
   *                            object
   * @throws IOException        if there is an error with the mocked BodyPart
   *                            object
   */
  @Test
  public void testParseBodyPartWithMultipart() throws MessagingException, IOException {
    // create a MimeMultipart BodyPart object with a plain text body part
    BodyPart plainTextPart2 = mock(MimeBodyPart.class);
    when(plainTextPart2.isMimeType("text/plain")).thenReturn(true);
    when(plainTextPart2.getContent()).thenReturn("This is another plain text message.");
    MimeMultipart mimeMultipart = new MimeMultipart();
    mimeMultipart.addBodyPart(plainTextPart2);
    BodyPart multipartPart = mock(MimeBodyPart.class);
    when(multipartPart.getContent()).thenReturn(mimeMultipart);

    // call the parseBodyPart method and verify the output
    String expectedMultipartText = "\n" + "This is another plain text message.";
    String actualMultipartText = EmailClient.parseBodyPart(multipartPart);
    assertEquals(expectedMultipartText, actualMultipartText);
  }

  /**
   *
   * Tests the {@link EmailClient#parseBodyPart(BodyPart)} method with a
   * BodyPart object with an unsupported type.
   * <p>
   * Creates a mock {@code MimeBodyPart} object and sets its content to a
   * {@code String} object.
   * The method {@code parseBodyPart} is called with this object and the output is
   * verified.
   *
   * @throws MessagingException if there is an error with the mocked BodyPart
   *                            object
   * @throws IOException        if there is an error with the mocked BodyPart
   *                            object
   */
  @Test
  public void testParseBodyPartWithUnsupportedType() throws MessagingException, IOException {
    // create a BodyPart object with an unsupported type
    BodyPart unsupportedPart = mock(MimeBodyPart.class);
    when(unsupportedPart.isMimeType("text/plain")).thenReturn(false);
    when(unsupportedPart.isMimeType("text/html")).thenReturn(false);

    // call the parseBodyPart method and verify the output
    String expectedUnsupportedText = unsupportedPart.toString();
    String actualUnsupportedText = EmailClient.parseBodyPart(unsupportedPart);
    assertEquals(expectedUnsupportedText, actualUnsupportedText);
  }
}
