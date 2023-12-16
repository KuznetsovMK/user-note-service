package my.project.user_note.repository.client_user_note;

import my.project.user_note.entity.note.Note;

import java.util.List;
import java.util.UUID;

public interface CustomizedClientUserNoteRepository {

    List<Note> findAllMyNotes(UUID clientUserId, Integer limit, Integer offset);
}
