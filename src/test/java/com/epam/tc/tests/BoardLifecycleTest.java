package com.epam.tc.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import com.epam.tc.entities.BoardEntity;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

public class BoardLifecycleTest extends BaseTest {

    public static final String BOARD_NAME = "testTrelloBoard";
    private static final String BOARD_NAME_UPDATED = "testTrelloBoardUpdated";
    BoardEntity boardEntity = new BoardEntity();

    @Test
    public void boardLifecycle() {

        Instant gmtTime = Instant.now();
        LocalDateTime localTimeSetToGmt = LocalDateTime.ofInstant(gmtTime, ZoneOffset.UTC);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d MMM yyyy");

        //create board
        boardEntity = given()
                .spec(baseRequestSpec)
                .when()
                .basePath(BOARDS_PATH)
                .queryParam("name", BOARD_NAME)
                .post()
                .then()
                .spec(baseResponseSpec)
                .body("name", equalTo(BOARD_NAME))
                .extract().body().as(BoardEntity.class);

        //get board
        given()
                .spec(baseRequestSpec)
                .when()
                .basePath(BOARD_ID)
                .pathParam("id", boardEntity.getId())
                .get()
                .then()
                .spec(baseResponseSpec)
                .body("name", equalTo(BOARD_NAME))
                .body("pinned", equalTo(false));

        //update board
        given()
                .spec(baseRequestSpec)
                .when()
                .basePath(BOARD_ID)
                .pathParam("id", boardEntity.getId())
                .queryParam("name", BOARD_NAME_UPDATED)
                .put()
                .then()
                .spec(baseResponseSpec)
                .header("Date", containsString(dateTimeFormatter.format(localTimeSetToGmt)))
                .body("id", equalTo(boardEntity.getId()))
                .body("name", equalTo(BOARD_NAME_UPDATED));

        //delete board
        given()
                .spec(baseRequestSpec)
                .when()
                .basePath(BOARD_ID)
                .pathParam("id", boardEntity.getId())
                .delete()
                .then()
                .spec(baseResponseSpec);

        //verify deletion
        given()
                .spec(baseRequestSpec)
                .when()
                .basePath(BOARD_ID)
                .pathParam("id", boardEntity.getId())
                .get()
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);

    }

}
