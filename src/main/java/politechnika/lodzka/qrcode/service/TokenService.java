package politechnika.lodzka.qrcode.service;

import politechnika.lodzka.qrcode.model.TokenOperationType;
import politechnika.lodzka.qrcode.model.user.User;

public interface TokenService {
    String generateAndSaveTokenForUser(User user, TokenOperationType operationType);

    boolean isTokenExpired(String token);
}
