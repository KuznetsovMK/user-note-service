package my.project.user_note.repository.client_user_note;

import lombok.RequiredArgsConstructor;
import my.project.user_note.entity.note.Note;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CustomizedClientUserNoteRepositoryImpl implements CustomizedClientUserNoteRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final BeanPropertyRowMapper<Note> NOTE_BEAN_PROPERTY_ROW_MAPPER = new BeanPropertyRowMapper<>(Note.class);

    @Override
    public List<Note> findAllMyNotes(UUID clientUserId, Integer limit, Integer offset) {
        var sql = """
                SELECT note.id, note.text
                FROM client_user_note
                         JOIN note ON client_user_note.note_id = note.id
                WHERE client_user_id = :clientUserId
                ORDER BY note.created_at desc
                LIMIT :limit OFFSET :offset;
                """;

        var parameters = new HashMap<String, Object>();
        parameters.put("clientUserId", clientUserId);
        parameters.put("limit", limit);
        parameters.put("offset", offset);

        return namedParameterJdbcTemplate.query(sql, parameters, NOTE_BEAN_PROPERTY_ROW_MAPPER);
    }
}
