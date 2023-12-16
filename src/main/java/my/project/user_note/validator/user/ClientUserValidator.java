package my.project.user_note.validator.user;

import com.model.CreateClientUserRequestDto;
import lombok.RequiredArgsConstructor;
import my.project.user_note.entity.user.ClientUser;
import my.project.user_note.exception.AlreadyExistsException;
import my.project.user_note.exception.NotFoundException;
import my.project.user_note.repository.user.ClientUserRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ClientUserValidator {
    private final ClientUserRepository clientUserRepository;

    public void validate(CreateClientUserRequestDto request) {
        if (clientUserRepository.existsByLogin(request.getLogin())) {
            throw new AlreadyExistsException("Entity %s with login: %s already exists".formatted(
                    ClientUser.class.getSimpleName(), request.getLogin()));
        }
    }

    public void validateClientUserId(UUID clientUserId) {
        if (!clientUserRepository.existsById(clientUserId)) {
            throw new NotFoundException("Entity %s with id: %s does not exists".formatted(
                    ClientUser.class.getSimpleName(), clientUserId));
        }
    }
}
