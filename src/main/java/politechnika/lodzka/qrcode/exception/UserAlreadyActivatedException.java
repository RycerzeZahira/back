package politechnika.lodzka.qrcode.exception;

public class UserAlreadyActivatedException extends AbstractBadRequestException {
    public UserAlreadyActivatedException(String message) {
        super(message);
    }
}
