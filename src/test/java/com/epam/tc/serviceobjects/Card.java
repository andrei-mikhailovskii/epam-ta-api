package com.epam.tc.serviceobjects;

import static io.restassured.RestAssured.given;

import com.epam.tc.tests.BaseTest;
import io.restassured.response.Response;

public class Card {

    private static final String CARDS_PATH = "/1/cards";
    private static final String CARD_ATTACHMENT_PATH = "/1/cards/{id}/attachments";

    //path parameters
    private static String pathParamId = "id";

    //query parameters
    private static String queryParamName = "name";
    private static String queryParamIdList = "idList";
    private static String queryParamUrl = "url";

    //card body keys
    public static String cardBodyKeyName = "name";
    public static String cardBodyKeyIdBoard = "idBoard";
    public static String cardBodyKeyIdList = "idList";
    public static String cardBodyKeyIsUpload = "isUpload";
    public static String cardBodyKeyMimeType = "mimeType";
    public static String cardBodyKeyFileName = "fileName";

    public static Response createCard(String listId, String cardName) {
        return given()
                .spec(BaseTest.baseRequestSpec)
                .when()
                .basePath(CARDS_PATH)
                .queryParam(queryParamIdList, listId)
                .queryParam(queryParamName, cardName)
                .post()
                .then()
                .extract().response();
    }

    public static Response createCardAttachment(String cardId, String attachmentName, String attachmentUrl) {
        return given()
                .spec(BaseTest.baseRequestSpec)
                .when()
                .basePath(CARD_ATTACHMENT_PATH)
                .pathParam(pathParamId, cardId)
                .queryParam(queryParamName, attachmentName)
                .queryParam(queryParamUrl, attachmentUrl)
                .post()
                .then()
                .extract().response();
    }

}
