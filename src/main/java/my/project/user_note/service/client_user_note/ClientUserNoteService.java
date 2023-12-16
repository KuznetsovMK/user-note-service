package my.project.user_note.service.client_user_note;

import com.model.CreateClientUserNoteRequestDto;
import com.model.NoteDto;
import com.model.UpdateClientUserNoteRequestDto;
import lombok.RequiredArgsConstructor;
import my.project.user_note.mapper.client_user_note.ClientUserNoteMapper;
import my.project.user_note.mapper.note.NoteMapper;
import my.project.user_note.repository.client_user_note.ClientUserNoteRepository;
import my.project.user_note.request.client_user_note.CreateClientUserNoteRequestV2;
import my.project.user_note.request.note.CreateNoteRequest;
import my.project.user_note.request.note.UpdateNoteRequest;
import my.project.user_note.service.note.NoteService;
import my.project.user_note.validator.client_user_note.ClientUserNoteValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientUserNoteService {
    private final ClientUserNoteValidator clientUserNoteValidator;
    private final NoteService noteService;
    private final ClientUserNoteRepository clientUserNoteRepository;
    private final ClientUserNoteMapper clientUserNoteMapper;
    private final NoteMapper noteMapper;

    @Transactional
    public void createNote(CreateClientUserNoteRequestDto request) {
        clientUserNoteValidator.validate(request);

        var noteId = UUID.randomUUID();

        noteService.createNote(CreateNoteRequest.builder()
                .id(noteId)
                .text(request.getText())
                .build());

        clientUserNoteRepository.save(clientUserNoteMapper.toEntity(CreateClientUserNoteRequestV2.builder()
                .id(UUID.randomUUID())
                .clientUserId(request.getClientUserId())
                .noteId(noteId)
                .build()));

    }

    public List<NoteDto> findAllMyNotes(UUID clientUserId, Integer limit, Integer offset) {
        return clientUserNoteRepository.findAllMyNotes(clientUserId, limit, offset).stream()
                .map(noteMapper::toDto)
                .toList();
    }

    public void updateNote(UpdateClientUserNoteRequestDto request) {
        clientUserNoteValidator.validate(request);

        noteService.updateNote(UpdateNoteRequest.builder()
                .id(request.getNoteId())
                .text(request.getText())
                .build());
    }

    public void deleteClientUserNote(UUID noteId) {
        clientUserNoteRepository.deleteByNoteId(noteId);
    }
}

