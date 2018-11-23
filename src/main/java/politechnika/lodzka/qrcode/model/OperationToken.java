package politechnika.lodzka.qrcode.model;

import lombok.Data;
import politechnika.lodzka.qrcode.model.user.User;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Entity
@Data
public class OperationToken extends BaseEntity {
    public static final int EXPIRATION = 60 * 24;

    private String token;

    private Date expiryDate = calculateExpiryDate();

    private Date calculateExpiryDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Timestamp(calendar.getTime().getTime()));
        calendar.add(Calendar.MINUTE, OperationToken.EXPIRATION);

        return new Date(calendar.getTime().getTime());
    }

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
}
