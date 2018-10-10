package politechnika.lodzka.qrcode.exception;

import politechnika.lodzka.qrcode.model.User;

public class NoPermissionException extends AppException {
    public NoPermissionException(User user) {
        super(new StringBuilder().append("User: ").append(user.getEmail()).append(" has no permission").toString());
    }
}
