package politechnika.lodzka.qrcode.service;

import org.springframework.security.core.Authentication;
import politechnika.lodzka.qrcode.model.request.AuthenticationRequest;
import politechnika.lodzka.qrcode.model.user.User;

public interface AuthService {
    Authentication auth(AuthenticationRequest authenticationRequest);

    String getAuthenticationToken(Authentication authentication);

    User getCurrentUser();

    boolean changePassword(String oldPassword, String newPassword);
}
