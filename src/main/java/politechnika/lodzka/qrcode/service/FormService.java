package politechnika.lodzka.qrcode.service;

import politechnika.lodzka.qrcode.model.Form;
import politechnika.lodzka.qrcode.model.request.CreateFormRequest;
import politechnika.lodzka.qrcode.model.request.scheme.SaveAnswersRequest;
import politechnika.lodzka.qrcode.model.response.AnswerResponse;
import politechnika.lodzka.qrcode.model.scheme.Answer;

import java.util.Collection;

public interface FormService {
    Form create(CreateFormRequest request);

    void saveAnswer(SaveAnswersRequest request);

    Collection<AnswerResponse> getAnswers(String formCode);
}
