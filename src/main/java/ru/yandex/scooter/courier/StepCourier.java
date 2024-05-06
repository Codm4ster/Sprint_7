package ru.yandex.scooter.courier;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.scooter.EnvConfig;

import java.util.Map;
public class StepCourier extends EnvConfig {
    private static final String COURIER_PATH = "/courier";

    @Step("Создание курьера")
    public ValidatableResponse createCourier(CreateCourier courier) {
        return spec()
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then().log().all();
    }

    @Step("Авторизация курьера")
    public ValidatableResponse loginCourier(LoginCourier creds) {
        return spec()
                .body(creds)
                .when()
                .post(COURIER_PATH + "/login")
                .then().log().all();
    }

    @Step("Удаление курьера")
    public ValidatableResponse deleteCourier(int id) {
        return spec()
                .body(Map.of("id", id))
                .when()
                .delete(COURIER_PATH + "/" + id)
                .then().log().all();
    }

}
