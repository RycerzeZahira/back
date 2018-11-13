package politechnika.lodzka.qrcode.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import politechnika.lodzka.qrcode.model.OperationToken;
import politechnika.lodzka.qrcode.model.user.User;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<OperationToken, Long> {
    @Query("SELECT at.user FROM OperationToken at WHERE at.token = :token")
    Optional<User> getUserByActivationToken(@Param("token") String token);

    void deleteActivationTokenByToken(String token);

    OperationToken findByUserEmail(String email);

    Optional<OperationToken> findByToken(String token);
}
