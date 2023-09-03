import io.restassured.response.ValidatableResponse;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.example.Courier;
import org.example.CourierAction;
import org.example.CourierCredentials;
import org.example.CourierGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

public class CourierLoginTest {

    private CourierAction courierStep;
    private Courier courier;
    private Integer courierId;

    @Before
    public void setUp() {
        courierStep = new CourierAction();
        courier = CourierGenerator.getRandom();
        courierStep.create(courier);
    }

    @After
    public void cleanUp() {
        if (courierId != null) {
            courierStep.deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Успешная авторизация")
    @Description("Проверяется авторизации с валидными данными")
    public void successLoginCourierTest(){
        ValidatableResponse loginResponse = courierStep.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");
        loginResponse
                .statusCode(200)
                .assertThat()
                .body("id",is(notNullValue()));
    }

}