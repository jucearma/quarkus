package org.acme;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import org.hamcrest.CoreMatchers;

@QuarkusTest
@QuarkusTestResource(HelloWorldQuarkusTestResourceLifeCicleManager.class)
public class GreetingResourceTest {

    @Test
    public void testCalculate() {
        given()
          .when().get("/hello/calculate")
          .then()
             .statusCode(200)
             .body(CoreMatchers.is("100"));
    }

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/hello")
          .then()
             .statusCode(200)
             .body(CoreMatchers.is("Hello world"));
    }

}