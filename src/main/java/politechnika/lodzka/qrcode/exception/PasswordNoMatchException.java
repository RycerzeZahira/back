package politechnika.lodzka.qrcode.exception;

public class PasswordNoMatchException extends AbstractBadRequestException {
    public PasswordNoMatchException(String message) {
        super(message);
    }
}
