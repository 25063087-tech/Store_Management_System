import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import java.io.File;

public class EmailService {

    // =================================================================
    // GMAIL REAL SENDING CONFIGURATION
    // =================================================================

    // 1. Enter your REAL Gmail address here
    private static final String SMTP_USERNAME = "adibrahman708@gmail.com";

    // 2. Enter your 16-character Google App Password here (NOT your login password)
    // Generate this at: Google Account > Security > 2-Step Verification > App Passwords
    private static final String SMTP_PASSWORD = "taqb ixyo zekh rhxt";

    // Standard Gmail Settings (Do not change these)
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";

    public static void sendDailyReport(String recipientEmail, String filePath, String date) {
        System.out.println(">> Connecting to Gmail SMTP Server...");

        // 1. Setup Mail Properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);

        // 2. Create Session with Authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
            }
        });

        try {
            // 3. Create the Email Message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SMTP_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("GOLDENHOUR Daily Sales Report - " + date);

            // 4. Create Email Body Text
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Dear Manager,\n\n"
                    + "Please find attached the official daily sales report for " + date + ".\n"
                    + "This report was generated automatically by the store system.\n\n"
                    + "Best regards,\n"
                    + "Store Management System");

            // 5. Create Attachment
            MimeBodyPart attachmentPart = new MimeBodyPart();
            File file = new File(filePath);

            if (file.exists()) {
                DataSource source = new FileDataSource(file);
                attachmentPart.setDataHandler(new DataHandler(source));
                attachmentPart.setFileName(file.getName());
            } else {
                System.out.println(">> Error: Attachment file not found at " + filePath);
                return;
            }

            // 6. Combine Body and Attachment
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            // 7. Send the Email
            Transport.send(message);
            System.out.println(">> Email sent successfully to " + recipientEmail);

        } catch (MessagingException e) {
            System.out.println(">> Error: Failed to send email via Gmail.");
            System.out.println(">> Possible causes: Wrong App Password, or 2-Step Verification not enabled.");
            e.printStackTrace();
        }
    }
}