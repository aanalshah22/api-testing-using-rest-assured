import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

public class SumValidation {
    @Test
    public void sumOfCourses(){
        JsonPath jsonPath = new JsonPath(payload.CoursePrice());
        int purchase_amount = jsonPath.getInt("dashboard.purchaseAmount");
        System.out.println("Purchase amount in API: "+purchase_amount);

        int sum = 0;
        int course_count = jsonPath.getInt("courses.size()");
        for(int i=0; i<course_count; i++){
            int price = jsonPath.getInt("courses["+i+"].price");
            int copies_sold = jsonPath.getInt("courses["+i+"].copies");
            int amount = price * copies_sold;
            sum = sum + amount;
        }
        System.out.println("Sum of all courses price obtained from API: "+sum);
        if(purchase_amount==sum){
            System.out.println("The purchase amount is equal to sum of all course prices");
        }
    }
}
