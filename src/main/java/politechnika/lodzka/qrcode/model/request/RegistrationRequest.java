package politechnika.lodzka.qrcode.model.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RegistrationRequest {
    @NotNull
    @NotEmpty
    @Size(min = 2, max = 255)
    private String firstName;

    @NotNull
    @NotEmpty
    @Size(min = 2, max = 255)
    private String lastName;

    @NotNull
    @NotEmpty
    @Email
    private String email;

    @NotNull
    @NotEmpty
    @Size(min = 8, max = 255)
    private String password;
}
