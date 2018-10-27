package politechnika.lodzka.qrcode.exception;

import politechnika.lodzka.qrcode.model.Form;

public class FormExpiredException extends AppException {
    public FormExpiredException(Form form) {
        super(new StringBuilder()
                .append("Form: ")
                .append(form.getCode())
                .append(" expired: ")
                .append(form.getExpiredDate())
                .toString());
    }
}
