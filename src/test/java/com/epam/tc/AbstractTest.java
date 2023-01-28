package com.epam.tc;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

public class AbstractTest {
    public static final String BOARDS_ENDPOINT = "https://api.trello.com/1/boards";
    public static final String BOARD_NAME = "testTrelloBoard";
    public static final String UPDATED_BOARD_NAME = "testTrelloBoardUpdated";

    protected RequestSpecification requestSpecPost;
    protected RequestSpecification requestSpecGet;

    @BeforeClass()
    public void postBoardSetup() {
        RestAssured.baseURI = BOARDS_ENDPOINT;

        requestSpecPost = new RequestSpecBuilder()
                .addQueryParam("name", BOARD_NAME)
                .addQueryParam("key", PropertiesExtractor.getKey())
                .addQueryParam("token", PropertiesExtractor.getToken())
                .setContentType(ContentType.JSON)
                .build();

        requestSpecGet = new RequestSpecBuilder()
                .addQueryParam("key", PropertiesExtractor.getKey())
                .addQueryParam("token", PropertiesExtractor.getToken())
                .build();
    }

}
