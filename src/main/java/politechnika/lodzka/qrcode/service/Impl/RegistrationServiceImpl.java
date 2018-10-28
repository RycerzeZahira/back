package politechnika.lodzka.qrcode.service.Impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import politechnika.lodzka.qrcode.exception.UserAlreadyExistsException;
import politechnika.lodzka.qrcode.model.ActivationToken;
import politechnika.lodzka.qrcode.model.Language;
import politechnika.lodzka.qrcode.model.MailType;
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
                .status(AccountStatus.ACTIVE)
                .build();

        userRepository.save(user);

 /*       final ActivationToken activationToken = new ActivationToken();
        String token = tokenService.generateActivationToken(user);
        activationToken.setToken(token);
        activationToken.setUser(user);

        tokenRepository.save(activationToken);
        String mailContent = createRegistrationEmailContent(language, token, user);

        mailSenderService.sendEmail(user.getEmail(), mailContent, MailType.ACTIVATION, language);*/
    }

    @Override
    @Transactional
    public void activateUser(String token) {
        User user = tokenRepository.getUserIdByActivationToken(token);
        user.setStatus(AccountStatus.ACTIVE);
        tokenRepository.deleteActivationTokenByToken(token);
    }

    private String createRegistrationEmailContent(Language language, String token, User user) {
        String mailContent;
        switch (language) {
            case PL:
                mailContent = mailSenderService.createActivationEmail(new StringBuilder().append("Witaj ").append(user.getEmail(), 0, user.getEmail().indexOf("@")).append("!").toString(),
                        token,
                        "Dziękujemy za rejestrację w serwisie ListIt",
                        "Oto Twój link aktywacyjny:",
                        "Kliknij tutaj, aby potwierdzić adres e-mail");
                break;
            case EN:
                mailContent = mailSenderService.createActivationEmail(new StringBuilder().append("Hello ").append(user.getEmail(), 0, user.getEmail().indexOf("@")).append("!").toString(),
                        token,
                        "Thank You for signing up in ListIt",
                        "Here is your activation link",
                        "Click here to activate your e-mail address");
                break;
            default:
                mailContent = mailSenderService.createActivationEmail(new StringBuilder().append("Hello ").append(user.getEmail(), 0, user.getEmail().indexOf("@")).append("!").toString(),
                        token,
                        "Thank You for signing up in ListIt",
                        "Here is your activation link",
                        "Click here to activate your e-mail address");
        }

        return mailContent;
    }
}
