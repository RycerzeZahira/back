package politechnika.lodzka.qrcode.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ResetPasswordRequest {
    @NotBlank
    private String password;

    @NotBlank
    private String confirmationPassword;

    @NotBlank
    private String token;
}
