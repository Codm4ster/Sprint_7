package ru.yandex.scooter.courier;

import org.apache.commons.lang3.RandomStringUtils;

public class CreateCourier {
    private String login;
    private String password;
    private String firstName;

    public CreateCourier() {
    }

    public CreateCourier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public static CreateCourier random() {
        String login = RandomStringUtils.randomAlphabetic(4, 10);
        String password = RandomStringUtils.randomAlphabetic(4, 10);
        String firstName = RandomStringUtils.randomAlphabetic(4, 10);
        return new CreateCourier(login, password, firstName);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
