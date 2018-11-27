package politechnika.lodzka.qrcode.exception;

public class NotPublicGroupException extends AbstractBadRequestException {
    public NotPublicGroupException(String message) {
        super(message);
    }
}
