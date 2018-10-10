package politechnika.lodzka.qrcode.model.request.scheme;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SaveAnswersRequest {
    @NotBlank
    private String formCode;
    @NotNull
    private ElementRequest root;
}
