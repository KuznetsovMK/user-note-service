package my.project.user_note.entity.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "CLIENT_USER")
public class ClientUser {
    @Id
    private UUID id;
    private String login;
}
