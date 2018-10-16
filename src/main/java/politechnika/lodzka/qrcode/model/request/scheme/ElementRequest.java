package politechnika.lodzka.qrcode.model.request.scheme;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

@Data
@JsonDeserialize(using = ElementRequestDeserialize.class)
public class ElementRequest extends SchemeInformationRequest {
    private Object element;
}
