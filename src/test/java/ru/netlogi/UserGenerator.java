package ru.netlogi;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;

public class UserGenerator {
    private static final Faker faker = new Faker();

    // Делаем requestSpec публичной и статичной, чтобы её можно было использовать в тестах
    public static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(io.restassured.filter.log.LogDetail.ALL)
            .build();

    public static RegistrationDto generateUser(String status) {
        return new RegistrationDto(
                faker.name().firstName(),
                faker.internet().password(),
                status
        );
    }

    public static void createUser(RegistrationDto user) {
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }
}
