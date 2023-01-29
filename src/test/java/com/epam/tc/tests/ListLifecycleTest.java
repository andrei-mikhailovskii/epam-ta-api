package com.epam.tc.tests;

import static io.restassured.RestAssured.given;

import com.epam.tc.entities.BoardEntity;
import com.epam.tc.entities.ListEntity;
import org.assertj.core.api.SoftAssertions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ListLifecycleTest extends AbstractTest {

    private static final String ARCHIVE_LIST_PATH = "/{id}/closed";
    BoardEntity boardEntity = new BoardEntity();
    ListEntity listEntity = new ListEntity();

    @Test(priority = 1, groups = "list")
    public void createList() {
        boardEntity = given()
            .spec(requestSpecPost)
            .when()
            .post(BOARDS_ENDPOINT)
            .then()
            .extract().body().as(BoardEntity.class);

        listEntity = given()
            .spec(requestSpecListPost)
            .when()
            //.pathParam("name", LIST_NAME)
            .queryParam("idBoard", boardEntity.getId())
            .post(LISTS_ENDPOINT)
            .then()
            .statusCode(STATUS_OK)
            .extract().body().as(ListEntity.class);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(listEntity.getName()).as("Invalid list name").isEqualTo(LIST_NAME);
        softly.assertThat(listEntity.getIdBoard()).as("Board id mismatch").isEqualTo(boardEntity.getId());
        softly.assertAll();
    }

    @Test(priority = 2, groups = "list")
    public void archiveList() {

        listEntity = given()
            .spec(requestSpecListPut)
            .when()
            .basePath(ARCHIVE_LIST_PATH)
            .pathParam("id", listEntity.getId())
            .queryParam("value", "true")
            .put()
            .then()
            .statusCode(STATUS_OK)
            .extract().body().as(ListEntity.class);

        Assert.assertEquals(listEntity.getClosed(), "true");

        given()
            .spec(requestSpecGet)
            .when()
            .basePath("/{id}")
            .pathParam("id", boardEntity.getId())
            .delete()
            .then()
            .statusCode(STATUS_OK);

    }

}
