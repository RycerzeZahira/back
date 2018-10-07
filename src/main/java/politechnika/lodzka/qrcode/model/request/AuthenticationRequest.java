package politechnika.lodzka.qrcode.model.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class AuthenticationRequest {
    @Email
    private String login;
    @Size(min = 8, max = 32)
    private String password;
}
