package politechnika.lodzka.qrcode.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MailType {
    ACTIVATION("UserActivationMail"),
    LIST("UserListMail");

    private String mailType;
}
