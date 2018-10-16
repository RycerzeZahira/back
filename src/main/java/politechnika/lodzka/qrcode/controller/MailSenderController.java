package politechnika.lodzka.qrcode.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    public MailSenderController(MailSenderService mailSenderService, TemplateEngine templateEngine, UserRepository userRepository) {
        this.mailSenderService = mailSenderService;
        this.templateEngine = templateEngine;
        this.userRepository = userRepository;
    }

    @ApiOperation(value = "Sending e-mail")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful e-mail sending"),
            @ApiResponse(code = 401, message = "Wrong auth token"),
            @ApiResponse(code = 404, message = "Couldn't find user")})
    @PostMapping
    public ResponseEntity sendMail(Principal principal, @RequestBody MailRequest mailRequest) {
        Context context = new Context();
        context.setVariable("header", "Witaj " + principal.getName() + "!");
        context.setVariable("title", "Oto twoja lista");
        context.setVariable("description", mailRequest.getContent());

        String body = templateEngine.process("mail", context);

        if (mailRequest.getReceiver() == null) {
            User user = userRepository.getUserByEmail(principal.getName())
                    .orElseThrow(() -> new UserNotFoundException("Could not found user"));

            mailSenderService.sendEmail(user.getEmail(), body);
        } else {
            mailSenderService.sendEmail(mailRequest.getReceiver(), body);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
