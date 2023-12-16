package my.project.user_note.request.client_user_note;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CreateClientUserNoteRequestV2 {
    private UUID id;
    private UUID clientUserId;
    private UUID noteId;
}
