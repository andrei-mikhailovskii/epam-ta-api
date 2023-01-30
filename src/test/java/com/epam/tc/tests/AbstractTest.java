package com.epam.tc.tests;

import static io.restassured.RestAssured.given;

import com.epam.tc.PropertiesExtractor;
import com.epam.tc.entities.BoardEntity;
import com.epam.tc.entities.ListEntity;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class AbstractTest {
    protected static final String BOARDS_ENDPOINT = "https://api.trello.com/1/boards";
    protected static final String BOARD_NAME = "testTrelloBoard";
    protected static final String BOARD_NAME_UPDATED = "testTrelloBoardUpdated";
    protected static final String LISTS_ENDPOINT = "https://api.trello.com/1/lists";
    protected static final String LIST_NAME = "testList";
    protected static final String CARDS_ENDPOINT = "https://api.trello.com/1/cards";
    protected static final String CARD_NAME = "testCard";

    protected static final int STATUS_OK = 200;
    protected static final int STATUS_NOT_FOUND = 404;
    BoardEntity boardEntity = new BoardEntity();
    ListEntity listEntity = new ListEntity();
    protected RequestSpecification requestSpecPost;
    protected RequestSpecification requestSpecGet;
    protected RequestSpecification requestSpecPut;
    protected RequestSpecification requestSpecListPost;
    protected RequestSpecification requestSpecListPut;
    protected RequestSpecification requestSpecCardPost;

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

    @BeforeClass(groups = "cards")
    public void cardSetup() {
        RestAssured.baseURI = CARDS_ENDPOINT;

        requestSpecCardPost = new RequestSpecBuilder()
                .addQueryParam("name", CARD_NAME)
                .addQueryParam("key", PropertiesExtractor.getKey())
                .addQueryParam("token", PropertiesExtractor.getToken())
                .setContentType(ContentType.JSON)
                .build();
    }

    @BeforeClass(enabled = false)
    protected BoardEntity createNewBoard() {
        return given()
                .spec(requestSpecPost)
                .when()
                .post(BOARDS_ENDPOINT)
                .then()
                .extract().body().as(BoardEntity.class);
    }

    @BeforeClass(enabled = false)
    protected ListEntity createNewList(String boardId) {
        return given()
                .spec(requestSpecListPost)
                .when()
                .queryParam("idBoard", boardId)
                .post(LISTS_ENDPOINT)
                .then()
                .extract().body().as(ListEntity.class);
    }

    @AfterClass(enabled = false)
    protected void deleteCreatedBoard(String boardId) {
        given()
            .spec(requestSpecGet)
            .when()
            .basePath("/{id}")
            .pathParam("id", boardId)
            .delete()
            .then()
            .statusCode(STATUS_OK);
    }

}
