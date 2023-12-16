package my.project.user_note.controller.auth;

import com.api.AuthApi;
import com.model.ClientUserDto;
import com.model.LoginClientUserRequestDto;
import lombok.RequiredArgsConstructor;
import my.project.user_note.service.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    private final AuthService authService;

    @Override
    public ResponseEntity<ClientUserDto> loginClientUser(LoginClientUserRequestDto body) {
        return ResponseEntity.ok(authService.loginClientUser(body));
    }
}

