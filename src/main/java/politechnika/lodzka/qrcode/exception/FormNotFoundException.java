package politechnika.lodzka.qrcode.exception;

import politechnika.lodzka.qrcode.model.Form;

public class FormNotFoundException extends AbstractNotFoundException {
    public FormNotFoundException(String code) {
        super(Form.class, code);
    }
}
