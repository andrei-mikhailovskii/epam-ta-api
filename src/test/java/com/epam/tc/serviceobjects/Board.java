package com.epam.tc.serviceobjects;

import static io.restassured.RestAssured.given;

import com.epam.tc.tests.BaseTest;
import io.restassured.response.Response;

public class Board {

    private static final String BOARDS_PATH = "/1/boards";
    private static final String BOARD_ID = "/1/boards/{id}";

    //path parameters
    public static String pathParamId = "id";
    public static String pathParamName = "name";

    //query parameters
    public static String queryParamName = "name";

    //board header keys
    public static String boardHeaderKeyDate = "Date";

    //board body keys
    public static String boardBodyKeyId = "id";
    public static String boardBodyKeyName = "name";
    public static String boardBodyKeyPinned = "pinned";

    public static Response createNewBoard(String boardName) {
        return given()
                .spec(BaseTest.baseRequestSpec)
                .basePath(BOARDS_PATH)
                .when()
                .queryParam(pathParamName, boardName)
                .post()
                .then()
                .extract().response();
    }

    public static Response getBoard(String boardId) {
        return given()
                .spec(BaseTest.baseRequestSpec)
                .when()
                .basePath(BOARD_ID)
                .pathParam(pathParamId, boardId)
                .get()
                .then()
                .extract().response();
    }

    public static Response updateBoard(String boardId, String newBoardName) {
        return given()
                .spec(BaseTest.baseRequestSpec)
                .when()
                .basePath(BOARD_ID)
                .pathParam(pathParamId, boardId)
                .queryParam(queryParamName, newBoardName)
                .put()
                .then()
                .extract().response();
    }

    public static Response deleteBoard(String boardId) {
        return given()
                .spec(BaseTest.baseRequestSpec)
                .when()
                .basePath(BOARD_ID)
                .pathParam(pathParamId, boardId)
                .delete()
                .then()
                .extract().response();
    }

}
