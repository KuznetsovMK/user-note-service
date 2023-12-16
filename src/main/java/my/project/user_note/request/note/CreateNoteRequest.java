package my.project.user_note.request.note;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CreateNoteRequest {
    private UUID id;
    private String text;
}
