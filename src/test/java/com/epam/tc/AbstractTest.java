package com.epam.tc;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

public class AbstractTest {

    public static final String BOARDS_ENDPOINT = "https://api.trello.com/1/boards/";

    protected RequestSpecification requestSpec;

    @BeforeTest(groups = "post")
    public void postBoardSetup() {
        String name = System.getenv("boardName");
        String key = System.getenv("key");
        String token = System.getenv("token");
        requestSpec = new RequestSpecBuilder()
                .addQueryParam("name", name)
                .addQueryParam("key", key)
                .addQueryParam("token", token)
                .setContentType(ContentType.JSON)
                .build();
    }

    @BeforeTest(groups = "get")
    public void getBoardSetup() {
        String key = System.getenv("key");
        String token = System.getenv("token");
        requestSpec = new RequestSpecBuilder()
                .addQueryParam("key", key)
                .addQueryParam("token", token)
                .build();
    }

}
