package politechnika.lodzka.qrcode.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateGroupRequest {
    @NotBlank
    private String name;
    @NotNull
    private Boolean publicGroup;
}
