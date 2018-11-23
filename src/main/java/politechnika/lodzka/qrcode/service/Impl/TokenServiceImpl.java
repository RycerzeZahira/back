package politechnika.lodzka.qrcode.service.Impl;

import org.springframework.stereotype.Service;
import politechnika.lodzka.qrcode.exception.OperationTokenNotFoundException;
import politechnika.lodzka.qrcode.exception.UserAlreadyActivatedException;
import politechnika.lodzka.qrcode.model.OperationToken;
import politechnika.lodzka.qrcode.model.TokenOperationType;
import politechnika.lodzka.qrcode.model.user.AccountStatus;
import politechnika.lodzka.qrcode.model.user.User;
import politechnika.lodzka.qrcode.repository.TokenRepository;
import politechnika.lodzka.qrcode.service.TokenService;

import java.util.Calendar;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;

    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public String generateAndSaveTokenForUser(User user, TokenOperationType operationType) {
        if (AccountStatus.ACTIVE.equals(user.getStatus()) && operationType.equals(TokenOperationType.ACTIVATION)) {
            throw new UserAlreadyActivatedException("User already activated");
        }

        String token = UUID.randomUUID().toString();

        final OperationToken operationToken = new OperationToken();
        operationToken.setToken(token);
        operationToken.setUser(user);

        tokenRepository.save(operationToken);

        return token;
    }

    @Override
    public boolean isTokenExpired(String token) {
        OperationToken operationToken = tokenRepository.findByToken(token).orElseThrow(
                () -> new OperationTokenNotFoundException("Operation token not found"));

        Calendar cal = Calendar.getInstance();
        return (operationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0;
    }
}
