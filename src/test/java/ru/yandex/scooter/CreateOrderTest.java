package ru.yandex.scooter;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.scooter.order.CheckOrder;
import ru.yandex.scooter.order.CreateOrder;
import ru.yandex.scooter.order.StepOrder;

import java.util.List;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private List<String> color;

    public CreateOrderTest(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, List<String> color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    private final StepOrder order = new StepOrder();
    private final CheckOrder check = new CheckOrder();

    @Parameterized.Parameters
    public static Object[][] getOrder() {
        return new Object[][]{
                {"Александр", "Пушкин", "Бабаевская, 7", "10", "+79998885522", 1, "2024-05-25", "Позвоните мне", List.of("BLACK")},
                {"Лев", "Толстой", "Арбат, 40", "77", "+78882226677", 2, "2024-05-26", "Только наличные", List.of("GREY")},
                {"Николай", "Гоголь", "Студенческая, 38", "72", "+79895551122", 3, "2024-05-27", "Только банковская карта", List.of()},
                {"Антон", "Чехов", "Усачева, 25", "15", "+74568883322", 4, "2024-05-28", "Хочу кататься", List.of("BLACK", "GREY")},
        };
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Проверка создания заказов с разными цветами самоката")
    public void createOrderTest() {
        CreateOrder createOrder = new CreateOrder(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        ValidatableResponse createResponse = order.createOrder(createOrder);
        check.checkCreatedOrderSuccessfully(createResponse);
    }
}
