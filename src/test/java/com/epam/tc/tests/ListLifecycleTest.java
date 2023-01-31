package com.epam.tc.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import com.epam.tc.entities.ListEntity;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ListLifecycleTest extends BaseTest {

    private static final String ARCHIVE_LIST_PATH = "/1/lists/{id}/closed";
    public static final String LIST_NAME = "testList";
    private String listClosed = "true";
    private boolean expectedListClosedStatus = true;
    ListEntity listEntity = new ListEntity();

    @BeforeClass()
    private void createBoard() {
        boardEntity = createNewBoard(BoardLifecycleTest.BOARD_NAME);
    }

    @Test(priority = 1)
    public void createList() {

        listEntity = given()
                .spec(baseRequestSpec)
                .when()
                .basePath(LISTS_PATH)
                .queryParam("name", LIST_NAME)
                .queryParam("idBoard", boardEntity.getId())
                .post()
                .then()
                .spec(baseResponseSpec)
                .body("name", equalTo(LIST_NAME))
                .body("idBoard", equalTo(boardEntity.getId()))
                .extract().body().as(ListEntity.class);
    }

    @Test(priority = 2)
    public void archiveList() {

        given()
                .spec(baseRequestSpec)
                .when()
                .basePath(ARCHIVE_LIST_PATH)
                .pathParam("id", listEntity.getId())
                .queryParam("value", listClosed)
                .put()
                .then()
                .spec(baseResponseSpec)
                .body("closed", equalTo(expectedListClosedStatus));

    }

    @AfterClass
    private void deleteCreatedBoard() {
        deleteCreatedBoard(boardEntity.getId());
    }

}
