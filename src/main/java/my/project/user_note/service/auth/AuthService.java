package my.project.user_note.service.auth;

import com.model.ClientUserDto;
import com.model.LoginClientUserRequestDto;
import lombok.RequiredArgsConstructor;
import my.project.user_note.exception.BadFieldValueException;
import my.project.user_note.mapper.user.ClientUserMapper;
import my.project.user_note.repository.auth.AuthRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final ClientUserMapper clientUserMapper;

    public ClientUserDto loginClientUser(LoginClientUserRequestDto body) {
        return clientUserMapper.toDto(
                authRepository.findByLogin(body.getLogin())
                        .orElseThrow(() -> new BadFieldValueException("Invalid login"))
        );
    }
}

