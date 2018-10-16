package politechnika.lodzka.qrcode.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import politechnika.lodzka.qrcode.model.Group;
import politechnika.lodzka.qrcode.model.User;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByCode(String code);
    Collection<Group> findByModeratorOrUsersContaining(User moderator, User me);
}
