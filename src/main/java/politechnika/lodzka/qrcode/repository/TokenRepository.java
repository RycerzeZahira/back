package politechnika.lodzka.qrcode.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import politechnika.lodzka.qrcode.model.ActivationToken;
import politechnika.lodzka.qrcode.model.user.User;

public interface TokenRepository extends JpaRepository<ActivationToken, Long> {
    @Query("SELECT at.user FROM ActivationToken at WHERE at.token = :token")
    User getUserIdByActivationToken(@Param("token") String token);

    void deleteActivationTokenByToken(String token);

    ActivationToken findByUserEmail(String email);
}
