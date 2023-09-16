package ru.practikum_services.qa_scooter.api.order_test;

import ru.practikum_services.qa_scooter.api.order.OrderAction;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderGetList {

    private OrderAction orderStep;

    @Before
    public void setUp(){
        orderStep = new OrderAction();
    }

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверяется, что запрос возвращает список заказов")
    public void getListOrdersTest() {
        ValidatableResponse response = orderStep.getListOrders();
        response
                .assertThat()
                .body("orders", notNullValue())
                .and()
                .statusCode(200);
    }
}