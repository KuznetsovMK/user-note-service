package my.project.user_note.client_user_note;

import io.restassured.RestAssured;
import lombok.RequiredArgsConstructor;
import my.project.user_note.TestSetup;
import my.project.user_note.entity.note.Note;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor
class ClientUserNoteControllerUpdateTest {
    @LocalServerPort
    private int localServerPort;
    @Autowired
    private TestSetup testSetup;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final BeanPropertyRowMapper<Note> NOTE_BEAN_PROPERTY_ROW_MAPPER = new BeanPropertyRowMapper<>(Note.class);

    @BeforeEach
    void setUp() {
        testSetup.deleteAll();

        RestAssured.port = localServerPort;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    @DisplayName("Update client user note")
    void test1() {
        createClientUser();
        createClientUserNote();

        var userId = UUID.fromString("10000000-0000-0000-0000-000000000001");
        var noteId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        var text = """
                note after update
                """;

        var request = Map.of(
                "clientUserId", userId,
                "noteId", noteId,
                "text", text
        );

        given()
                .contentType("application/json")
                .log().all()
                .body(request)
                .when().put("/client-user-note/update")
                .then()
                .log().all()
                .statusCode(200);

        var sql = """
                SELECT *
                FROM note
                WHERE id = '%s';
                """.formatted(noteId);

        var note = namedParameterJdbcTemplate.queryForObject(
                sql, Map.of(), NOTE_BEAN_PROPERTY_ROW_MAPPER);

        assertNotNull(note);
        assertEquals(note.getId(), noteId);
        assertEquals(note.getText(), text);
    }

    private void createClientUser() {
        var clientUser1 = Map.of(
                "id", UUID.fromString("10000000-0000-0000-0000-000000000001"),
                "login", "clientUserLogin1"
        );

        var clientUser2 = Map.of(
                "id", UUID.fromString("00000000-0000-0000-0000-000000000001"),
                "login", "clientUserLogin2"
        );

        var sql = """
                INSERT INTO client_user (id, login)
                VALUES (:id, :login)
                """;

        var source = SqlParameterSourceUtils.createBatch(
                List.of(clientUser1, clientUser2));
        namedParameterJdbcTemplate.batchUpdate(sql, source);
    }

    private void createClientUserNote() {
        var note1 = Map.of(
                "id", UUID.fromString("00000000-0000-0000-0000-000000000001"),
                "text", "text_1"
        );

        var note2 = Map.of(
                "id", UUID.fromString("00000000-0000-0000-0000-000000000002"),
                "text", "text_2"
        );

        var note3 = Map.of(
                "id", UUID.fromString("00000000-0000-0000-0000-000000000003"),
                "text", "text_3"
        );

        var sqlNote = """
                INSERT INTO note (id, text)
                VALUES (:id, :text)
                """;

        var sourceNote = SqlParameterSourceUtils.createBatch(List.of(note1, note2, note3));
        namedParameterJdbcTemplate.batchUpdate(sqlNote, sourceNote);

        var clientUserNote1 = Map.of(
                "id", UUID.fromString("00000000-0000-0000-0000-000000000001"),
                "clientUserId", UUID.fromString("10000000-0000-0000-0000-000000000001"),
                "noteId", UUID.fromString("00000000-0000-0000-0000-000000000001")
        );

        var clientUserNote2 = Map.of(
                "id", UUID.fromString("00000000-0000-0000-0000-000000000002"),
                "clientUserId", UUID.fromString("10000000-0000-0000-0000-000000000001"),
                "noteId", UUID.fromString("00000000-0000-0000-0000-000000000002")
        );

        var clientUserNote3 = Map.of(
                "id", UUID.fromString("00000000-0000-0000-0000-000000000003"),
                "clientUserId", UUID.fromString("00000000-0000-0000-0000-000000000001"),
                "noteId", UUID.fromString("00000000-0000-0000-0000-000000000003")
        );

        var sqlClientUserNote = """
                INSERT INTO client_user_note (id, client_user_id, note_id)
                values (:id, :clientUserId, :noteId);
                """;

        var sourceClientUserNote = SqlParameterSourceUtils.createBatch(
                List.of(clientUserNote1, clientUserNote2, clientUserNote3));
        namedParameterJdbcTemplate.batchUpdate(sqlClientUserNote, sourceClientUserNote);
    }
}
