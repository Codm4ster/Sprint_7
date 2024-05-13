package ru.yandex.scooter;

import io.qameta.allure.Description;
import ru.yandex.scooter.courier.CheckCourier;
import ru.yandex.scooter.courier.CreateCourier;
import ru.yandex.scooter.courier.LoginCourier;
import ru.yandex.scooter.courier.StepCourier;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

public class LoginCourierTest {
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
    @DisplayName("Успешная авторизация курьера")
    @Description("Проверка, что если в ручку передать все обязательные поля созданного курьера, можно авторизоваться")
    public void loggedInCourier() {
        var courier = CreateCourier.random();
        ValidatableResponse createResponse = client.createCourier(courier);
        check.createdSuccessfully(createResponse);

        var creds = LoginCourier.from(courier);
        ValidatableResponse loginResponse = client.loginCourier(creds);
        courierId = check.loggedInSuccessfully(loginResponse);

        assertNotEquals(0, courierId);
    }

    @Test
    @DisplayName("Авторизация без указания логина")
    @Description("Проверка, что если в ручку не передать поле логина, запрос возвращает ошибку")
    public void loggedInCourierWithoutLogin() {
        var courier = CreateCourier.random();
        client.createCourier(courier);

        var creds = LoginCourier.from(courier);
        creds.setLogin("");
        ValidatableResponse loginResponse = client.loginCourier(creds);
        check.loggedInWithoutParameter(loginResponse);
    }

    @Test
    @DisplayName("Авторизация без указания пароля")
    @Description("Проверка, что если в ручку не передать поле пароля, запрос возвращает ошибку")
    public void loggedInCourierWithoutPassword() {
        var courier = CreateCourier.random();
        client.createCourier(courier);

        var creds = LoginCourier.from(courier);
        creds.setPassword("");
        ValidatableResponse loginResponse = client.loginCourier(creds);
        check.loggedInWithoutParameter(loginResponse);
    }

    @Test
    @DisplayName("Авторизация под несуществующим пользователем")
    @Description("Проверка, что если в ручку передать несуществующую пару логин-пароль, запрос возвращает ошибку")
    public void loggedInWithNotCreatedCourier() {
        var courier = CreateCourier.random();

        var creds = LoginCourier.from(courier);
        ValidatableResponse loginResponse = client.loginCourier(creds);
        check.loggedInWithNotCreatedParameters(loginResponse);
    }
}


