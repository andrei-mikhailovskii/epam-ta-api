package com.epam.tc;

import static io.restassured.RestAssured.given;
import com.epam.tc.entities.BoardEntity;
import org.testng.annotations.Test;
import org.assertj.core.api.SoftAssertions;


public class BoardLifecycle extends AbstractTest {

    BoardEntity boardEntity = new BoardEntity();

    @Test(priority = 1)
    public void createBoard() {

        boardEntity = given()
                .spec(requestSpecPost)
                .when()
                .post(BOARDS_ENDPOINT)
                .then()
                .statusCode(200)
                .extract().body().as(BoardEntity.class);

    }

    @Test(priority = 2)
    public void getBoard() {

        boardEntity = given()
            .spec(requestSpecGet)
            .when()
            .basePath("/{id}")
            .pathParam("id", boardEntity.getId())
            .get()
            .then()
            .statusCode(200)
            .extract().body().as(BoardEntity.class);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(boardEntity.getName()).as("Incorrect name")
                .isEqualTo("testTrelloBoard");
        softly.assertAll();

    }


    @Test(priority = 4)
    public void deleteBoard() {

        boardEntity = given()
            .spec(requestSpecGet)
            .when()
            .basePath("/{id}")
            .pathParam("id", boardEntity.getId())
            .delete()
            .then()
            .statusCode(200)
            .extract().body().as(BoardEntity.class);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(boardEntity.getName())
                .as("Board name after deletion is not null!").isEqualTo(null);
        softly.assertAll();

    }

}
