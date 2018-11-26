package politechnika.lodzka.qrcode.service.Impl;

import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import politechnika.lodzka.qrcode.exception.NotOwnerException;
import politechnika.lodzka.qrcode.exception.UserNotFoundException;
import politechnika.lodzka.qrcode.model.Form;
import politechnika.lodzka.qrcode.model.Language;
import politechnika.lodzka.qrcode.model.MailType;
import politechnika.lodzka.qrcode.model.scheme.Answer;
import politechnika.lodzka.qrcode.model.scheme.TypeClass;
import politechnika.lodzka.qrcode.model.user.User;
import politechnika.lodzka.qrcode.repository.AnswersRepository;
import politechnika.lodzka.qrcode.repository.UserRepository;
import politechnika.lodzka.qrcode.service.FormService;
import politechnika.lodzka.qrcode.service.MailSenderService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

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
    private final UserRepository userRepository;

    @Value("${spring.mail.username}")
    private String mailSender;

    public MailSenderServiceImpl(JavaMailSender javaMailSender, TemplateEngine templateEngine,
                                 @Lazy FormService formService, AnswersRepository answersRepository, UserRepository userRepository) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.formService = formService;
        this.answersRepository = answersRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void sendListEmail(String to, String formCode, MailType mailType, Language language) throws IOException {
        Collection<Answer> answers = answersRepository.findAnswerByFormCode(formCode);
        Form form = formService.findByCode(formCode);
        User user = userRepository.getUserByEmail(to).orElseThrow(() -> new UserNotFoundException("Could not find user"));

        if (user != form.getGroup().getModerator()) {
            throw new NotOwnerException(user, form.getGroup().toString());
        }

        String fileName = form.getRoot().getName() + ".csv";
        File file = new File(fileName);
        FileWriter outputFile = new FileWriter(file);

        CSVWriter writer = new CSVWriter(outputFile, '\t', CSVWriter.DEFAULT_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
        writeDataToFile(writer, form, answers);
        writer.close();

        String mailContent = createListEmailContent(language, to);
        sendEmail(to, mailContent, mailType, language, file, fileName);

        file.delete();
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
                          final File file, final String fileName) {
        try {
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
            helper.setFrom(mailSender);
            helper.setSubject(chooseMailSubject(mailType, language));
            helper.setText(content, true);
            helper.addAttachment(fileName, file);
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

    private String[] getAnswersFieldsNames(Collection<Answer> answers) {
        ArrayList<String> fieldsNames = new ArrayList<>();
        Optional<Answer> userAnswer = answers.stream().findFirst();
        if (userAnswer.isPresent()) {
            for (Answer answer : userAnswer.get().getChilds()) {
                if (!TypeClass.GROUP.equals(answer.getScheme().getType())) {
                    fieldsNames.add(answer.getScheme().getName());
                }
            }
        }

        return convertListOfStringsToArray(fieldsNames);
    }

    private String[] convertListOfStringsToArray(ArrayList<String> stockList) {
        String[] stockArr = new String[stockList.size()];
        return stockList.toArray(stockArr);
    }

    private void writeDataToFile(CSVWriter writer, Form form, Collection<Answer> answers) {
        writer.writeNext(new String[]{form.getRoot().getName()});
        writer.writeNext(getAnswersFieldsNames(answers));

        for (Answer userAnswer : answers) {
            ArrayList<String> answersList = new ArrayList<>();
            for (Answer answer : userAnswer.getChilds()) {
                if (!TypeClass.GROUP.equals(answer.getScheme().getType())) {
                    Object object = answer.getValue();
                    answersList.add(object.toString());
                }
            }
            writer.writeNext(convertListOfStringsToArray(answersList));
        }
    }

    private String createListEmailContent(Language language, String to) {
        switch (language) {
            case EN:
                return createListEmail(new StringBuilder().append("Hello ").append(to, 0, to.indexOf("@")).append("!").toString(),
                        "You can find your list in the attachment.");
            case PL:
                return createListEmail(new StringBuilder().append("Witaj ").append(to, 0, to.indexOf("@")).append("!").toString(),
                        "W załączniku znajduje się Twoja lista.");
            default:
                return createListEmail(new StringBuilder().append("Hello ").append(to, 0, to.indexOf("@")).append("!").toString(),
                        "You can find your list in the attachment.");
        }
    }

    private String createListEmail(String name, String description) {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("description", description);

        return templateEngine.process(MailType.LIST.getMailType(), context);
    }
}
