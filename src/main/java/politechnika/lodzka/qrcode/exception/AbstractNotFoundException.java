package politechnika.lodzka.qrcode.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AbstractNotFoundException extends AppException {
    public AbstractNotFoundException(Class clazz, String code) {
        super(new StringBuilder(clazz.getSimpleName()).append(" code: ").append(code).append(" not found exception").toString());
    }
}
