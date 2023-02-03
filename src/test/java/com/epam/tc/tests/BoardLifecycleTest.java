package com.epam.tc.tests;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import com.epam.tc.entities.BoardEntity;
import com.epam.tc.serviceobjects.Board;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

public class BoardLifecycleTest extends BaseTest {

    private static final String BOARD_NAME = "testTrelloBoard";
    private static final String BOARD_NAME_UPDATED = "testTrelloBoardUpdated";

    @Test
    public void boardLifecycle() {

        Instant gmtTime = Instant.now();
        LocalDateTime localTimeSetToGmt = LocalDateTime.ofInstant(gmtTime, ZoneOffset.UTC);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d MMM yyyy");

        //create board
        boardEntity = Board.createNewBoard(BOARD_NAME)
                .then()
                .spec(baseResponseSpec)
                .body(Board.boardBodyKeyName, equalTo(BOARD_NAME))
                .extract().body().as(BoardEntity.class);

        //get board
        Board.getBoard(boardEntity.getId())
                .then()
                .spec(baseResponseSpec)
                .body(Board.boardBodyKeyName, equalTo(BOARD_NAME))
                .body(Board.boardBodyKeyPinned, equalTo(false));

        //update board
        Board.updateBoard(boardEntity.getId(), BOARD_NAME_UPDATED)
                .then()
                .spec(baseResponseSpec)
                .header(Board.boardHeaderKeyDate, containsString(dateTimeFormatter.format(localTimeSetToGmt)))
                .body(Board.boardBodyKeyId, equalTo(boardEntity.getId()))
                .body(Board.boardBodyKeyName, equalTo(BOARD_NAME_UPDATED));

        //delete board
        Board.deleteBoard(boardEntity.getId())
                .then()
                .spec(baseResponseSpec);

        //verify deletion
        Board.getBoard(boardEntity.getId())
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);

    }

}
