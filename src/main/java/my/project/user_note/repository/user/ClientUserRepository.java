package my.project.user_note.repository.user;

import my.project.user_note.entity.user.ClientUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientUserRepository extends JpaRepository<ClientUser, UUID> {
    boolean existsByLogin(String login);
}
