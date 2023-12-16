package my.project.user_note.user;

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
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor
class ClientUserControllerCreateTest {
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

    @Test
    @DisplayName("Create new user")
    void test1() {
        var userId = UUID.fromString("a9db8d00-cde4-4ad0-98dc-c1008ac4cd95");
        var userLogin = "testUserLogin";

        var request = Map.of(
                "id", userId,
                "login", userLogin
        );

        given()
                .contentType("application/json")
                .log().all()
                .body(request)
                .when().post("/client-user/create")
                .then()
                .log().all()
                .statusCode(200);

        var sql = """
                SELECT *
                FROM client_user
                WHERE id = 'a9db8d00-cde4-4ad0-98dc-c1008ac4cd95'
                """;

        var clientUsers = namedParameterJdbcTemplate.queryForObject(
                sql, Map.of(), CLIENT_USER_BEAN_PROPERTY_ROW_MAPPER);

        assertNotNull(clientUsers);
        assertEquals(clientUsers.getId(), userId);
        assertEquals(clientUsers.getLogin(), userLogin);
    }

    @SneakyThrows
    @Test
    @DisplayName("Create new user with login already exists")
    void test2() {
        createData();

        var userId = UUID.fromString("a9db8d00-cde4-4ad0-98dc-c1008ac4cd95");
        var userLogin = "testUserLogin";

        var request = Map.of(
                "id", userId,
                "login", userLogin
        );

        var response = given()
                .contentType("application/json")
                .log().all()
                .body(request)
                .when().post("/client-user/create")
                .then()
                .log().all()
                .statusCode(400)
                .extract()
                .body()
                .asString();

        JSONAssert.assertEquals("""
                {
                    "message": "Entity ClientUser with login: testUserLogin already exists"
                }
                """, response, false);
    }

    private void createData() {
        var clientUser = new ClientUser();
        clientUser.setId(UUID.fromString("95753e20-25f7-4bfc-925e-b5631b6b92ac"));
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
