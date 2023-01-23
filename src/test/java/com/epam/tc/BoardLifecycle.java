package com.epam.tc;

import static io.restassured.RestAssured.given;
import com.epam.tc.entities.BoardEntity;
import org.testng.annotations.Test;
import org.assertj.core.api.SoftAssertions;


public class BoardLifecycle extends AbstractTest {

    private static String boardUrl = BOARDS_ENDPOINT + getBoardId();

    public static String getBoardId() {
        BoardEntity boardEntity = new BoardEntity();
        return boardEntity.getId();
    }

    @Test(groups = "post", priority = 1)
    public void createBoard() {

        given()
                .spec(requestSpec)
                .when()
                .post(BOARDS_ENDPOINT)
                .then()
                .statusCode(200);

    }

    @Test(groups = "get", priority = 2)
    public void getBoard() {

        BoardEntity response = given()
            .spec(requestSpec)
            .when()
            .get(boardUrl)
            .then()
            .extract().body().as(BoardEntity.class);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(response.getName()).as("Incorrect name")
                .isEqualTo("testBoard");
        softly.assertAll();

    }

}
