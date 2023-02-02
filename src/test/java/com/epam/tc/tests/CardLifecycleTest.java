package com.epam.tc.tests;

import static org.hamcrest.Matchers.equalTo;

import com.epam.tc.entities.BoardEntity;
import com.epam.tc.entities.CardEntity;
import com.epam.tc.entities.ListEntity;
import com.epam.tc.serviceobjects.Board;
import com.epam.tc.serviceobjects.Card;
import com.epam.tc.serviceobjects.Lists;
import org.apache.http.HttpStatus;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CardLifecycleTest extends BaseTest {

    private static final String BOARD_NAME = "testTrelloBoard";
    private static final String LIST_NAME = "testList";
    private static final String CARD_NAME = "testCard";
    private static final String ATTACHMENT_NAME = "testAttachment";
    private String attachmentUrl = "https://venturebeat.com/wp-content/uploads/2015/12/oracle-java-e1450723340931.jpg";
    private String expectedMimeType = "image/jpeg";
    private boolean expectedIsUploadValue = true;
    private String expectedFileName = "oracle-java-e1450723340931.jpg";
    CardEntity cardEntity = new CardEntity();

    @BeforeClass
    private void createBoardAndList() {
        boardEntity = Board.createNewBoard(BOARD_NAME).getBody().as(BoardEntity.class);;
        listEntity = Lists.createList(LIST_NAME, boardEntity.getId()).getBody().as(ListEntity.class);
    }

    @Test(priority = 1)
    public void createCard() {

        cardEntity = Card.createCard(listEntity.getId(), CARD_NAME)
                .then()
                .spec(baseResponseSpec)
                .body(Card.cardBodyKeyIdBoard, equalTo(boardEntity.getId()))
                .body(Card.cardBodyKeyIdList, equalTo(listEntity.getId()))
                .body(Card.cardBodyKeyName, equalTo(CARD_NAME))
                .extract().body().as(CardEntity.class);

    }

    @Test(priority = 2)
    public void createCardAttachment() {

        Card.createCardAttachment(cardEntity.getId(), ATTACHMENT_NAME, attachmentUrl)
                .then()
                .spec(baseResponseSpec)
                .body(Card.cardBodyKeyIsUpload, equalTo(expectedIsUploadValue))
                .body(Card.cardBodyKeyMimeType, equalTo(expectedMimeType))
                .body(Card.cardBodyKeyName, equalTo(ATTACHMENT_NAME))
                .body(Card.cardBodyKeyFileName, equalTo(expectedFileName));

    }

    @AfterClass()
    private void deleteCreatedBoard() {
        Board.deleteBoard(boardEntity.getId()).then().statusCode(HttpStatus.SC_OK);
    }

}
