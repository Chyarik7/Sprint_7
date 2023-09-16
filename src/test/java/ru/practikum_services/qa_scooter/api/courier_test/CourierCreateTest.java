package ru.practikum_services.qa_scooter.api.courier_test;

import io.restassured.response.ValidatableResponse;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import ru.practikum_services.qa_scooter.api.courier.Courier;
import ru.practikum_services.qa_scooter.api.courier.CourierGenerator;
import ru.practikum_services.qa_scooter.api.courier.CourierCredentials;
import ru.practikum_services.qa_scooter.api.courier.CourierAction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class CourierCreateTest {

    private CourierAction courierStep;
    private Courier courier;
    private Integer courierId;

    @Before
    public void setUp() {
        courierStep = new CourierAction();
        courier = CourierGenerator.getRandom();
    }

    @After
    public void cleanUp() {
        if (courierId != null) {
            courierStep.deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Cоздание курьера с валидными данными")
    @Description("Проверяется, что курьера можно создать")
    public void courierCanBeCreatedTest(){
        ValidatableResponse createResponse = courierStep.create(courier);
        ValidatableResponse loginResponse = courierStep.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");
        createResponse
                .statusCode(201)
                .assertThat()
                .body("ok", is(true));
    }

}