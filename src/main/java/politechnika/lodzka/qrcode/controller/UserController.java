package politechnika.lodzka.qrcode.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import politechnika.lodzka.qrcode.exception.UserNotFoundException;
import politechnika.lodzka.qrcode.model.Language;
import politechnika.lodzka.qrcode.model.request.AuthenticationRequest;
import politechnika.lodzka.qrcode.model.request.ResetPasswordRequest;
import politechnika.lodzka.qrcode.model.request.SendResetPasswordEmailRequest;
import politechnika.lodzka.qrcode.model.response.JwtAuthenticationResponse;
import politechnika.lodzka.qrcode.model.user.User;
import politechnika.lodzka.qrcode.repository.UserRepository;
import politechnika.lodzka.qrcode.service.AuthService;
import politechnika.lodzka.qrcode.service.TokenService;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public UserController(AuthService authService, UserRepository userRepository, TokenService tokenService) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
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

    @PostMapping("/changePassword")
    public ResponseEntity changePassword(String oldPassword, String newPassword) {
        return ResponseEntity.ok(authService.changePassword(oldPassword, newPassword) ? HttpStatus.NO_CONTENT : HttpStatus.UNAUTHORIZED);
    }

    @ApiOperation(value = "Sending password reset link")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful email sending"),
            @ApiResponse(code = 404, message = "User not found")})
    @PostMapping("/sendResetPasswordMail")
    public ResponseEntity sendResetPasswordEmail(@Valid @RequestBody final SendResetPasswordEmailRequest resetPasswordRequest,
                                                 @RequestHeader(HttpHeaders.ACCEPT_LANGUAGE) final String language) {
        authService.sendResetPasswordEmail(resetPasswordRequest.getEmail(), Language.fromString(language));

        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "Resets User's password")
    @PostMapping("/resetPassword")
    public ModelAndView resetPassword(@ModelAttribute("resetPasswordForm") @Valid ResetPasswordRequest resetPasswordRequest,
                                      WebRequest request) {
        String lang = request.getLocale().getLanguage();
        if (tokenService.isTokenExpired(resetPasswordRequest.getToken())) {
            return new ModelAndView(new StringBuilder().append("redirect:/view/expiredToken?lang=").append(lang).toString());
        }
        try {
            authService.resetPassword(resetPasswordRequest.getToken(), resetPasswordRequest.getPassword(), resetPasswordRequest.getConfirmationPassword());
        } catch (Exception ex) {
            return new ModelAndView(new StringBuilder().append("redirect:/view/failurePasswordChange?lang=").append(lang).toString());
        }

        return new ModelAndView(new StringBuilder().append("redirect:/view/successPasswordChange?lang=").append(lang).toString());
    }
}
