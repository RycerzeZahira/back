package politechnika.lodzka.qrcode.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import politechnika.lodzka.qrcode.model.Group;
import politechnika.lodzka.qrcode.model.user.User;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Collection<Group> findGroupByPublicGroupIsTrueAndUsersIsNotContainingAndModeratorIsNot(User user, User moderator);

    Optional<Group> findByCode(String code);

    Collection<Group> findByModeratorOrUsersContaining(User moderator, User me);
}
