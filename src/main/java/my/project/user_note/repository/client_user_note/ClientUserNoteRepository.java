package my.project.user_note.repository.client_user_note;

import my.project.user_note.entity.client_user_note.ClientUserNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientUserNoteRepository extends JpaRepository<ClientUserNote, UUID>, CustomizedClientUserNoteRepository {
    Optional<ClientUserNote> findByClientUserIdAndNoteId(UUID clientUserId, UUID noteId);

    void deleteByNoteId(UUID noteId);
}
