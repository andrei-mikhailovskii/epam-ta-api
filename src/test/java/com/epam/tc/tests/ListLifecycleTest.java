package com.epam.tc.tests;

import static org.hamcrest.Matchers.equalTo;

import com.epam.tc.entities.BoardEntity;
import com.epam.tc.entities.ListEntity;
import com.epam.tc.serviceobjects.Board;
import com.epam.tc.serviceobjects.Lists;
import org.apache.http.HttpStatus;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ListLifecycleTest extends BaseTest {

    private static final String BOARD_NAME = "testTrelloBoard";
    private static final String LIST_NAME = "testList";
    private String listClosed = "true";
    private boolean expectedListClosedStatus = true;

    ListEntity listEntity = new ListEntity();

    @BeforeClass()
    private void createBoard() {
        boardEntity = Board.createNewBoard(BOARD_NAME).getBody().as(BoardEntity.class);
    }

    @Test(priority = 1)
    public void createList() {

        listEntity = Lists.createList(LIST_NAME, boardEntity.getId())
                .then()
                .spec(baseResponseSpec)
                .body(Lists.listBodyKeyName, equalTo(LIST_NAME))
                .body(Lists.listBodyKeyIdBoard, equalTo(boardEntity.getId()))
                .extract().body().as(ListEntity.class);
    }

    @Test(priority = 2)
    public void archiveList() {

        Lists.archiveList(listEntity.getId(), listClosed)
                .then()
                .spec(baseResponseSpec)
                .body(Lists.listBodyKeyClosed, equalTo(expectedListClosedStatus));

    }

    @AfterClass
    private void deleteCreatedBoard() {
        Board.deleteBoard(boardEntity.getId()).then().statusCode(HttpStatus.SC_OK);
    }

}
