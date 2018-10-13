package politechnika.lodzka.qrcode.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    public UserController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @ApiOperation(value = "Signing in user", notes = "Returns authentication token")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful Login"),
            @ApiResponse(code = 401, message = "Wrong data")})
    @PostMapping(value = "/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        Authentication authentication = authService.auth(authenticationRequest);
        String tokenQuery = authService.getAuthenticationToken(authentication);

        return ResponseEntity.ok(new JwtAuthenticationResponse(tokenQuery));
    }

    @ApiOperation(value = "Getting user profile", notes = "Returns user profile")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful user's profile getting"),
            @ApiResponse(code = 401, message = "Wrong authentication token"),
            @ApiResponse(code = 404, message = "User not found")})
    @GetMapping(value = "/profile")
    public User getUserProfile(Principal principal) {

        return userRepository.getUserByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("Could not found user"));
    }
}
