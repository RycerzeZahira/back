package politechnika.lodzka.qrcode.model.request.scheme;

import lombok.Data;
import politechnika.lodzka.qrcode.model.scheme.TypeClass;

@Data
public class SchemeInformationRequest {
    private String code;
    private TypeClass type;
}
