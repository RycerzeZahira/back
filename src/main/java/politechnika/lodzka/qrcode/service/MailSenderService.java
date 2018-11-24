package politechnika.lodzka.qrcode.service;

import politechnika.lodzka.qrcode.model.Language;
import politechnika.lodzka.qrcode.model.MailType;

import java.io.File;
import java.io.IOException;

public interface MailSenderService {
    void sendEmail(String to, String content, MailType mailType, Language language);

    void sendEmail(String to, String content, MailType mailType, Language language, File file, String fileName);

    String createEmailContent(String header, String title, String description, String template);

    String createTokenOperationEmail(String name, String token, String mailDescription, String activationDescription, String confirmation);

    void sendListEmail(String to, String formCode, MailType mailType, Language language) throws IOException;
}
