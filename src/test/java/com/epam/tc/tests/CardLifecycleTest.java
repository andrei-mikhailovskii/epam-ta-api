package com.epam.tc.tests;

import static io.restassured.RestAssured.given;

import com.epam.tc.entities.BoardEntity;
import com.epam.tc.entities.CardAttachmentEntity;
import com.epam.tc.entities.CardEntity;
import com.epam.tc.entities.ListEntity;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

public class CardLifecycleTest extends AbstractTest {

    private static final String CARD_ATTACHMENT_PATH = "{id}/attachments";
    private static final String ATTACHMENT_NAME = "testAttachment";
    private String attachmentUrl = "https://venturebeat.com/wp-content/uploads/2015/12/oracle-java-e1450723340931.jpg";
    private String expectedMimeType = "image/jpeg";
    private String expectedIsUploadValue = "true";
    private String expectedFileName = "oracle-java-e1450723340931.jpg";

    BoardEntity boardEntity = new BoardEntity();
    ListEntity listEntity = new ListEntity();
    CardEntity cardEntity = new CardEntity();
    CardAttachmentEntity attachmentEntity = new CardAttachmentEntity();

    @Test(priority = 1, groups = {"cards"})
    public void createCard() {
        boardEntity = given()
            .spec(requestSpecPost)
            .when()
            .post(BOARDS_ENDPOINT)
            .then()
            .extract().body().as(BoardEntity.class);

        listEntity = given()
            .spec(requestSpecListPost)
            .when()
            .queryParam("idBoard", boardEntity.getId())
            .post(LISTS_ENDPOINT)
            .then()
            .extract().body().as(ListEntity.class);

        cardEntity = given()
                .spec(requestSpecCardPost)
                .when()
                .queryParam("idList", listEntity.getId())
                .post(CARDS_ENDPOINT)
                .then()
                .statusCode(STATUS_OK)
                .extract().body().as(CardEntity.class);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(cardEntity.getName()).as("Invalid card name").isEqualTo(CARD_NAME);
        softly.assertThat(cardEntity.getIdBoard()).as("Invalid board id").isEqualTo(boardEntity.getId());
        softly.assertThat(cardEntity.getIdList()).as("Invalid list id").isEqualTo(listEntity.getId());
        softly.assertAll();

    }

    @Test(priority = 2, groups = {"cards"})
    public void createCardAttachment() {

        attachmentEntity = given()
                .spec(requestSpecCardPost)
                .when()
                .basePath(CARD_ATTACHMENT_PATH)
                .pathParam("id", cardEntity.getId())
                .queryParam("name", ATTACHMENT_NAME)
                .queryParam("url", attachmentUrl)
                .post()
                .then()
                .statusCode(200)
                .extract().body().as(CardAttachmentEntity.class);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(attachmentEntity.getName())
                .as("Invalid attachment name").contains(ATTACHMENT_NAME);
        softly.assertThat(attachmentEntity.getMimeType())
                .as("Invalid mime type").isEqualTo(expectedMimeType);
        softly.assertThat(attachmentEntity.getIsUpload())
                .as("Invalid \"isUpload\" value").isEqualTo(expectedIsUploadValue);
        softly.assertThat(attachmentEntity.getFileName())
                .as("Invalid file name").isEqualTo(expectedFileName);
        softly.assertAll();


    }

    @AfterClass(groups = {"cards"})
    private void deleteCreatedBoard() {
        deleteCreatedBoard(boardEntity.getId());
    }

}
