package politechnika.lodzka.qrcode.service.Impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import politechnika.lodzka.qrcode.model.Language;
import politechnika.lodzka.qrcode.model.MailType;
import politechnika.lodzka.qrcode.service.MailSenderService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
class MailSenderServiceImpl implements MailSenderService {
    private final static String ACTIVATION_SUBJECT_PL = "ListIt - Link aktywacyjny";
    private final static String ACTIVATION_SUBJECT_EN = "ListIt - Activation Link";
    private final static String LIST_SUBJECT_PL = "ListIt - Nowa lista";
    private final static String LIST_SUBJECT_EN = "ListIt - New list";
    private final static String PASSWORD_RESET_SUBJECT_PL = "ListIt - Reset hasła";
    private final static String PASSWORD_RESET_SUBJECT_EN = "ListIt - Password reset";

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String mailSender;

    public MailSenderServiceImpl(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendEmail(final String to, final String content,
                          final MailType mailType, Language language) {
        try {
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
            helper.setFrom(mailSender);
            helper.setSubject(chooseMailSubject(mailType, language));
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

    @Override
    public String createTokenOperationEmail(String name, String token, String mailDescription, String activationDescription, String confirmation) {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("token", token);
        context.setVariable("mailDescription", mailDescription);
        context.setVariable("activationDescription", activationDescription);
        context.setVariable("confirmation", confirmation);

        return templateEngine.process(MailType.ACTIVATION.getMailType(), context);
    }

    private String chooseMailSubject(MailType mailType, Language language){
        switch (mailType){
            case ACTIVATION:
                return chooseMailLanguage(language, ACTIVATION_SUBJECT_PL, ACTIVATION_SUBJECT_EN);
            case LIST:
                return chooseMailLanguage(language, LIST_SUBJECT_PL, LIST_SUBJECT_EN);
            case PASSWORD_RESET:
                return chooseMailLanguage(language, PASSWORD_RESET_SUBJECT_PL, PASSWORD_RESET_SUBJECT_EN);
            default:
                return ACTIVATION_SUBJECT_EN;
        }
    }

    private String chooseMailLanguage(Language language, String subject_pl, String subject_en) {
        switch (language){
            case PL:
                return subject_pl;
            case EN:
                return subject_en;
            default:
                return ACTIVATION_SUBJECT_EN;
        }
    }
}
