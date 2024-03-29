package my.project.user_note.entity.client_user_note;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "CLIENT_USER_NOTE")
public class ClientUserNote {
    @Id
    private UUID id;
    private UUID clientUserId;
    private UUID noteId;
}
