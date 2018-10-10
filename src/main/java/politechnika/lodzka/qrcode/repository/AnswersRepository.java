package politechnika.lodzka.qrcode.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import politechnika.lodzka.qrcode.model.scheme.Answer;

public interface AnswersRepository extends JpaRepository<Answer, Long> {
}
