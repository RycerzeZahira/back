package politechnika.lodzka.qrcode.exception;

import politechnika.lodzka.qrcode.model.user.User;

public class UserNotFoundException extends AbstractNotFoundException {

    public UserNotFoundException(String code) {
        super(User.class, code);
    }
}
