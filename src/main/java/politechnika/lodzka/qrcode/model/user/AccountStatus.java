package politechnika.lodzka.qrcode.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountStatus {
    ACTIVE(true),
    INACTIVE(false);

    private boolean isActive;
}
