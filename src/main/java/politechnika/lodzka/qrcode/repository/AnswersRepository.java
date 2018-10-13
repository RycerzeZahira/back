package politechnika.lodzka.qrcode.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import politechnika.lodzka.qrcode.model.scheme.Answer;

import java.util.Collection;

public interface AnswersRepository extends JpaRepository<Answer, Long> {
    Collection<Answer> findAnswerByFormCode(String code);
}
