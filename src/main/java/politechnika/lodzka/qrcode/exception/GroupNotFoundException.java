package politechnika.lodzka.qrcode.exception;

import politechnika.lodzka.qrcode.model.Group;

public class GroupNotFoundException extends AbstractNotFoundException {
    public GroupNotFoundException(String message) {
        super(Group.class, message);
    }
}
