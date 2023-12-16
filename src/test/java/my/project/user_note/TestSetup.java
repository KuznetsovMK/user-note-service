package my.project.user_note;

import lombok.RequiredArgsConstructor;
import my.project.user_note.entity.user.ClientUser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TestSetup {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Transactional
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM CLIENT_USER_NOTE");
        jdbcTemplate.update("DELETE FROM CLIENT_USER");
        jdbcTemplate.update("DELETE FROM NOTE");
    }

    public void createUser() {
        var clientUser = new ClientUser();
        clientUser.setId(UUID.fromString("10000000-0000-0000-0000-000000000001"));
        clientUser.setLogin("testUserLogin");

        var sql = """
                INSERT INTO client_user (id, login)
                VALUES (:id, :login)
                """;

        var source = Arrays.stream(SqlParameterSourceUtils.createBatch(
                        List.of(clientUser).toArray()
                )).findFirst()
                .orElse(new EmptySqlParameterSource());
        namedParameterJdbcTemplate.update(sql, source);
    }
}
