package politechnika.lodzka.qrcode.exception;

public class UserAlreadyExistsException extends AbstractBadRequestException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
