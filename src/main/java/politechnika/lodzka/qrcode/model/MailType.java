package politechnika.lodzka.qrcode.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MailType {
    ACTIVATION("UserTokenOperationMail"),
    LIST("UserListMail"),
    PASSWORD_RESET("UserTokenOperationMail");

    private String mailType;
}
