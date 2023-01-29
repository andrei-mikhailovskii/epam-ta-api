package com.epam.tc.tests;

import com.epam.tc.PropertiesExtractor;
import com.epam.tc.entities.BoardEntity;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

public class AbstractTest {
    public static final String BOARDS_ENDPOINT = "https://api.trello.com/1/boards";
    public static final String BOARD_NAME = "testTrelloBoard";
    public static final String BOARD_NAME_UPDATED = "testTrelloBoardUpdated";
    public static final String LISTS_ENDPOINT = "https://api.trello.com/1/lists";
    public static final String LIST_NAME = "testList";

    public static final int STATUS_OK = 200;

    protected RequestSpecification requestSpecPost;
    protected RequestSpecification requestSpecGet;
    protected RequestSpecification requestSpecPut;
    protected RequestSpecification requestSpecListPost;
    protected RequestSpecification requestSpecListPut;

    @BeforeClass(groups = "board")
    public void boardSetup() {
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

        requestSpecPut = new RequestSpecBuilder()
                .addQueryParam("name", BOARD_NAME_UPDATED)
                .addQueryParam("key", PropertiesExtractor.getKey())
                .addQueryParam("token", PropertiesExtractor.getToken())
                .setContentType(ContentType.JSON)
                .build();
    }

    @BeforeClass(groups = "list")
    public void listSetup() {
        RestAssured.baseURI = LISTS_ENDPOINT;

        requestSpecListPost = new RequestSpecBuilder()
                .addQueryParam("name", LIST_NAME)
                .addQueryParam("key", PropertiesExtractor.getKey())
                .addQueryParam("token", PropertiesExtractor.getToken())
                .setContentType(ContentType.JSON)
                .build();

        requestSpecListPut = new RequestSpecBuilder()
                .addQueryParam("key", PropertiesExtractor.getKey())
                .addQueryParam("token", PropertiesExtractor.getToken())
                .setContentType(ContentType.JSON)
                .build();
    }

}
