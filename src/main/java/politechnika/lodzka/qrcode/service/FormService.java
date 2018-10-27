package politechnika.lodzka.qrcode.service;

import politechnika.lodzka.qrcode.model.Form;
import politechnika.lodzka.qrcode.model.request.CloneFormRequest;
import politechnika.lodzka.qrcode.model.request.CreateFormRequest;
import politechnika.lodzka.qrcode.model.request.UpdateFormRequest;
import politechnika.lodzka.qrcode.model.request.scheme.SaveAnswersRequest;
import politechnika.lodzka.qrcode.model.response.AnswerResponse;

import java.util.Collection;

public interface FormService {
    Form create(CreateFormRequest request);

    void saveAnswer(SaveAnswersRequest request);

    Collection<AnswerResponse> getAnswers(String formCode);

    Form findByCode(String code);

    void clone(CloneFormRequest cloneFormRequest);

    void update(UpdateFormRequest request);

    Form findFormByCode(String code);it
}
