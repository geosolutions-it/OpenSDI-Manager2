package it.geosolutions.opensdi2.workflow.utils;
import java.util.Properties;
 
import javax.mail.internet.MimeMessage;
 
import org.springframework.mail.MailException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;
 /**
  * Mock class for mail sending test.
  * @author lorenzo
  *
  */
public class MockMailSender extends JavaMailSenderImpl {
 
  @Override
  public void send(final MimeMessagePreparator mimeMessagePreparator) throws MailException {
    final MimeMessage mimeMessage = createMimeMessage();
    try {
      mimeMessagePreparator.prepare(mimeMessage);
      final String content = (String) mimeMessage.getContent();
      final Properties javaMailProperties = getJavaMailProperties();
      javaMailProperties.setProperty("mailContent", content);
    } catch (final Exception e) {
      throw new MailPreparationException(e);
    }
  }
}