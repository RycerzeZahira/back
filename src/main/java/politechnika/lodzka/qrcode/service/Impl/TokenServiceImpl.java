package politechnika.lodzka.qrcode.service.Impl;

import org.springframework.stereotype.Service;
import politechnika.lodzka.qrcode.exception.UserAlreadyActivatedException;
import politechnika.lodzka.qrcode.model.user.AccountStatus;
import politechnika.lodzka.qrcode.model.user.User;
import politechnika.lodzka.qrcode.service.TokenService;

import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {
    @Override
    public String generateActivationToken(User user) {
        if (AccountStatus.ACTIVE.equals(user.getStatus())) {
            throw new UserAlreadyActivatedException("User already activated");
        }

        return UUID.randomUUID().toString();
    }
}
