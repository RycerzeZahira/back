package politechnika.lodzka.qrcode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import politechnika.lodzka.qrcode.exception.UserNotFoundException;
import politechnika.lodzka.qrcode.model.User;
import politechnika.lodzka.qrcode.model.request.AuthenticationRequest;
import politechnika.lodzka.qrcode.model.response.JwtAuthenticationResponse;
import politechnika.lodzka.qrcode.repository.UserRepository;
import politechnika.lodzka.qrcode.service.AuthService;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final AuthService authService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        Authentication authentication = authService.auth(authenticationRequest);
        String tokenQuery = authService.getAuthenticationToken(authentication);

        return ResponseEntity.ok(new JwtAuthenticationResponse(tokenQuery));
    }

    @GetMapping(value = "/profile")
    public User getUserProfile(Principal principal){
        User user = userRepository.getUserByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("Could not found user"));

        return user;
    }
}
