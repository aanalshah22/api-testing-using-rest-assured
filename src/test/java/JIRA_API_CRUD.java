import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

public class JIRA_API_CRUD {
    @Test
    public void createSessionandAddIssue() {
        RestAssured.baseURI = "http://localhost:8080";
        //Create session
        SessionFilter sessionFilter = new SessionFilter();
        String response = given().header("Content-Type", "application/json").body("{ \"username\": \"aanal.addweb\", \"password\": \"addweb123\" }")
                .filter(sessionFilter).when().post("/rest/auth/1/session")
                .then().assertThat().statusCode(200).log().all().extract().response().asString();
        System.out.println(response);
        JsonPath jsonPath = new JsonPath(response);
        String sessionID = jsonPath.getString("session.value");
        System.out.println(sessionID);

        //Add issue
        String response1 = given().header("Content-Type", "application/json").body("\n" +
                "{\n" +
                "    \"fields\": {\n" +
                "        \"summary\": \"Bug 1\",\n" +
                "       \"description\": \"Creating of an issue using project keys and issue type names using the REST API\",\n" +
                "       \"issuetype\": {\n" +
                "          \"id\": \"10101\"\n" +
                "       },\n" +
                "       \"project\":\n" +
                "       {\n" +
                "          \"id\": \"10000\"\n" +
                "       }   \n" +
                "   }\n" +
                "}")
                .filter(sessionFilter).when().post("/rest/api/2/issue")
                .then().assertThat().statusCode(201).extract().response().asString();
        JsonPath jsonPath1 = new JsonPath(response1);
        System.out.println(response1);
        String bugID = jsonPath1.getString("id");
        String bug_key = jsonPath1.getString("key");
        String bug_url = jsonPath1.getString("self");
        System.out.println(bugID);
        System.out.println(bug_key);
        System.out.println(bug_url);
    }

    @Test
    public void addComment() {
        RestAssured.baseURI = "http://localhost:8080";
        //Create session
        SessionFilter sessionFilter = new SessionFilter();
        String response = given().header("Content-Type", "application/json").body("{ \"username\": \"aanal.addweb\", \"password\": \"addweb123\" }")
                .filter(sessionFilter).when().post("/rest/auth/1/session")
                .then().assertThat().statusCode(200).log().all().extract().response().asString();
        System.out.println(response);
        JsonPath jsonPath = new JsonPath(response);
        String sessionID = jsonPath.getString("session.value");
        System.out.println(sessionID);

        //Add comment
        String response1 = given().header("Content-Type", "application/json").body("{\n" +
                "    \"body\": \"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque eget venenatis elit. Duis eu justo eget augue iaculis fermentum. Sed semper quam laoreet nisi egestas at posuere augue semper.\",\n" +
                "    \"visibility\": {\n" +
                "        \"type\": \"role\",\n" +
                "        \"value\": \"Administrators\"\n" +
                "    }\n" +
                "}")
                .filter(sessionFilter).when().post("/rest/api/2/issue/10300/comment")
                .then().assertThat().statusCode(201).extract().response().asString();
        JsonPath jsonPath1 = new JsonPath(response1);
        System.out.println(response1);
        String body = jsonPath1.getString("body");
        System.out.println(body);
    }

    @Test
    public void addAttachment() {
        RestAssured.baseURI = "http://localhost:8080";
        //Create session
        SessionFilter sessionFilter = new SessionFilter();
        String response = given().header("Content-Type", "application/json").body("{ \"username\": \"aanal.addweb\", \"password\": \"addweb123\" }")
                .filter(sessionFilter).when().post("/rest/auth/1/session")
                .then().assertThat().statusCode(200).log().all().extract().response().asString();
        System.out.println(response);
        JsonPath jsonPath = new JsonPath(response);
        String sessionID = jsonPath.getString("session.value");
        System.out.println(sessionID);

        //Add attachment
        String response1 = given().header("X-Atlassian-Token", "no-check").pathParam("key", "10300").header("Content-Type", "multipart/form-data")
                .filter(sessionFilter)
                .multiPart("file", new File("/Users/addweb/IdeaProjects/APITestingusingRestAssured/src/test/java/jira.txt")).when().post("/rest/api/2/issue/{key}/attachments")
                .then().assertThat().statusCode(200).extract().response().asString();
        JsonPath jsonPath1 = new JsonPath(response1);
        System.out.println(response1);
        String filename = jsonPath1.getString("filename");
        System.out.println(filename);
    }
}