import io.restassured.path.json.JsonPath;

public class ComplexJsonPath {
    public static void main(String[] args){
        JsonPath jsonPath = new JsonPath(payload.CoursePrice());

        //Print no. of courses returned by API
        int course_count = jsonPath.getInt("courses.size()");
        System.out.println("//Print no. of courses returned by API");
        System.out.println("Number of courses: "+course_count);

        //Print purchase amount
        int purchase_amount = jsonPath.getInt("dashboard.purchaseAmount");
        System.out.println("\n//Print purchase amount");
        System.out.println("Total amount: "+purchase_amount);

        //Print title of first course
        String first_title = jsonPath.get("courses[0].title");
        System.out.println("\n//Print title of first course");
        System.out.println("Title of first course: "+first_title);

        //Print all course titles and their prices
        System.out.println("\n//Print all course titles and their prices");
        for(int i=0; i<course_count; i++){
            String title = jsonPath.get("courses["+i+"].title");
            int price = jsonPath.getInt("courses["+i+"].price");
            System.out.println("Course "+(i+1)+ ": "+title );
            System.out.println("Price of "+title+": "+price+"\n");
        }

        //Print number of copies sold by RPA course
        System.out.println("\n//Print number of copies sold by RPA course");
        for(int i=0; i<course_count; i++){
            String title = jsonPath.get("courses["+i+"].title");
            String course_title = "RPA";
            if(title.equalsIgnoreCase(course_title)){
                int copies_sold = jsonPath.getInt("courses["+i+"].copies");
                System.out.println("Number of copies sold by "+course_title+" : "+copies_sold);
                break;
            }
        }

    }
}

