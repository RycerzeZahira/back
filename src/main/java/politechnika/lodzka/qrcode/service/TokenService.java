package politechnika.lodzka.qrcode.service;

import politechnika.lodzka.qrcode.model.user.User;

public interface TokenService {
    String generateActivationToken(User user);
}
