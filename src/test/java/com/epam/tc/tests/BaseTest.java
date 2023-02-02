package com.epam.tc.tests;

import com.epam.tc.PropertiesExtractor;
import com.epam.tc.entities.BoardEntity;
import com.epam.tc.entities.ListEntity;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class BaseTest extends PropertiesExtractor {

    public static final String BASE_ENDPOINT = "https://api.trello.com";

    private String queryParamNameKey = "key";
    private String queryParamNameToken = "token";
    private String headerNameContentType = "Content-Type";
    private String expectedHeaderContentType = "application/json; charset=utf-8";

    BoardEntity boardEntity = new BoardEntity();
    ListEntity listEntity = new ListEntity();

    public static RequestSpecification baseRequestSpec;
    public static ResponseSpecification baseResponseSpec;

    @BeforeClass
    public void baseSetup() {
        RestAssured.baseURI = BASE_ENDPOINT;

        PropertiesExtractor.getProperties();

        baseRequestSpec = new RequestSpecBuilder()
                .addQueryParam(queryParamNameKey, apiKey)
                .addQueryParam(queryParamNameToken, apiToken)
                .setContentType(ContentType.JSON)
                .build();

        baseResponseSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(HttpStatus.SC_OK)
                .expectHeader(headerNameContentType, expectedHeaderContentType)
                .build();
    }

    @AfterClass
    public void teardown() {
        RestAssured.reset();
        apiKey = null;
        apiToken = null;
    }

}
