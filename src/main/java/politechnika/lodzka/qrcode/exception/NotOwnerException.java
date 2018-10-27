package politechnika.lodzka.qrcode.exception;

import politechnika.lodzka.qrcode.model.Group;
import politechnika.lodzka.qrcode.model.user.User;

public class NotOwnerException extends AppException {
    public NotOwnerException(User moderator, String group) {
        super(new StringBuilder()
                .append("User: ")
                .append(moderator.getEmail())
                .append(" is not owner of the group: ")
                .append(group).toString());
    }
}
