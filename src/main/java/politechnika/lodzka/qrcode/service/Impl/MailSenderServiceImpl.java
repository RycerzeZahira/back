package politechnika.lodzka.qrcode.service.Impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import politechnika.lodzka.qrcode.service.MailSenderService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
class MailSenderServiceImpl implements MailSenderService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${mail.sender}")
    private String mailSender;
    @Value("${mail.subject}")
    private String mailSubject;

    public MailSenderServiceImpl(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendEmail(final String to, final String content) {
        try {
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
            helper.setFrom(mailSender);
            helper.setSubject(mailSubject);
            helper.setText(content, true);
            javaMailSender.send(mail);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String createEmailContent(String header, String title, String description, String template) {
        Context context = new Context();
        context.setVariable("header", header);
        context.setVariable("title", title);
        context.setVariable("description", description);

        return templateEngine.process(template, context);
    }
}
