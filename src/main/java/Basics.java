import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.Assert;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Basics{
    public static void main(String[] args){
        //given - All input details
        //when - submit the API(resource, http method)
        //then - validate the response

        //validate if Add Place API is working as expected
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body(payload.AddPlace())
                .when().post("/maps/api/place/add/json")
                .then().log().all().assertThat().statusCode(200).body("scope",equalTo("APP"))
                .header("Server", "Apache/2.4.18 (Ubuntu)").extract().response().asString();
        System.out.println("Response: "+response);
        JsonPath jsonPath = new JsonPath(response); // for parsing json
        String placeId = jsonPath.getString("place_id");
        System.out.println("Place ID: "+placeId);

        //validate if update place API is working as expected
        String new_address = "70 winter walk, USA";
        given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body("{\n" +
                        "\"place_id\":\""+placeId+"\",\n" +
                        "\"address\":\""+new_address+"\",\n" +
                        "\"key\":\"qaclick123\"\n" +
                        "}")
                .when().put("/maps/api/place/update/json")
                .then().log().all().assertThat().statusCode(200).body("msg",equalTo("Address successfully updated"));

        //validate if get place API is working as expected
         String getPlaceResponse = given().log().all().queryParam("key", "qaclick123")
                .queryParam("place_id", placeId)
                .when().get("/maps/api/place/get/json")
        .then().log().all().assertThat().statusCode(200).extract().response().asString();
        JsonPath jsonPath1 =  ReusableMethods.rawToJson(getPlaceResponse);
        String actual_address = jsonPath1.getString("address");
        Assert.assertEquals(actual_address, new_address);
    }
}