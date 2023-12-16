package my.project.user_note.repository.auth;

import my.project.user_note.entity.user.ClientUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthRepository extends JpaRepository<ClientUser, UUID> {
    Optional<ClientUser> findByLogin(String login);
}
