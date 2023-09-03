import org.example.CourierGenerator;
import org.example.CourierCredentials;
import org.example.CourierAction;
import io.restassured.response.ValidatableResponse;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.example.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class CourierNegativeTest {

    private CourierAction courierStep;
    private Courier courier;
    private Integer courierId;
    private Courier notExistCourier = CourierGenerator.getRandom();

    @Before
    public void setUp() {
        courierId = null;
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
    @DisplayName("Cоздание курьера без логина")
    @Description("Проверется невозможность создания курьера без логина")
    public void createCourierWithoutLoginTest(){
        courier.setLogin(null);
        ValidatableResponse createResponse = courierStep.create(courier);
        createResponse
                .statusCode(400)
                .assertThat()
                .body("message",equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Cоздание курьера без пароля")
    @Description("Ответ при создания курьера без указания пароля")
    public void createCourierWithoutPasswordTest(){
        courier.setPassword(null);
        ValidatableResponse createResponse = courierStep.create(courier);
        createResponse
                .statusCode(400)
                .assertThat()
                .body("message",equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Cоздание курьера с существующим логином")
    @Description("Нельзя создать курьеров с одинаковыми логинами")
    public void createDuplicateCourierTest(){
        ValidatableResponse createResponseFirst = courierStep.create(courier);
        ValidatableResponse createResponseSecond = courierStep.create(courier);
        ValidatableResponse loginResponse = courierStep.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");
        createResponseSecond
                .statusCode(409)
                .assertThat()
                .body("code",equalTo(409))
                .and()
                .body("message",equalTo("Этот логин уже используется"));
    }

    @Test
    @DisplayName("Авторизации без логина")
    @Description("Авторизации курьера в системе без логина")
    public void loginCourierWithoutLoginTest(){
        courierId = courierStep.login(CourierCredentials.from(courier)).extract().path("id");
        ValidatableResponse loginResponse = courierStep.login(CourierCredentials.from(courier.setLogin("")));
        loginResponse
                .statusCode(400)
                .assertThat()
                .body("code",equalTo(400))
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизации без пароля")
    @Description("Авторизации курьера в системе без пароля")
    public void loginCourierWithoutPasswordTest(){
        courierId = courierStep.login(CourierCredentials.from(courier)).extract().path("id");
        ValidatableResponse loginResponse = courierStep.login(CourierCredentials.from(courier.setPassword("")));
        loginResponse
                .statusCode(400)
                .assertThat()
                .body("code",equalTo(400))
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация курьера c некорректным логином")
    @Description("Авторизации курьера в системе c несущетвующем логином")
    public void loginWithIncorrectLoginTest() {
        courierId = courierStep.login(CourierCredentials.from(courier)).extract().path("id");
        ValidatableResponse loginResponse = courierStep.login(CourierCredentials.from(courier.setLogin("UNKNOWN_LOGIN")));
        loginResponse
                .statusCode(404)
                .assertThat()
                .body("code",equalTo(404))
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация курьера c некорректным паролем")
    @Description("Авторизации курьера в системе c неправельным паролем")
    public void loginWithIncorrectPasswordTest() {
        courierId = courierStep.login(CourierCredentials.from(courier)).extract().path("id");
        ValidatableResponse loginResponse = courierStep.login(CourierCredentials.from(courier.setPassword("UNKNOWN_PASSWORD")));
        loginResponse
                .statusCode(404)
                .assertThat()
                .body("code",equalTo(404))
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация несуществующего курьера")
    @Description("Авторизации курьера в системе с несуществующим логином и паролем")
    public void loginUnknownCourierTest() {
        courierId = courierStep.login(CourierCredentials.from(courier)).extract().path("id");
        ValidatableResponse loginResponse = courierStep.login(CourierCredentials.from(notExistCourier));
        loginResponse
                .statusCode(404)
                .assertThat()
                .body("code", equalTo(404))
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

}