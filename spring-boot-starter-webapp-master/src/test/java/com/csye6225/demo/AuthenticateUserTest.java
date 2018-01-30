package com.csye6225.demo;

import com.csye6225.demo.model.User;
import io.restassured.RestAssured;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class AuthenticateUserTest {
    @Ignore
    @Test
    public void checkLogin () {
        User user =new User();
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("username","joshu");
        jsonAsMap.put("password","123");

        given().contentType("application/json").
                body(jsonAsMap).
                when().
                get("/").then().statusCode(200).log().all();
    }


}
