package politechnika.lodzka.qrcode.model.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class SendResetPasswordEmailRequest {
    @Email
    @NotBlank
    private String email;
}
