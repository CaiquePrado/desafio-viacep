package dbserver.desafioviacep.desafioviacep.config;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;

public class BaseTest implements Constants {

  @BeforeEach
  public void setup() {
    RestAssured.baseURI = APP_BASE_URL;
    RestAssured.basePath = APP_BASE_PATH;

    RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
    requestSpecBuilder.setContentType(APP_CONTENT_TYPE);

    RestAssured.requestSpecification = requestSpecBuilder.build();

    ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
    responseSpecBuilder.expectResponseTime(Matchers.lessThan(MAX_TIME_OUT));

    RestAssured.responseSpecification = responseSpecBuilder.build();

    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
  }
}
