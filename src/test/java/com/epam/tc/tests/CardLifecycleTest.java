package com.epam.tc.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import com.epam.tc.entities.CardEntity;
import com.epam.tc.entities.ListEntity;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CardLifecycleTest extends BaseTest {

    private static final String CARDS_PATH = "/1/cards";
    private static final String CARD_ATTACHMENT_PATH = "/1/cards/{id}/attachments";
    private static final String CARD_NAME = "testCard";
    private static final String ATTACHMENT_NAME = "testAttachment";
    private String attachmentUrl = "https://venturebeat.com/wp-content/uploads/2015/12/oracle-java-e1450723340931.jpg";
    private String expectedMimeType = "image/jpeg";
    private boolean expectedIsUploadValue = true;
    private String expectedFileName = "oracle-java-e1450723340931.jpg";
    CardEntity cardEntity = new CardEntity();

    @BeforeClass
    private void createBoardAndList() {
        boardEntity = createNewBoard(BoardLifecycleTest.BOARD_NAME);
        listEntity = given()
                .spec(baseRequestSpec)
                .when()
                .basePath(LISTS_PATH)
                .queryParam("name", ListLifecycleTest.LIST_NAME)
                .queryParam("idBoard", boardEntity.getId())
                .post()
                .then()
                .extract().body().as(ListEntity.class);
    }

    @Test(priority = 1)
    public void createCard() {

        cardEntity = given()
                .spec(baseRequestSpec)
                .when()
                .basePath(CARDS_PATH)
                .queryParam("idList", listEntity.getId())
                .queryParam("name", CARD_NAME)
                .post()
                .then()
                .spec(baseResponseSpec)
                .body("idBoard", equalTo(boardEntity.getId()))
                .body("idList", equalTo(listEntity.getId()))
                .body("name", equalTo(CARD_NAME))
                .extract().body().as(CardEntity.class);

    }

    @Test(priority = 2)
    public void createCardAttachment() {

        given()
                .spec(baseRequestSpec)
                .when()
                .basePath(CARD_ATTACHMENT_PATH)
                .pathParam("id", cardEntity.getId())
                .queryParam("name", ATTACHMENT_NAME)
                .queryParam("url", attachmentUrl)
                .post()
                .then()
                .spec(baseResponseSpec)
                .body("isUpload", equalTo(expectedIsUploadValue))
                .body("mimeType", equalTo(expectedMimeType))
                .body("name", equalTo(ATTACHMENT_NAME))
                .body("fileName", equalTo(expectedFileName));

    }

    @AfterClass()
    private void deleteCreatedBoard() {
        deleteCreatedBoard(boardEntity.getId());
    }

}
