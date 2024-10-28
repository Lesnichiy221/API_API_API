package ru.netlogi;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class AuthTest {
    @BeforeAll
    static void setUpAll() {
        // Создаем активного пользователя
        RegistrationDto activeUser = UserGenerator.generateUser("active");
        UserGenerator.createUser(activeUser);

        // Создаем заблокированного пользователя
        RegistrationDto blockedUser = UserGenerator.generateUser("blocked");
        UserGenerator.createUser(blockedUser);
    }

    @Test
    void shouldLoginWithActiveUser() {
        RegistrationDto activeUser = UserGenerator.generateUser("active");
        UserGenerator.createUser(activeUser);

        given()
                .spec(UserGenerator.requestSpec)
                .body(activeUser)
                .when()
                .post("/api/system/login")
                .then()
                .statusCode(200)
                .body("status", equalTo("success"));
    }

    @Test
    void shouldNotLoginWithBlockedUser() {
        RegistrationDto blockedUser = UserGenerator.generateUser("blocked");
        UserGenerator.createUser(blockedUser);

        given()
                .spec(UserGenerator.requestSpec)
                .body(blockedUser)
                .when()
                .post("/api/system/login")
                .then()
                .statusCode(403);
    }

    @Test
    void shouldNotLoginWithInvalidLogin() {
        RegistrationDto activeUser = UserGenerator.generateUser("active");
        UserGenerator.createUser(activeUser);
        activeUser.setLogin("invalidLogin");

        given()
                .spec(UserGenerator.requestSpec)
                .body(activeUser)
                .when()
                .post("/api/system/login")
                .then()
                .statusCode(401);
    }

    @Test
    void shouldNotLoginWithInvalidPassword() {
        RegistrationDto activeUser = UserGenerator.generateUser("active");
        UserGenerator.createUser(activeUser);
        activeUser.setPassword("invalidPassword");

        given()
                .spec(UserGenerator.requestSpec)
                .body(activeUser)
                .when()
                .post("/api/system/login")
                .then()
                .statusCode(401);
    }
}

