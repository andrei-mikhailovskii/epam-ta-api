package com.epam.tc;

import static io.restassured.RestAssured.given;
import com.epam.tc.entities.BoardEntity;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.assertj.core.api.SoftAssertions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class BoardLifecycle extends AbstractTest {

    private static final String BASE_PATH = "/{id}";
    BoardEntity boardEntity = new BoardEntity();

    @Test(priority = 1)
    public void createBoard() {

        Response response = given()
                .spec(requestSpecPost)
                .when()
                .post(BOARDS_ENDPOINT);

        boardEntity = response
                .then()
                .extract().body().as(BoardEntity.class);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(response.header("Content-Type"))
                .as("Invalid content type").contains("charset=utf-8");
        softly.assertThat(response.statusCode()).as("Invalid status code").isEqualTo(STATUS_OK);
        softly.assertThat(boardEntity.getName()).as("Invalid board name").isEqualTo(BOARD_NAME);
        softly.assertAll();

    }

    @Test(priority = 2)
    public void getBoard() {

        boardEntity = given()
            .spec(requestSpecGet)
            .when()
            .basePath(BASE_PATH)
            .pathParam("id", boardEntity.getId())
            .get()
            .then()
            .statusCode(STATUS_OK)
            .extract().body().as(BoardEntity.class);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(boardEntity.getName()).as("Incorrect name")
                .isEqualTo(BOARD_NAME);
        softly.assertThat(boardEntity.getPinned()).as("Invalid boolean\"pinned\"").isEqualTo("false");
        softly.assertAll();

    }

    @Test(priority = 3)
    public void updateBoard() {

        Response response = given()
                .spec(requestSpecPut)
                .basePath(BASE_PATH)
                .pathParam("id", boardEntity.getId())
                .put();

        boardEntity = response
            .then()
            .statusCode(STATUS_OK)
            .extract().body().as(BoardEntity.class);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d MMM yyyy");
        LocalDateTime localDateTime = LocalDateTime.now();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(response.header("Date"))
                .as("Invalid date").contains(dateTimeFormatter.format(localDateTime));
        softly.assertThat(boardEntity.getName())
                .as("Board name is not updated!").isEqualTo(BOARD_NAME_UPDATED);
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
            .statusCode(STATUS_OK)
            .extract().body().as(BoardEntity.class);

        Assert.assertEquals(boardEntity.getName(), null);

    }

}
