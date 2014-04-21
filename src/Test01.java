
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


//import com.rallydev.rest.util.Ref;

public class Test01 {

	public static void main(String[] args) throws URISyntaxException, IOException {
		// TODO Auto-generated method stub
//		String host = "https://rally1.rallydev.com";
//        String username = "tttvan@tma.com.vn";
//        String password = "Ktn@051628";
//        String wsapiVersion = "v2.0";
//        String RingoProjectRef = "/project/7803686457";
//        String SoftswitchProjectRef = "/project/9808582196";
//        String SkyProjectRef = "/project/7803241224";
////        String workspaceRef = "/workspace/ShoreTel"; 
//        String applicationName = "RestExample_GetDefect";
//        
//		RallyRestApi restApi = new RallyRestApi(new URI(host), username, password);
//		restApi.setWsapiVersion(wsapiVersion);
//		restApi.setApplicationName(applicationName);
//		try{		 
//			System.out.println("Querying for one test defect");
////			JsonObject defect = new JsonObject();
////			defect.addProperty("Workspace", workspaceRef);
////			defect.addProperty("Project", projectRef);
////			defect.addProperty("FormattedID", "10205");
//			
//			QueryRequest qdefect = new QueryRequest("defect");
//			qdefect.setFetch(new Fetch("Name", "FormattedID","SubmittedBy"));
//			qdefect.setQueryFilter(new QueryFilter("Name", "contains", "RT:"));
//			qdefect.setOrder("FormattedID desc");
//			
//			//set project
////			qdefect.setProject(SkyProjectRef);
//			qdefect.setPageSize(10);
//			qdefect.setLimit(10);
//			
//			QueryResponse response = restApi.query(qdefect);
//			System.out.println("Query url: " + qdefect.toUrl());
//			
//			int count = 0;
//			if(response.wasSuccessful()){
//				System.out.println(String.format("Total result: %d", response.getTotalResultCount()));
//				System.out.println("Detail:");
//				for(JsonElement je : response.getResults()){
//					JsonObject defect1 = je.getAsJsonObject();
//					System.out.println(String.format("\t%s - %s - %s",
//                            defect1.get("FormattedID").getAsString(),
//                            defect1.get("Name").getAsString(),
//                            
//                            defect1.get("SubmittedBy").toString()));
//					count++;
//                            
//				}
//				System.out.println("total printed: "+count);
////				for(String t1 : response.getErrors()){
////					System.out.println("Error: "+ t1);
////				}
////				for(String t2 : response.getWarnings()){
////					System.out.println("Warning: "+ t2);
////				}
//			}else{
//				System.out.println("Query fail!");
//				for(String t1 : response.getErrors()){
//					System.out.println("Error: "+ t1);
//				}
//				for(String t2 : response.getWarnings()){
//					System.out.println("Warning: "+ t2);
//				}
//			}
//  
//  
//		}
//		finally{
//			restApi.close();
//			System.out.println("Finish!");
//		}
		
//		
		System.out.println("Begin to get defects");
		QueryDefect qd = new QueryDefect();
		System.out.println("Save defect to file");
		qd.saveToExcel("defects.xlsx");
		qd.closeQuery();
		System.out.println("Finish!");
		
		
	}

}
