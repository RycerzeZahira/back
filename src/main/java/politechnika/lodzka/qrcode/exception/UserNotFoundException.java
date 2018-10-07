package politechnika.lodzka.qrcode.exception;

public class UserNotFoundException extends AbstractNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
