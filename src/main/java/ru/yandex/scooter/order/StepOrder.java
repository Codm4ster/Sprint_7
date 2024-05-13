package ru.yandex.scooter.order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.scooter.EnvConfig;

public class StepOrder extends EnvConfig {
    private static final String COURIER_PATH = "/orders";

    @Step("Создание заказа")
    public ValidatableResponse createOrder(CreateOrder order) {
        return spec()
                .body(order)
                .when()
                .post(COURIER_PATH)
                .then().log().all();
    }

    @Step("Получение списка заказов")
    public ValidatableResponse getListOrder() {
        return spec()
                .when()
                .get(COURIER_PATH)
                .then().log().all();
    }
}
