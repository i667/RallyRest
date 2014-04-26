
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;



import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rallydev.rest.RallyRestApi;
import com.rallydev.rest.request.QueryRequest;
import com.rallydev.rest.response.QueryResponse;
import com.rallydev.rest.util.Fetch;
import com.rallydev.rest.util.QueryFilter;
import com.rallydev.rest.util.Ref;

public class Test01 {

	public static void main(String[] args) throws URISyntaxException, IOException {
		
//		System.out.println("Begin to get sky testset");
//		QueryTestSet qtestset = new QueryTestSet();
//		System.out.println("Save testset to file");
//		qtestset.closeQuery();
//		System.out.println("Finish!");
		
//        BugData bug = new BugData();
        
//        bug.setData("1","2","3","4","5","6","7","8","9","10","11");
//        System.out.println(bug.formUpdateString());
//        bug.setId("DE8900");
//        bug.setName("This is a name");
//        qd.save();
        
		QueryDefect qd = new QueryDefect();
		if(qd.openConnection()){
			System.out.println("Check connection ok!");
			qd.query();
			qd.pushToDb();
			System.out.println("Close query!");
	        qd.closeQuery();
		}
		else{
			System.out.println("Check connection fail!");
		}
        
//		qd.printJson();
		
	}

}
