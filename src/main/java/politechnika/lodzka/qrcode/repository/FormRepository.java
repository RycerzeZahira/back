package politechnika.lodzka.qrcode.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import politechnika.lodzka.qrcode.model.Form;
import politechnika.lodzka.qrcode.model.Group;
import politechnika.lodzka.qrcode.model.user.User;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface FormRepository extends JpaRepository<Form, Long> {
    Optional<Form> findByCode(String code);
    Collection<Form> findByGroupCode(String code);
    Collection<Form> findByGroup_UsersContaining(User user);
}
