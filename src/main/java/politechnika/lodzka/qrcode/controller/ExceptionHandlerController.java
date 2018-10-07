package politechnika.lodzka.qrcode.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import politechnika.lodzka.qrcode.exception.AbstractBadRequestException;
import politechnika.lodzka.qrcode.exception.AbstractNotFoundException;
import politechnika.lodzka.qrcode.model.exception.ExceptionJSONInfo;

@ControllerAdvice
public class ExceptionHandlerController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AbstractBadRequestException.class)
    public @ResponseBody
    ExceptionJSONInfo handleBadRequestException(Exception ex) {
        ExceptionJSONInfo exceptionJSONInfo = new ExceptionJSONInfo();
        exceptionJSONInfo.setMessage(ex.getMessage());

        return exceptionJSONInfo;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AbstractNotFoundException.class)
    public @ResponseBody
    ExceptionJSONInfo handleNotFoundException(Exception ex) {
        ExceptionJSONInfo exceptionJSONInfo = new ExceptionJSONInfo();
        exceptionJSONInfo.setMessage(ex.getMessage());

        return exceptionJSONInfo;
    }
}
