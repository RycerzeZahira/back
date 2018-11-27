package politechnika.lodzka.qrcode.exception;

public class UserAlreadyInGroupException extends AbstractBadRequestException {
    public UserAlreadyInGroupException(String message) {
        super(message);
    }
}
