package politechnika.lodzka.qrcode.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import politechnika.lodzka.qrcode.model.scheme.SchemeGroup;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class CreateFormRequest {
    @NotNull
    private String groupCode;
    @NotNull
    private SchemeGroup root;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date expiredDate;
}
