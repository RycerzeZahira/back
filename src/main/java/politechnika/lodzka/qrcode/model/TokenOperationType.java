package politechnika.lodzka.qrcode.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TokenOperationType {
    ACTIVATION("activation"),
    PASSWORD_RESET("passwordReset");

    private String operationType;
}
