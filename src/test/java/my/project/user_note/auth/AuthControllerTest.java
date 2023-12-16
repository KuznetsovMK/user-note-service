package my.project.user_note.auth;

import io.restassured.RestAssured;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import my.project.user_note.TestSetup;
import my.project.user_note.entity.user.ClientUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor
class AuthControllerTest {
    @LocalServerPort
    private int localServerPort;
    @Autowired
    private TestSetup testSetup;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final BeanPropertyRowMapper<ClientUser> CLIENT_USER_BEAN_PROPERTY_ROW_MAPPER = new BeanPropertyRowMapper<>(ClientUser.class);

    @BeforeEach
    void setUp() {
        testSetup.deleteAll();

        RestAssured.port = localServerPort;
        RestAssured.baseURI = "http://localhost";
    }

    @SneakyThrows
    @Test
    @DisplayName("Login client-user")
    void test1() {
        createClientUser();

        var userLogin = "clientUserLogin1";

        var request = Map.of(
                "login", userLogin
        );

        var response = given()
                .contentType("application/json")
                .log().all()
                .body(request)
                .when().post("/auth/login")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        JSONAssert.assertEquals("""
                {
                    "id": "10000000-0000-0000-0000-000000000001",
                    "login": "clientUserLogin1"
                }
                """, response, false);
    }

    @SneakyThrows
    @Test
    @DisplayName("Login client-user with invalid login")
    void test2() {
        createClientUser();

        var userLogin = "loginNotPresentInDb";

        var request = Map.of(
                "login", userLogin
        );

        var response = given()
                .contentType("application/json")
                .log().all()
                .body(request)
                .when().post("/auth/login")
                .then()
                .log().all()
                .statusCode(400)
                .extract()
                .body()
                .asString();

        JSONAssert.assertEquals("""
                {
                    "message": "Invalid login"
                }
                """, response, false);
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
}
