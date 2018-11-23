package politechnika.lodzka.qrcode.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import politechnika.lodzka.qrcode.model.Language;
import politechnika.lodzka.qrcode.model.MailType;
import politechnika.lodzka.qrcode.repository.UserRepository;
import politechnika.lodzka.qrcode.service.MailSenderService;

import java.io.IOException;
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
    @PostMapping("/{formCode}")
    public ResponseEntity sendMail(Principal principal, @PathVariable("formCode") String formCode, @RequestHeader(HttpHeaders.ACCEPT_LANGUAGE) final String language) {
        try {
            mailSenderService.sendListEmail(principal.getName(), formCode, MailType.LIST, Language.fromString(language));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
