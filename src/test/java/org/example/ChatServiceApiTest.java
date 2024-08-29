package org.example;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

public class ChatServiceApiTest {

    @BeforeClass
    public void setupSuite() {

        RestAssured.baseURI = "https://chat-service-s5g2.onrender.com";
    }

    @Test(priority = 1, description = "Test Case 1.1: Verify the API service is running.")
    public void testHealthCheck() {

        given()
                .when()
                .get("/")
                .then()
                .statusCode(200);
    }

    @Test(priority = 2, description = "Test Case 2.1: Successful user signup.")
    public void testSignupUserSuccess() {

        String payload = "{\"email\":\"ashishB@gmail.com\",\"password\":\"password123\"}";

        given()
                .header("Content-Type", "application/json")
                .body(payload)
                .when()
                .post("/auth/signup")
                .then()
                .statusCode(201);
    }

    @Test(priority = 3, description = "Test Case 2.2: User signup with an already registered email.")
    public void testSignupUserEmailAlreadyRegistered() {

        String payload = "{\"email\":\"ashishB@gmail.com\",\"password\":\"password123\"}";

        given()
                .header("Content-Type", "application/json")
                .body(payload)
                .when()
                .post("/auth/signup")
                .then()
                .statusCode(400);
    }

    @Test(priority = 4, description = "Test Case 2.3: Successful user login.")
    public void testLoginUserSuccess() {

        String payload = "{\"email\":\"ashishB@gmail.com\",\"password\":\"password123\"}";

        Response response = given()
                .header("Content-Type", "application/json")
                .body(payload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .response();

        String token = response.jsonPath().getString("authToken");
        assertNotNull(token, "Token should be present in the response");
    }

    @Test(priority = 5, description = "Test Case 2.4: Login with incorrect password.")
    public void testLoginUserInvalidPassword() {

        String payload = "{\"email\":\"ashishB@gmail.com\",\"password\":\"wrongpassword\"}";

        given()
                .header("Content-Type", "application/json")
                .body(payload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401);
    }

    @Test(priority = 6, description = "Test Case 3.1: Post a new message successfully.")
    public void testPostMessageSuccess() {

        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIxZGI0ZTEzZi04NDA2LTQ0OTEtOGYxYS01YmQzZDViMDNhYTkiLCJpYXQiOjE3MjQ5MjMzNjMsImV4cCI6MTcyNDkyMzQyM30._5Ax-rFTZ2jLjs1vkG422iQYyH8wF_sGU_34zpYWfD4";
        String payload = "{\"message\":\"Hello, World!\",\"userId\":\"userId123\",\"channelId\":\"channelId456\"}";

        given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(payload)
                .when()
                .post("/chat/postMessage")
                .then()
                .statusCode(201);
    }

    @Test(priority = 7, description = "Test Case 3.2: Attempt to post a message without authentication.")
    public void testPostMessageWithoutAuthentication() {

        String payload = "{\"message\":\"Hello, World!\",\"userId\":\"userId123\",\"channelId\":\"channelId456\"}";

        given()
                .header("Content-Type", "application/json")
                .body(payload)
                .when()
                .post("/chat/postMessage")
                .then()
                .statusCode(401);
    }

    @Test(priority = 8, description = "Test Case 3.3: Retrieve all messages.")
    public void testGetAllMessages() {

        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIxZGI0ZTEzZi04NDA2LTQ0OTEtOGYxYS01YmQzZDViMDNhYTkiLCJpYXQiOjE3MjQ5MjMzNjMsImV4cCI6MTcyNDkyMzQyM30._5Ax-rFTZ2jLjs1vkG422iQYyH8wF_sGU_34zpYWfD4";

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/chat/messages")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test(priority = 9, description = "Test Case 3.4: Retrieve replies to a specific message.")
    public void testGetRepliesToMessage() {

        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIxZGI0ZTEzZi04NDA2LTQ0OTEtOGYxYS01YmQzZDViMDNhYTkiLCJpYXQiOjE3MjQ5MjMzNjMsImV4cCI6MTcyNDkyMzQyM30._5Ax-rFTZ2jLjs1vkG422iQYyH8wF_sGU_34zpYWfD4";  // Replace with a valid token
        String messageId = "id123";  //

        given()
                .header("Authorization", "Bearer " + token)
                .queryParam("messageId", messageId)
                .when()
                .get("/chat/replies")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test(priority = 10, description = "Test Case 4.1: Create a new channel successfully.")
    public void testCreateChannelSuccess() {

        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIxZGI0ZTEzZi04NDA2LTQ0OTEtOGYxYS01YmQzZDViMDNhYTkiLCJpYXQiOjE3MjQ5MjMzNjMsImV4cCI6MTcyNDkyMzQyM30._5Ax-rFTZ2jLjs1vkG422iQYyH8wF_sGU_34zpYWfD4";  // Replace with a valid token
        String payload = "{\"name\":\"Channel Name\",\"description\":\"Description\"}";

        given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(payload)
                .when()
                .post("/channel/org/orgId123")
                .then()
                .statusCode(201);
    }

    @Test(priority = 11, description = "Test Case 4.2: Attempt to create a channel without proper authorization.")
    public void testCreateChannelWithoutAuthorization() {

        String payload = "{\"name\":\"Channel Name\",\"description\":\"Description\"}";

        given()
                .header("Content-Type", "application/json")
                .body(payload)
                .when()
                .post("/channel/org/orgId123")
                .then()
                .statusCode(403);
    }

    @Test(priority = 12, description = "Test Case 4.3: Get all channels in an organization.")
    public void testGetAllChannelsInOrganization() {

        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIxZGI0ZTEzZi04NDA2LTQ0OTEtOGYxYS01YmQzZDViMDNhYTkiLCJpYXQiOjE3MjQ5MjMzNjMsImV4cCI6MTcyNDkyMzQyM30._5Ax-rFTZ2jLjs1vkG422iQYyH8wF_sGU_34zpYWfD4";

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/channel/org/orgId123")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test(priority = 13, description = "Test Case 4.4: Get details of a specific channel.")
    public void testGetSpecificChannelDetails() {

        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIxZGI0ZTEzZi04NDA2LTQ0OTEtOGYxYS01YmQzZDViMDNhYTkiLCJpYXQiOjE3MjQ5MjMzNjMsImV4cCI6MTcyNDkyMzQyM30._5Ax-rFTZ2jLjs1vkG422iQYyH8wF_sGU_34zpYWfD4";
        String channelId = "id1234";

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/channel/" + channelId + "/org/orgId123")
                .then()
                .statusCode(200)
                .body("name", notNullValue())
                .body("description", notNullValue());
    }

    @Test(priority = 14, description = "Test Case 4.5: Add members to a channel.")
    public void testAddMembersToChannel() {

        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIxZGI0ZTEzZi04NDA2LTQ0OTEtOGYxYS01YmQzZDViMDNhYTkiLCJpYXQiOjE3MjQ5MjMzNjMsImV4cCI6MTcyNDkyMzQyM30._5Ax-rFTZ2jLjs1vkG422iQYyH8wF_sGU_34zpYWfD4";
        String payload = "{\"userIds\":[\"userId1\",\"userId2\"],\"channelId\":\"channelId123\"}";

        given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(payload)
                .when()
                .post("/channel/members")
                .then()
                .statusCode(200);
    }

    @Test(priority = 15, description = "Test Case 5.1: Retrieve all organizations.")
    public void testGetAllOrganizations() {

        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIxZGI0ZTEzZi04NDA2LTQ0OTEtOGYxYS01YmQzZDViMDNhYTkiLCJpYXQiOjE3MjQ5MjMzNjMsImV4cCI6MTcyNDkyMzQyM30._5Ax-rFTZ2jLjs1vkG422iQYyH8wF_sGU_34zpYWfD4";

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/organizations")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test(priority = 16, description = "Test Case 5.2: Create a new organization.")
    public void testCreateOrganizationSuccess() {

        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIxZGI0ZTEzZi04NDA2LTQ0OTEtOGYxYS01YmQzZDViMDNhYTkiLCJpYXQiOjE3MjQ5MjMzNjMsImV4cCI6MTcyNDkyMzQyM30._5Ax-rFTZ2jLjs1vkG422iQYyH8wF_sGU_34zpYWfD4";
        String payload = "{\"name\":\"Organization Name\",\"description\":\"Description\"}";

        given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(payload)
                .when()
                .post("/organizations")
                .then()
                .statusCode(201);
    }


    @AfterClass
    public void tearDownMethod() {
    }

}