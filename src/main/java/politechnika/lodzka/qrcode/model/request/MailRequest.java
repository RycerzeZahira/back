package politechnika.lodzka.qrcode.model.request;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotEmpty;

@Data
public class MailRequest {
    private String receiver;

    @NonNull
    @NotEmpty
    private String content;
}
