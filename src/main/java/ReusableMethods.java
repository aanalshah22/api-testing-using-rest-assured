import io.restassured.path.json.JsonPath;

public class ReusableMethods {
    public static JsonPath rawToJson(String response){
        JsonPath jsonPath1 = new JsonPath(response);
        return jsonPath1;
    }
}
