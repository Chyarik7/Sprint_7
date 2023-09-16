package ru.practikum_services.qa_scooter.api.order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.practikum_services.qa_scooter.api.Client;

import static io.restassured.RestAssured.given;

public class OrderAction extends Client {
    private static final String ORDER_PATH = "/api/v1/orders";
    private static final String GET_LIST_OF_ORDERS_PATH = "/api/v1/orders";

    @Step("Создание заказа")
    public ValidatableResponse createOrder(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Получение списка заказов")
    public ValidatableResponse getListOrders() {
        return given()
                .spec(getBaseSpec())
                .get(GET_LIST_OF_ORDERS_PATH)
                .then();
    }
}