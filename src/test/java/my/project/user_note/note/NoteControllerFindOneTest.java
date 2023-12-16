package my.project.user_note.note;

import io.restassured.RestAssured;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import my.project.user_note.TestSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor
class NoteControllerFindOneTest {
    @LocalServerPort
    private int localServerPort;
    @Autowired
    private TestSetup testSetup;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @BeforeEach
    void setUp() {
        testSetup.deleteAll();

        RestAssured.port = localServerPort;
        RestAssured.baseURI = "http://localhost";
    }

    @SneakyThrows
    @Test
    @DisplayName("Find note by id")
    void test1() {
        createNote();

        var noteId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        var response = given()
                .contentType("application/json")
                .log().all()
                .when().get("/note/find-one/{id}", noteId)
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        JSONAssert.assertEquals("""
                {
                    "id": "00000000-0000-0000-0000-000000000001",
                    "text": "text_1"
                }
                """, response, false);
    }

    @SneakyThrows
    @Test
    @DisplayName("Note not found")
    void test2() {
        createNote();

        var noteId = UUID.fromString("10000000-0000-0000-0000-000000000001");

        var response = given()
                .contentType("application/json")
                .log().all()
                .when().get("/note/find-one/{id}", noteId)
                .then()
                .log().all()
                .statusCode(404)
                .extract()
                .body()
                .asString();

        JSONAssert.assertEquals("""
                {
                    "message": "Entity Note with id: 10000000-0000-0000-0000-000000000001 not found"
                }
                """, response, false);
    }

    private void createNote() {
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
    }
}
