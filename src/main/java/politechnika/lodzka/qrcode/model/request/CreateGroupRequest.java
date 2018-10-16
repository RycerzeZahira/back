package politechnika.lodzka.qrcode.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateGroupRequest {
    @NotBlank
    private String name;
}
