package my.project.user_note.client_user_note;

import io.restassured.RestAssured;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import my.project.user_note.TestSetup;
import my.project.user_note.entity.client_user_note.ClientUserNote;
import my.project.user_note.entity.note.Note;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor
class ClientUserNoteControllerCreateTest {
    @LocalServerPort
    private int localServerPort;
    @Autowired
    private TestSetup testSetup;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final BeanPropertyRowMapper<ClientUserNote> CLIENT_USER_NOTE_BEAN_PROPERTY_ROW_MAPPER = new BeanPropertyRowMapper<>(ClientUserNote.class);
    private static final BeanPropertyRowMapper<Note> NOTE_BEAN_PROPERTY_ROW_MAPPER = new BeanPropertyRowMapper<>(Note.class);

    @BeforeEach
    void setUp() {
        testSetup.deleteAll();

        RestAssured.port = localServerPort;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    @DisplayName("Create new note")
    void test1() {
        testSetup.createUser();

        var userId = UUID.fromString("10000000-0000-0000-0000-000000000001");
        var text = """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean venenatis enim vitae purus sodales vestibulum. Suspendisse laoreet augue et sem dignissim laoreet. Donec efficitur ultrices.
                """;

        var request = Map.of(
                "clientUserId", userId,
                "text", text
        );

        given()
                .contentType("application/json")
                .log().all()
                .body(request)
                .when().post("/client-user-note/create")
                .then()
                .log().all()
                .statusCode(200);

        var sql = """
                SELECT *
                FROM client_user_note
                         LEFT JOIN note ON client_user_note.note_id = note.id
                WHERE client_user_id = '10000000-0000-0000-0000-000000000001';
                """;

        var clientUserNote = namedParameterJdbcTemplate.queryForObject(
                sql, Map.of(), CLIENT_USER_NOTE_BEAN_PROPERTY_ROW_MAPPER);

        assertNotNull(clientUserNote);
        assertEquals(clientUserNote.getClientUserId(), userId);

        var note = namedParameterJdbcTemplate.queryForObject(
                sql, Map.of(), NOTE_BEAN_PROPERTY_ROW_MAPPER);

        assertNotNull(note);
        assertEquals(note.getText(), text);
    }

    @SneakyThrows
    @Test
    @DisplayName("Create new note, clientUserId not found")
    void test2() {
        testSetup.createUser();
        var userId = UUID.fromString("10000000-0000-0000-0000-000000000000");
        var text = """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean venenatis enim vitae purus sodales vestibulum. Suspendisse laoreet augue et sem dignissim laoreet. Donec efficitur ultrices.
                """;

        var request = Map.of(
                "clientUserId", userId,
                "text", text
        );

        var response = given()
                .contentType("application/json")
                .log().all()
                .body(request)
                .when().post("/client-user-note/create")
                .then()
                .log().all()
                .statusCode(404)
                .extract()
                .body()
                .asString();

        JSONAssert.assertEquals("""
                {
                    "message": "Entity ClientUser with id: 10000000-0000-0000-0000-000000000000 does not exists"
                }
                """, response, false);
    }

    @SneakyThrows
    @Test
    @DisplayName("Create new note, exceeded 5000 character limit")
    void test3() {
        testSetup.createUser();

        var userId = UUID.fromString("10000000-0000-0000-0000-000000000001");
        var text = "A".repeat(5001);

        var request = Map.of(
                "clientUserId", userId,
                "text", text
        );

        var response = given()
                .contentType("application/json")
                .log().all()
                .body(request)
                .when().post("/client-user-note/create")
                .then()
                .log().all()
                .statusCode(400)
                .extract()
                .body()
                .asString();

        JSONAssert.assertEquals("""
                {
                    "message": "Exceeded 5000 character limit"
                }
                """, response, false);
    }
}
