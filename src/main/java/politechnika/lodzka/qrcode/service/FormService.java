package politechnika.lodzka.qrcode.service;

import politechnika.lodzka.qrcode.model.Form;
import politechnika.lodzka.qrcode.model.request.CreateFormRequest;
import politechnika.lodzka.qrcode.model.request.scheme.SaveAnswersRequest;

public interface FormService {
    Form create(CreateFormRequest request);

    void saveAnswer(SaveAnswersRequest request);
}
