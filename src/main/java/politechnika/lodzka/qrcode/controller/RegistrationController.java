package politechnika.lodzka.qrcode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import politechnika.lodzka.qrcode.model.request.RegistrationRequest;
import politechnika.lodzka.qrcode.service.RegistrationService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/registration")
public class RegistrationController {

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity userRegistration(@RequestBody @Valid final RegistrationRequest registrationRequest) {
        registrationService.registerUser(registrationRequest);

        return new ResponseEntity(HttpStatus.CREATED);
    }
}
