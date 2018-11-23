package politechnika.lodzka.qrcode.service.Impl;

import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import politechnika.lodzka.qrcode.model.Language;
import politechnika.lodzka.qrcode.model.MailType;
import politechnika.lodzka.qrcode.model.response.AnswerResponse;
import politechnika.lodzka.qrcode.repository.AnswersRepository;
import politechnika.lodzka.qrcode.service.FormService;
import politechnika.lodzka.qrcode.service.MailSenderService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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
    private final FormService formService;
    private final AnswersRepository answersRepository;

    @Value("${spring.mail.username}")
    private String mailSender;

    public MailSenderServiceImpl(JavaMailSender javaMailSender, TemplateEngine templateEngine, @Lazy FormService formService, AnswersRepository answersRepository, AnswersRepository answersRepository1) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.formService = formService;
        this.answersRepository = answersRepository1;
    }

    @Override
    public void sendListEmail(String to, String formCode, MailType mailType, Language language) throws IOException {
        ArrayList<AnswerResponse> answers = (ArrayList<AnswerResponse>) formService.getAnswers(formCode);

        File file = new File("list.csv");
        FileWriter outputFile = new FileWriter(file);
        CSVWriter writer = new CSVWriter(outputFile, '\t', CSVWriter.DEFAULT_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);

        for (AnswerResponse answerResponse : answers) {
            writer.writeNext(new String[]{answerResponse.toString()});
        }
        writer.close();

        String mailContent;
        switch (language) {
            case EN:
                mailContent = createListEmail(new StringBuilder().append("Hello").append(to, 0, to.indexOf("@")).append("!").toString(),
                        "You can find your list in the attachment.");
                break;
            case PL:
                mailContent = createListEmail(new StringBuilder().append("Witaj").append(to, 0, to.indexOf("@")).append("!").toString(),
                        "W załączniku znajduje się Twoja lista.");
                break;
            default:
                mailContent = createListEmail(new StringBuilder().append("Hello").append(to, 0, to.indexOf("@")).append("!").toString(),
                        "You can find your list in the attachment.");
                break;
        }

        sendEmail(to, mailContent, mailType, language, file);
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
    public void sendEmail(final String to, final String content,
                          final MailType mailType, Language language,
                          final File file) {
        try {
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
            helper.setFrom(mailSender);
            helper.setSubject(chooseMailSubject(mailType, language));
            helper.setText(content, true);
            helper.addAttachment("list.csv", file);
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

    private String createListEmail(String name, String description) {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("description", description);

        return templateEngine.process(MailType.LIST.getMailType(), context);
    }

    private String chooseMailSubject(MailType mailType, Language language) {
        switch (mailType) {
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
        switch (language) {
            case PL:
                return subject_pl;
            case EN:
                return subject_en;
            default:
                return ACTIVATION_SUBJECT_EN;
        }
    }
}
