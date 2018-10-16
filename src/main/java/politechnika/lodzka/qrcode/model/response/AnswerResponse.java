package politechnika.lodzka.qrcode.model.response;

import lombok.Data;
import politechnika.lodzka.qrcode.model.scheme.SchemeInformation;

@Data
public class AnswerResponse extends SchemeInformation {
    private Object value;
}
