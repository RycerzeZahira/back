package politechnika.lodzka.qrcode.service;

public interface MailSenderService {
    void sendEmail(String to, String content);
}
