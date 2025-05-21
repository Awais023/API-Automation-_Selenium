//import io.restassured.RestAssured;

import static io.restassured.RestAssured.*;

import org.testng.Assert;
import org.testng.annotations.Test;
import io.restassured.response.Response;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class APITests {

    @Test
    void FirstAPITest() {
        Response response = get("https://jsonplaceholder.typicode.com/users/1");
//        Response response = RestAssured.get("https://jsonplaceholder.typicode.com/users/1");
        System.out.println("RESPONSE : " + response.asString());
        System.out.println("STATUS CODE : " + response.getStatusCode());
        System.out.println("API BODY : " + response.getBody().asString());
        System.out.println("API TIME TAKEN in MILI SECONDS : " + response.getTime());
        System.out.println("API HEADER : " + response.getHeader("content-Type"));

        // PASSED ASSERTION
//        int statusCode = response.getStatusCode();
//        Assert.assertEquals(statusCode, 200);

//         FAILED ASSERTION
//        int statusCode_ = response.getStatusCode();
//        Assert.assertEquals(statusCode_, 201);

        //IF I DO A STATIC IMPORT OF REST ASSURED LIBRARY, THEN NO NEED TO USE RESTASSURED.GET.
    }

    @Test
    void SecondAPITest() {
        given().get("https://jsonplaceholder.typicode.com/users/1").then().statusCode((200));
    }

    @Test
    void ValidateEmail() {
        Response res = get("https://jsonplaceholder.typicode.com/users/1");
        String email = res.jsonPath().getString("email");
        Assert.assertEquals(email, "Sincere@april.biz");
    }

    @Test
    void ValidateEmailFromList() {
        Response res = get("https://jsonplaceholder.typicode.com/users");
        List<String> emailList = res.jsonPath().getList("email");
        System.out.println("Extracted Emails: " + emailList);
        String expectedEmail = "Sincere@april.biz";

        String email = res.jsonPath().getString("email");
        Assert.assertTrue(emailList.contains(expectedEmail), "Email not found in list!");
    }

    @Test
    void saveResponseToFile() throws IOException {
        String json = get("https://jsonplaceholder.typicode.com/users").asString();
        Files.write(Paths.get("usersNew.json"), json.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void validatePerformance() {
        long responseTime = get("https://jsonplaceholder.typicode.com/users").getTime();
        System.out.println("RESPONSE TIME: " + responseTime);
        Assert.assertTrue(responseTime < 2000, "API is too slow");
    }

    @Test
    void validateHeaders() {
        Response response = get("https://jsonplaceholder.typicode.com/users");
        String contentType = response.header("Content-Type");
        Assert.assertEquals(contentType, "application/json; charset=utf-8", "Header mismatch");
    }

    @Test
    void createUser() {
        String payload = """
                    {
                        "name": "Awais Sultan",
                        "username": "awais_sultan",
                        "email": "awais@example.com"
                    }
                """;
        Response response = given()
                .header("Content-Type", "application/json")
                .body(payload)
                .post("https://jsonplaceholder.typicode.com/users");
        Assert.assertEquals(response.getStatusCode(), 201);
        System.out.println("RESPONSE BODY: " + response.getBody().asString());
        Assert.assertTrue(response.getBody().asString().contains("Awais"));
    }

    @Test
    void updateUser() {
        String payload = """
                    {
                        "name": "Awais Updated New ",
                        "username": "awais_sultan",
                        "email": "awais@example.com"
                    }
                """;
        Response response = given()
                .header("Content-Type", "application/json")
                .body(payload)
                .put("https://jsonplaceholder.typicode.com/users/1");
        System.out.println("RESPONSE BODY: " + response.getBody().asString());
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test
    void deleteUser() {
        Response response = delete("https://jsonplaceholder.typicode.com/users/1");
        Assert.assertEquals(response.getStatusCode(), 200);
    }
}
