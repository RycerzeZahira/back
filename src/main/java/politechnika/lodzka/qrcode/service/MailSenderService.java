package politechnika.lodzka.qrcode.service;

public interface MailSenderService {
    void sendEmail(String to, String content);
    String createEmailContent(String header, String title, String description, String template);
}
