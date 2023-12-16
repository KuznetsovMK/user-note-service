package my.project.user_note.validator.client_user_note;

import com.model.CreateClientUserNoteRequestDto;
import com.model.UpdateClientUserNoteRequestDto;
import lombok.RequiredArgsConstructor;
import my.project.user_note.entity.client_user_note.ClientUserNote;
import my.project.user_note.exception.NotFoundException;
import my.project.user_note.repository.client_user_note.ClientUserNoteRepository;
import my.project.user_note.validator.user.ClientUserValidator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientUserNoteValidator {
    private final ClientUserValidator clientUserValidator;
    private final ClientUserNoteRepository clientUserNoteRepository;

    public void validate(CreateClientUserNoteRequestDto request) {
        clientUserValidator.validateClientUserId(request.getClientUserId());
    }

    public void validate(UpdateClientUserNoteRequestDto request) {
        clientUserValidator.validateClientUserId(request.getClientUserId());

        if (clientUserNoteRepository.findByClientUserIdAndNoteId(
                request.getClientUserId(), request.getNoteId()).isEmpty()
        ) {
            throw new NotFoundException("Entity %s with fields clientUserId: %s and noteId: %s not found".formatted(
                    ClientUserNote.class.getSimpleName(), request.getClientUserId(), request.getNoteId()
            ));
        }
    }
}
