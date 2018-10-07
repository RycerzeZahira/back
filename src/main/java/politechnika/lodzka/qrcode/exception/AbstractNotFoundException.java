package politechnika.lodzka.qrcode.exception;

public class AbstractNotFoundException extends AppException {
    public AbstractNotFoundException(String message) {
        super(message);
    }

    public AbstractNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
