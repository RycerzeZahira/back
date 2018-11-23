package politechnika.lodzka.qrcode.exception;

import politechnika.lodzka.qrcode.model.OperationToken;

public class OperationTokenNotFoundException extends AbstractNotFoundException {
    public OperationTokenNotFoundException(String code) {
        super(OperationToken.class, code);
    }
}
