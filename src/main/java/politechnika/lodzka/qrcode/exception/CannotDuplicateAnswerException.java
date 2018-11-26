package politechnika.lodzka.qrcode.exception;

import politechnika.lodzka.qrcode.model.Form;
import politechnika.lodzka.qrcode.model.user.User;

public class CannotDuplicateAnswerException extends AbstractBadRequestException {
    public CannotDuplicateAnswerException(User user, Form form) {
        super(new StringBuilder()
                .append("The user: ")
                .append(user.getEmail())
                .append(" has already given the answer for form: ")
                .append(form.getCode())
                .toString());
    }
}
