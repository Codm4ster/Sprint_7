import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.scooter.courier.CreateCourier;
import ru.yandex.scooter.courier.LoginCourier;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;


public class LoginCourierTest {

    private String courierId;

    @Before
    public void setUp() {
        RestAssured.baseURI= "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Успешная авторизация курьера")
    public void checkLoginCourierSuccessfully() {
        CreateCourier createCourier = new CreateCourier("Sprint", "12345", "Test");
        given()
                .header("Content-type", "application/json")
                .and()
                .body(createCourier)
                .when()
                .post("/api/v1/courier");

        LoginCourier loginCourier = new LoginCourier("Sprint", "12345");
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(loginCourier)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().statusCode(200).and().assertThat().body("id", notNullValue());
        courierId = response.jsonPath().getString("id");
    }

    @Test
    @DisplayName("Авторизация курьера без указания логина")
    public void checkLoginCourierWithoutLogin() {
        LoginCourier loginCourier = new LoginCourier("", "1234");
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(loginCourier)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().statusCode(400).and().assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация курьера без указания пароля")
    public void checkLoginCourierWithoutPassword() {
        LoginCourier loginCourier = new LoginCourier("ninja", "");
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(loginCourier)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().statusCode(400).and().assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация курьера с несуществующей парой логин-пароль")
    public void checkLoginCourierNotExisted() {
        String json = "{\"login\": \"Test12345\", \"password\": \"54321\"}";;
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().statusCode(404).and().assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @After
    public void tearDown() {
        if (courierId != null) {
            Response response =
                    given()
                            .when()
                            .delete("/api/v1/courier/" + courierId);
        }
    }
}

