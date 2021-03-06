import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rallydev.rest.RallyRestApi;
import com.rallydev.rest.request.GetRequest;
import com.rallydev.rest.request.QueryRequest;
import com.rallydev.rest.response.QueryResponse;
import com.rallydev.rest.util.Fetch;
import com.rallydev.rest.util.QueryFilter;
import com.rallydev.rest.util.Ref;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import com.rallydev.rest.response.*;


public class QueryDefect {
	
	
	RallyRestApi rallyRest;
	Fetch defect_fetch;
	QueryRequest qdefect;
	QueryResponse response;
	
	public QueryDefect() throws URISyntaxException{
		this.rallyRest = new RallyConnector().getRally();
		defect_fetch = new Fetch("FormattedID","Name", "CreationDate", "Priority", "Severity",  "State", "FoundInBuild", "SubmittedBy", "Release", "Tags", "Notes");
		this.query();
	}
	
	public void query(){
		qdefect = new QueryRequest("defect");
		qdefect.setFetch(defect_fetch);
//		qdefect.setQueryFilter(new QueryFilter("Name", "contains", "RT:"));
		qdefect.setQueryFilter(new QueryFilter("FormattedID", "=", "de10204"));
		qdefect.setOrder("FormattedID desc");
//		qdefect.setPageSize(10);
		qdefect.setLimit(10);
		try {
			response = rallyRest.query(qdefect);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(!response.wasSuccessful()){
			System.out.println("Query fail!");
			for(String t1 : response.getErrors()){
				System.out.println("Error: "+ t1);
			}
			for(String t2 : response.getWarnings()){
				System.out.println("Warning: "+ t2);
			}
		}else{
			System.out.println("Total: " + response.getTotalResultCount());
		}
		
	}
	
//	public void saveToExcel(String filename){
//		Workbook wb = new XSSFWorkbook();
//		CreationHelper helper = wb.getCreationHelper();
//		
//		Sheet sheet1 = wb.createSheet("Sheet1");
//		
//		//set headers
//		Row row1 = sheet1.createRow(0);
//		for(int i = 0; i < 6; i++){
//			row1.createCell(i).setCellValue(defect_fetch.get(i));
////			row1.createCell(i).set
//		}
//		
//		int j = 1;
//		for(JsonElement je : response.getResults()){
//			JsonObject defect1 = je.getAsJsonObject();
//			
////                    defect1.get("_ref").getAsString()));
//			if(!defect1.get("Name").getAsString().startsWith("RT:")){
//				continue;
//			}
//			
//			System.out.println(String.format("\t%d\t%s - %s",
//					j,
//                    defect1.get("FormattedID").getAsString(),
//                    defect1.get("Name").getAsString()));
//				
//			Row row = sheet1.createRow(j);
//			for(int i = 0; i < 6; i++){
//				String t01 = defect_fetch.get(i);
//				String t02 = null;
////				System.out.println("fetch: " + t01);
//				if(i == 5){
//					if(defect1.has(t01) && defect1.get(t01).isJsonObject()){
//						t02 = getNameFromObject(defect1.get(t01).getAsJsonObject());
//					}else{
//						t02 = "";
//					}
//					
//				}else{
//					t02 = defect1.get(t01).getAsString();
//				}
////				System.out.println("content: " + t02);
//				row.createCell(i).setCellValue(t02);
//			}
//			j++;
//                    
//		}
//		
//		try {
//			FileOutputStream fos = new FileOutputStream(filename);
//			wb.write(fos);
//			fos.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//	}
	
	public String getNameFromObject(JsonObject obj, String field){
        String subRef = obj.get("_ref").getAsString();
        GetRequest subRequest = new GetRequest(subRef);
        GetResponse subResponse = null;
		try {
			subResponse = this.rallyRest.get(subRequest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        JsonObject subObj = subResponse.getObject();
        
        if(!subObj.isJsonNull() && subObj.has(field))
        {
        	return subObj.get(field).getAsString();
        }
        
        return "";
	}
	
//	public Hyperlink getHyperlinkFromRef(String ref){
//		String oid = Ref.getOidFromRef(ref);
//		String link = "https://rally1.rallydev.com/#/"
//	}
	
	public void save(){
		int j = 1;
		for(JsonElement je : response.getResults()){
			JsonObject def = je.getAsJsonObject();
			
//			if(!defect1.get("Name").getAsString().startsWith("RT:")){
//				continue;
//			}
			
			System.out.println(String.format("\t%d\t%s - %s",
					j,
                    def.get("FormattedID").getAsString(),
                    def.get("Name").getAsString()));
				
			
			BugData bug = new BugData();
			bug.setId(def.get("FormattedID").getAsString());
			bug.setName(def.get("Name").getAsString());
			bug.setCreationDate(def.get("CreationDate").getAsString());
			bug.setPriority(def.get("Priority").getAsString());
			bug.setSeverity(def.get("Severity").getAsString());
			bug.setSeverity(def.get("State").getAsString());
			bug.setSeverity(def.get("FoundInBuild").getAsString());
			bug.setSubmitter(def.get("SubmittedBy").getAsJsonObject().get("_refObjectName").getAsString());
			
			j++;
		}
	}
	
	public void printJson(){
		System.out.println(response.getResults().toString());
	}
	
	public void closeQuery() throws IOException{
		rallyRest.close();
	}
	
	public String convertDate(String strDate){
		String[] array = strDate.split("T");
		StringBuffer strbuf = new StringBuffer(array[1]);
		strbuf.deleteCharAt(strbuf.length()-1);
		return array[0] + " " + strbuf.toString();
		
	}
	

}
