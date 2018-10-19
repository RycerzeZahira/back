package politechnika.lodzka.qrcode.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @ApiOperation(value = "Signing up user")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful registration"),
            @ApiResponse(code = 400, message = "Invalid request body")})
    @PostMapping
    public ResponseEntity userRegistration(@RequestBody @Valid final RegistrationRequest registrationRequest) {
        registrationService.registerUser(registrationRequest);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @ApiOperation(value = "User activation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful user activation")
    })
    @GetMapping(value = "/activate/{token}")
    public ResponseEntity userActivation(final @PathVariable("token") String token) {
        registrationService.activateUser(token);

        return new ResponseEntity(HttpStatus.OK);
    }
}
