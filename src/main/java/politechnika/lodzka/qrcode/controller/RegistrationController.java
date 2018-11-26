package politechnika.lodzka.qrcode.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import politechnika.lodzka.qrcode.model.Language;
import politechnika.lodzka.qrcode.model.request.RegistrationRequest;
import politechnika.lodzka.qrcode.service.RegistrationService;
import politechnika.lodzka.qrcode.service.TokenService;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/registration")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final TokenService tokenService;

    public RegistrationController(RegistrationService registrationService, TokenService tokenService) {
        this.registrationService = registrationService;
        this.tokenService = tokenService;
    }

    @ApiOperation(value = "Signing up user")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful registration"),
            @ApiResponse(code = 400, message = "Invalid request body")})
    @PostMapping
    public ResponseEntity userRegistration(@RequestBody @Valid final RegistrationRequest registrationRequest, @RequestHeader(HttpHeaders.ACCEPT_LANGUAGE) final String language) {
        registrationService.registerUser(registrationRequest, Language.fromString(language));

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @ApiOperation(value = "User activation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful user activation")
    })
    @GetMapping(value = "/activate/{token}")
    public String userActivation(final @PathVariable("token") String token, WebRequest request) {
        String lang = request.getLocale().getLanguage();

        try {
            if (tokenService.isTokenExpired(token)) {
                return new StringBuilder().append("redirect:/view/expiredToken?lang=").append(lang).toString();
            }
            registrationService.activateUser(token);
        } catch (Exception ex) {
            return new StringBuilder().append("redirect:/view/failure?lang=").append(lang).toString();
        }

        return new StringBuilder().append("redirect:/view/success?lang=").append(lang).toString();
    }
}
