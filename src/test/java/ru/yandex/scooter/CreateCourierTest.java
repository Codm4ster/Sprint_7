package ru.yandex.scooter;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import ru.yandex.scooter.courier.CheckCourier;
import ru.yandex.scooter.courier.CreateCourier;
import ru.yandex.scooter.courier.LoginCourier;
import ru.yandex.scooter.courier.StepCourier;


public class CreateCourierTest {
    private final StepCourier client = new StepCourier();
    private final CheckCourier check = new CheckCourier();
    int courierId;

    @After
    public void deleteCourier() {
        if (courierId != 0) {
            ValidatableResponse response = client.deleteCourier(courierId);
            check.deletedSuccessfully(response);
        }
    }

    @Test
    @DisplayName("Курьера можно создать")
    @Description("Проверка, что если в ручку передать все обязательные поля, то курьера можно создать")
    public void createCourier() {
        var courier = CreateCourier.random();
        ValidatableResponse createResponse = client.createCourier(courier);
        check.createdSuccessfully(createResponse);

        var creds = LoginCourier.from(courier);
        ValidatableResponse loginResponse = client.loginCourier(creds);
        courierId = check.loggedInSuccessfully(loginResponse);
    }

    @Test
    @DisplayName("Создание одинакового курьера")
    @Description("Проверка, что если создать пользователя с логином, который уже есть, возвращается ошибка")
    public void createDuplicateCourier() {
        var courier = CreateCourier.random();
        client.createCourier(courier);
        ValidatableResponse createDuplicateResponse = client.createCourier(courier);
        check.createdWithRepeatLogin(createDuplicateResponse);

        var creds = LoginCourier.from(courier);
        ValidatableResponse loginResponse = client.loginCourier(creds);
        courierId = check.loggedInSuccessfully(loginResponse);
    }

    @Test
    @DisplayName("Создание курьера без указания логина")
    @Description("Проверка, что если создать пользователя без указания логина, запрос возвращает ошибку")
    public void createCourierWithoutLogin() {
        var courier = CreateCourier.random();
        courier.setLogin("");
        ValidatableResponse createResponse = client.createCourier(courier);
        check.createdWithoutParameter(createResponse);
    }

    @Test
    @DisplayName("Создание курьера без указания пароля")
    @Description("Проверка, что если создать пользователя без указания пароля, запрос возвращает ошибку")
    public void createCourierWithoutPassword() {
        var courier = CreateCourier.random();
        courier.setPassword("");
        ValidatableResponse createResponse = client.createCourier(courier);
        check.createdWithoutParameter(createResponse);
    }
}

