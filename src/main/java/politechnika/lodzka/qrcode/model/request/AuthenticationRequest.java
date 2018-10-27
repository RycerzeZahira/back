package politechnika.lodzka.qrcode.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @Email
    private String login;
    @Size(min = 8, max = 32)
    private String password;
}
