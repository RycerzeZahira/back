package politechnika.lodzka.qrcode.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import politechnika.lodzka.qrcode.exception.UserNotFoundException;
import politechnika.lodzka.qrcode.model.Language;
import politechnika.lodzka.qrcode.model.MailType;
import politechnika.lodzka.qrcode.model.request.MailRequest;
import politechnika.lodzka.qrcode.model.user.User;
import politechnika.lodzka.qrcode.repository.UserRepository;
import politechnika.lodzka.qrcode.service.MailSenderService;

import java.security.Principal;

@Controller
@RequestMapping(value = "/mail")
public class MailSenderController {

    private final MailSenderService mailSenderService;
    private final UserRepository userRepository;

    public MailSenderController(MailSenderService mailSenderService, UserRepository userRepository) {
        this.mailSenderService = mailSenderService;
        this.userRepository = userRepository;
    }

    @ApiOperation(value = "Sending e-mail")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful e-mail sending"),
            @ApiResponse(code = 401, message = "Wrong auth token"),
            @ApiResponse(code = 404, message = "Couldn't find user")})
    @PostMapping
    public ResponseEntity sendMail(Principal principal, @RequestBody MailRequest mailRequest, @RequestHeader(HttpHeaders.ACCEPT_LANGUAGE) final Language language) {
        String mailContent = mailSenderService.createEmailContent(new StringBuilder().append("Witaj ").append(principal.getName()).append("!").toString(),
                "Oto Twoja lista: ", mailRequest.getContent(), MailType.LIST.getMailType());

        if (mailRequest.getReceiver() == null) {
            User user = userRepository.getUserByEmail(principal.getName())
                    .orElseThrow(() -> new UserNotFoundException("Could not found user"));

            mailSenderService.sendEmail(user.getEmail(), mailContent, MailType.LIST, language);
        } else {
            mailSenderService.sendEmail(mailRequest.getReceiver(), mailContent, MailType.LIST, language);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
