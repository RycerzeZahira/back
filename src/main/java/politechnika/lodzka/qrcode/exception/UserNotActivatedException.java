package politechnika.lodzka.qrcode.exception;

public class UserNotActivatedException extends AbstractBadRequestException {
    public UserNotActivatedException(String message) {
        super(message);
    }
}
