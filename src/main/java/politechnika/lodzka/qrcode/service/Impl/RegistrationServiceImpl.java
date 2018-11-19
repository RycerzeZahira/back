package politechnika.lodzka.qrcode.service.Impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import politechnika.lodzka.qrcode.exception.UserAlreadyExistsException;
import politechnika.lodzka.qrcode.exception.UserNotFoundException;
import politechnika.lodzka.qrcode.model.Language;
import politechnika.lodzka.qrcode.model.MailType;
import politechnika.lodzka.qrcode.model.TokenOperationType;
import politechnika.lodzka.qrcode.model.request.RegistrationRequest;
import politechnika.lodzka.qrcode.model.user.AccountStatus;
import politechnika.lodzka.qrcode.model.user.User;
import politechnika.lodzka.qrcode.repository.TokenRepository;
import politechnika.lodzka.qrcode.repository.UserRepository;
import politechnika.lodzka.qrcode.service.MailSenderService;
import politechnika.lodzka.qrcode.service.RegistrationService;
import politechnika.lodzka.qrcode.service.TokenService;

@Service
class RegistrationServiceImpl implements RegistrationService {
    @Value("${server.url}")
    private String serverUrl;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final TokenService tokenService;
    private final MailSenderService mailSenderService;

    public RegistrationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                   TokenRepository tokenRepository, TokenService tokenService,
                                   MailSenderService mailSenderService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.tokenService = tokenService;
        this.mailSenderService = mailSenderService;
    }

    @Override
    @Transactional
    public void registerUser(final RegistrationRequest registrationRequest, final Language language) {
        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new UserAlreadyExistsException("User with provided e-mail already exists");
        }

        final User user = User.builder()
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .isLocked(false)
                .status(AccountStatus.INACTIVE)
                .build();

        userRepository.save(user);

        String token = tokenService.generateAndSaveTokenForUser(user, TokenOperationType.ACTIVATION);

        String mailContent = createRegistrationEmailContent(language, new StringBuilder().append(serverUrl).append("/registration/activate/").append(token).toString(), user);

        mailSenderService.sendEmail(user.getEmail(), mailContent, MailType.ACTIVATION, language);
    }

    @Override
    @Transactional
    public void activateUser(String token) {
        User user = tokenRepository.getUserByActivationToken(token).orElseThrow(
                () -> new UserNotFoundException("User not found"));
        user.setStatus(AccountStatus.ACTIVE);
        tokenRepository.deleteActivationTokenByToken(token);
    }

    private String createRegistrationEmailContent(Language language, String token, User user) {
        String mailContent;
        switch (language) {
            case PL:
                mailContent = mailSenderService.createTokenOperationEmail(new StringBuilder().append("Witaj ").append(user.getEmail(), 0, user.getEmail().indexOf("@")).append("!").toString(),
                        token,
                        "Po poprawnej aktywacji konta będziesz mógł zalogować się w aplikacji mobilnej.",
                        "Dziękujemy za rejestrację w ListIt, aby dokończyć ten proces proszę naciśnij link poniżej:",
                        "Kliknij tutaj, aby potwierdzić adres e-mail");
                break;
            case EN:
                mailContent = mailSenderService.createTokenOperationEmail(new StringBuilder().append("Hello ").append(user.getEmail(), 0, user.getEmail().indexOf("@")).append("!").toString(),
                        token,
                        "You will be able to sign in to mobile application after successful activation.",
                        "Thank You for signing up in ListIt. Please follow the link below to complete the registration:",
                        "Click here to activate your e-mail address");
                break;
            default:
                mailContent = mailSenderService.createTokenOperationEmail(new StringBuilder().append("Hello ").append(user.getEmail(), 0, user.getEmail().indexOf("@")).append("!").toString(),
                        token,
                        "You will be able to sign in to mobile application after successful activation.",
                        "Thank You for signing up in ListIt. Please follow the link below to complete the registration:",
                        "Click here to activate your e-mail address");
        }

        return mailContent;
    }
}
