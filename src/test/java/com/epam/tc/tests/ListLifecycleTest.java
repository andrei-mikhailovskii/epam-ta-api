package com.epam.tc.tests;

import static io.restassured.RestAssured.given;

import com.epam.tc.entities.ListEntity;
import org.assertj.core.api.SoftAssertions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ListLifecycleTest extends AbstractTest {

    private static final String ARCHIVE_LIST_PATH = "/{id}/closed";
    ListEntity listEntity = new ListEntity();

    @BeforeClass()
    private void createBoard() {
        boardEntity = createNewBoard();
    }

    @Test(priority = 1, groups = "list")
    public void createList() {

        listEntity = given()
            .spec(requestSpecListPost)
            .when()
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

    }

    @AfterClass(groups = {"list"})
    private void deleteCreatedBoard() {
        deleteCreatedBoard(boardEntity.getId());
    }

}
