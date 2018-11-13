package politechnika.lodzka.qrcode.service;

import politechnika.lodzka.qrcode.model.Language;
import politechnika.lodzka.qrcode.model.MailType;

public interface MailSenderService {
    void sendEmail(String to, String content, MailType mailType, Language language);

    String createEmailContent(String header, String title, String description, String template);

    String createTokenOperationEmail(String name, String token, String mailDescription, String activationDescription, String confirmation);
}
