package politechnika.lodzka.qrcode.service;

import politechnika.lodzka.qrcode.model.Language;
import politechnika.lodzka.qrcode.model.request.RegistrationRequest;

public interface RegistrationService {
    void registerUser(final RegistrationRequest registrationRequest, final Language language);
    void activateUser(final String token);
}
