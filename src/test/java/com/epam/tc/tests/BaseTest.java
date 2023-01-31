package com.epam.tc.tests;

import static io.restassured.RestAssured.given;

import com.epam.tc.PropertiesExtractor;
import com.epam.tc.entities.BoardEntity;
import com.epam.tc.entities.ListEntity;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class BaseTest extends PropertiesExtractor {

    protected static final String BASE_ENDPOINT = "https://api.trello.com";
    protected static final String BOARDS_PATH = "/1/boards";
    protected static final String BOARD_ID = "/1/boards/{id}";
    protected static final String LISTS_PATH = "/1/lists";
    BoardEntity boardEntity = new BoardEntity();
    ListEntity listEntity = new ListEntity();

    protected RequestSpecification baseRequestSpec;
    protected ResponseSpecification baseResponseSpec;

    @BeforeClass
    public void baseSetup() {
        RestAssured.baseURI = BASE_ENDPOINT;

        PropertiesExtractor.getProperties();

        baseRequestSpec = new RequestSpecBuilder()
                .addQueryParam("key", apiKey)
                .addQueryParam("token", apiToken)
                .setContentType(ContentType.JSON)
                .build();

        baseResponseSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(HttpStatus.SC_OK)
                .expectHeader("Content-Type", "application/json; charset=utf-8")
                .build();
    }

    @AfterClass
    public void teardown() {
        RestAssured.reset();
        apiKey = null;
        apiToken = null;
    }

    protected BoardEntity createNewBoard(String boardName) {
        return given()
                .spec(baseRequestSpec)
                .basePath(BOARDS_PATH)
                .when()
                .queryParam("name", boardName)
                .post()
                .then()
                .extract().body().as(BoardEntity.class);
    }

    protected void deleteCreatedBoard(String boardId) {
        given()
            .spec(baseRequestSpec)
            .when()
            .basePath(BOARD_ID)
            .pathParam("id", boardId)
            .delete()
            .then()
            .statusCode(HttpStatus.SC_OK);
    }

}
