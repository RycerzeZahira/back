package politechnika.lodzka.qrcode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import politechnika.lodzka.qrcode.exception.UserNotFoundException;
import politechnika.lodzka.qrcode.model.User;
import politechnika.lodzka.qrcode.model.request.MailRequest;
import politechnika.lodzka.qrcode.repository.UserRepository;
import politechnika.lodzka.qrcode.service.MailSenderService;

import java.security.Principal;

@Controller
@RequestMapping(value = "/mail")
public class MailSenderController {

    private final MailSenderService mailSenderService;
    private final TemplateEngine templateEngine;
    private final UserRepository userRepository;

    @Autowired
    public MailSenderController(MailSenderService mailSenderService, TemplateEngine templateEngine, UserRepository userRepository) {
        this.mailSenderService = mailSenderService;
        this.templateEngine = templateEngine;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity sendMail(Principal principal, @RequestBody MailRequest mailRequest) {
        Context context = new Context();
        context.setVariable("header", "ListIt");
        context.setVariable("title", "Nowa lista");
        context.setVariable("description", "Dziękujemy za skorzystanie z aplikacji - ListIt\n" + mailRequest.getContent());

        String body = templateEngine.process("mail", context);

        if(mailRequest.getReceiver() == null){
            User user = userRepository.getUserByEmail(principal.getName())
                    .orElseThrow(() -> new UserNotFoundException("Could not found user"));

            mailSenderService.sendEmail(user.getEmail(), body);
        }else{
            mailSenderService.sendEmail(mailRequest.getReceiver(), body);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
