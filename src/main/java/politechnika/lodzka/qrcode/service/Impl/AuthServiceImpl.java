package politechnika.lodzka.qrcode.service.Impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import politechnika.lodzka.qrcode.exception.PasswordNoMatchException;
import politechnika.lodzka.qrcode.exception.UserNotActivatedException;
import politechnika.lodzka.qrcode.exception.UserNotFoundException;
import politechnika.lodzka.qrcode.model.Language;
import politechnika.lodzka.qrcode.model.MailType;
import politechnika.lodzka.qrcode.model.TokenOperationType;
import politechnika.lodzka.qrcode.model.request.AuthenticationRequest;
import politechnika.lodzka.qrcode.model.user.AccountStatus;
import politechnika.lodzka.qrcode.model.user.User;
import politechnika.lodzka.qrcode.repository.TokenRepository;
import politechnika.lodzka.qrcode.repository.UserRepository;
import politechnika.lodzka.qrcode.security.JwtTokenProvider;
import politechnika.lodzka.qrcode.service.AuthService;
import politechnika.lodzka.qrcode.service.MailSenderService;
import politechnika.lodzka.qrcode.service.TokenService;

@Service
class AuthServiceImpl implements AuthService {
    @Value("${server.url}")
    private String serverUrl;

    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final TokenService tokenService;
    private final MailSenderService mailSenderService;


    public AuthServiceImpl(JwtTokenProvider tokenProvider, AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder, TokenRepository tokenRepository, TokenService tokenService, MailSenderService mailSenderService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.tokenService = tokenService;
        this.mailSenderService = mailSenderService;
    }

    @Override
    public Authentication auth(AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getLogin(),
                        authenticationRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User userOpt = userRepository.getUserByEmail(authenticationRequest.getLogin()).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (userOpt.getStatus().equals(AccountStatus.INACTIVE)) {
            throw new UserNotActivatedException("User is not activated");
        }

        return authentication;
    }

    @Override
    public String getAuthenticationToken(Authentication authentication) {
        return tokenProvider.generateToken(authentication);
    }

    @Override
    public User getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.getUserByEmail(userDetails.getUsername()).orElseThrow(() -> new UserNotFoundException(userDetails.getUsername()));
    }

    @Override
    public boolean changePassword(String oldPassword, String newPassword) {
        User current = getCurrentUser();
        if (auth(new AuthenticationRequest(current.getEmail(), oldPassword)).isAuthenticated()) {
            current.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(current);
            return true;
        }
        return false;
    }

    @Override
    public void sendResetPasswordEmail(final String email, final Language language) {
        User user = userRepository.getUserByEmail(email).orElseThrow(
                () -> new UserNotFoundException("User not found"));

        String token = tokenService.generateAndSaveTokenForUser(user, TokenOperationType.PASSWORD_RESET);

        String mailContent = createResetPasswordEmailContent(language, new StringBuilder().append(serverUrl).append("/view/resetPassword?token=").append(token).toString(), user);

        mailSenderService.sendEmail(user.getEmail(), mailContent, MailType.PASSWORD_RESET, language);
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword, String repeatedNewPassword) {
        User user = tokenRepository.getUserByActivationToken(token).orElseThrow(
                () -> new UserNotFoundException("User not found"));

        if (!newPassword.equals(repeatedNewPassword)) {
            throw new PasswordNoMatchException("Passwords don't match");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        tokenRepository.deleteActivationTokenByToken(token);
    }

    private String createResetPasswordEmailContent(Language language, String token, User user) {
        String mailContent;
        switch (language) {
            case PL:
                mailContent = mailSenderService.createTokenOperationEmail(new StringBuilder().append("Witaj ").append(user.getEmail(), 0, user.getEmail().indexOf("@")).append("!").toString(),
                        token,
                        "Reset hasła",
                        "Oto Twój link resetujący hasło:",
                        "Kliknij tutaj, aby zresetować hasło");
                break;
            case EN:
                mailContent = mailSenderService.createTokenOperationEmail(new StringBuilder().append("Hello ").append(user.getEmail(), 0, user.getEmail().indexOf("@")).append("!").toString(),
                        token,
                        "Password Reset",
                        "Here is your password reset link",
                        "Click here to reset password");
                break;
            default:
                mailContent = mailSenderService.createTokenOperationEmail(new StringBuilder().append("Hello ").append(user.getEmail(), 0, user.getEmail().indexOf("@")).append("!").toString(),
                        token,
                        "Password Reset",
                        "Here is your password reset link",
                        "Click here to reset password");
        }

        return mailContent;
    }
}
