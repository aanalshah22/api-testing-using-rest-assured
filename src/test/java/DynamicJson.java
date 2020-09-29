import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;

public class DynamicJson {
    @DataProvider(name = "users")
    public Object[] getData(){
        return new Object[][]{
                {"Riddhi","QA"},
                {"Krina","QA"},
                {"Akash","QA"}
        };
    }

    //Check the response code is 200 when user is added
    @Test(dataProvider= "users")
    public void checkResponseCode(String name, String job){
        RestAssured.baseURI = "https://reqres.in";
        Response response = given().header("Content-Type", "application/json").body(payload.AddUser(name, job)).
                            when().post("/api/users");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 201, "Status codes does not match");
    }

    //Add 3 users and get the id of the users
    @Test(dataProvider= "users")
    public void addUser(String name, String job){
        RestAssured.baseURI = "https://reqres.in";
        String response = given().header("Content-Type", "application/json").body(payload.AddUser(name, job)).
                          when().post("/api/users").
                          then().assertThat().statusCode(201).extract().response().asString();
        JsonPath jsonPath = ReusableMethods.rawToJson(response);
        System.out.println(response);
        int user_id = jsonPath.getInt("id");
        System.out.println("ID of "+name+" is "+user_id);
    }

    //Update job of the users and verify the response
    @Test(dataProvider= "users")
    public void updateUserJob(String name, String job){
        RestAssured.baseURI = "https://reqres.in";
        //Add the user
        String response = given().header("Content-Type", "application/json").body(payload.AddUser(name, job)).
                          when().post("/api/users").
                          then().assertThat().statusCode(201).extract().response().asString();
        JsonPath jsonPath = ReusableMethods.rawToJson(response);
        System.out.println(response);
        int user_id  = jsonPath.getInt("id");
        System.out.println("ID of "+name+" is "+user_id );

        //Update the job of the user using the id
        String new_job = "Analyst";
        String response1 = given().header("Content-Type", "application/json").body(payload.UpdateUser(name, new_job)).
                           when().post("/api/users/2").
                           then().extract().response().asString();
        JsonPath jsonPath1 = ReusableMethods.rawToJson(response1);
        System.out.println(response1);
        String updated_job = jsonPath1.getString("job");
        Assert.assertEquals(new_job, updated_job, "Job is not updated");
    }

    //Delete 3 users and check the response
    @Test(dataProvider= "users")
    public void deleteUser(String name, String job){
        RestAssured.baseURI = "https://reqres.in";
        //Add 3 users first
        String response = given().header("Content-Type", "application/json").body(payload.AddUser(name, job)).
                          when().post("/api/users").
                          then().assertThat().statusCode(201).extract().response().asString();
        JsonPath jsonPath = ReusableMethods.rawToJson(response);
        System.out.println(response);
        int id = jsonPath.getInt("id");
        System.out.println("ID of "+name+" is "+id);
        //Hit delete request for those ids and check the user is deleted or not
        Response response1 = given().header("Content-Type", "application/json").
                             when().delete("/api/users/2");
        int statusCode = response1.getStatusCode();
        Assert.assertEquals(statusCode, 204, "Status codes does not match");
    }

    //Read json file and execute add user
    @Test
    public void readFileAddUser() throws IOException {
        RestAssured.baseURI = "https://reqres.in";
        String response = given().header("Content-Type", "application/json").body(new String(Files.readAllBytes(Paths.get("/Users/addweb/Desktop/AddUser.json")))).
                          when().post("/api/users").
                          then().assertThat().statusCode(201).extract().response().asString();
        System.out.println(response);
    }
}