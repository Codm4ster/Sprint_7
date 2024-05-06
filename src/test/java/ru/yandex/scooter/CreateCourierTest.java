import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.scooter.courier.CreateCourier;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateCourierTest {

    private String login = RandomStringUtils.randomAlphabetic(10);

    private String password = RandomStringUtils.randomNumeric(10);

    private String firstName = RandomStringUtils.randomAlphabetic(10);

    private String courierId;

    @Before
    public void setUp() {
        RestAssured.baseURI= "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Создание нового курьера")
    public void createNewCourier() {
        CreateCourier createCourier = new CreateCourier(login, password, firstName);
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(createCourier)
                        .when()
                        .post("/api/v1/courier");
        response.then().statusCode(201).and().assertThat().body("ok", equalTo(true));
        courierId = response.jsonPath().getString("id");
    }

    @Test
    @DisplayName("Создание двух одинаковых курьеров")
    public void createDuplicateCourier() {
        String json = "{\"login\": \"ninja\", \"password\": \"1234\", \"firstName\": \"saske\"}";;
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then().statusCode(409).and().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Создание нового курьера без указания логина")
    public void createCourierWithoutLogin() {
        String json = "{\"password\": \"1234\", \"firstName\": \"Алексей\"}";;
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then().statusCode(400).and().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание нового курьера без указания пароля")
    public void createCourierWithoutPassword() {
        String json = "{\"login\": \"TestCourier\", \"firstName\": \"Алексей\"}";;
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then().statusCode(400).and().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @After
    public void tearDown() {
        if (courierId != null) {
            Response response =
                    given()
                            .when()
                            .delete("/api/v1/courier/" + courierId);
            response.then().statusCode(200).and().assertThat().body("ok", equalTo(true));
        }
    }
}
