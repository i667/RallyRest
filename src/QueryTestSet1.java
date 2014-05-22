import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rallydev.rest.RallyRestApi;
import com.rallydev.rest.request.GetRequest;
import com.rallydev.rest.request.QueryRequest;
import com.rallydev.rest.response.GetResponse;
import com.rallydev.rest.response.QueryResponse;
import com.rallydev.rest.util.Fetch;
import com.rallydev.rest.util.QueryFilter;
import com.rallydev.rest.util.Ref;

public class QueryTestSet1 {

	RallyRestApi rallyRest;
	Fetch testset_fetch;
	QueryRequest qtestset;
	QueryResponse response;
	public static String SkyProjectRef = "/project/7803686457";
	
	public QueryTestSet1() throws URISyntaxException {
		this.rallyRest = new RallyConnector().getRally();
		
		testset_fetch = new Fetch("FormattedID","Name","TestCases");
		this.query();
	}
	
	public void query(){
		qtestset = new QueryRequest("testset");
//		qtestset.setProject(SkyProjectRef);
		qtestset.setFetch(testset_fetch);		
		qtestset.setOrder("FormattedID desc");
		qtestset.setQueryFilter(new QueryFilter("FormattedID", "=", "TS363"));
//		qdefect.setPageSize(10);
		qtestset.setLimit(5000);
		try {
			response = rallyRest.query(qtestset);
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
	
	public void saveToExcel(String filename){
		Workbook wb = new XSSFWorkbook();
		CreationHelper helper = wb.getCreationHelper();
		
		Sheet sheet1 = wb.createSheet("Sheet1");
		CellStyle hlink_style = wb.createCellStyle();
		Font hlink_font = wb.createFont();
		hlink_font.setColor(IndexedColors.BLUE.getIndex());
	    hlink_style.setFont(hlink_font);
		//set headers
		Row row1 = sheet1.createRow(0);
		for(int i = 0; i < testset_fetch.size(); i++){
			row1.createCell(i).setCellValue(testset_fetch.get(i));
		}
		
		int j = 1;
		for(JsonElement je : response.getResults()){
			JsonObject testset1 = je.getAsJsonObject();
			
			System.out.println(String.format("\t%d - %s - %s",
					j,
                    testset1.get("FormattedID").getAsString(),
                    testset1.get("Name").getAsString()));
				
			Row row = sheet1.createRow(j);
			
			//column ID
			String strId = testset1.get("FormattedID").getAsString();
			row.createCell(0).setCellValue(strId);
			row.getCell(0).setHyperlink(getHyperLink(testset1.get("_ref").getAsString(), helper));
			row.getCell(0).setCellStyle(hlink_style);
			
			//column Name			
			String tsname = testset1.get("Name").getAsString();
			row.createCell(1).setCellValue(tsname);
			
			//column Iteration
			String iteration = testset1.get("Iteration").getAsJsonObject().get("Name").getAsString();
			row.createCell(2).setCellValue(iteration);
			
			//column update date
			String updateDate = convertDate(testset1.get("LastUpdateDate").getAsString());
			row.createCell(3).setCellValue(updateDate);
			
			j++;
		}
		
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			wb.write(fos);
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public Hyperlink getHyperLink(String _ref, CreationHelper helper){
		String link = null;
		String projecId = "7803686457";
		String oid = Ref.getOidFromRef(_ref);
		link = String.format("https://rally1.rallydev.com/#/%sud/detail/testset/%s/run",
				projecId, oid);
		
		Hyperlink hyperlink = helper.createHyperlink(Hyperlink.LINK_URL);
		hyperlink.setAddress(link);
		
		return hyperlink;
	}
	
	public void printResult(){
		int j = 1;
		for(JsonElement je : response.getResults()){
			JsonObject testset1 = je.getAsJsonObject();
			
			System.out.println(String.format("\t%d - %s - %s - %s \t%s",
					j,
                    testset1.get("FormattedID").getAsString(),                    
                    testset1.get("Name").getAsString(),
                    testset1.get("Iteration").getAsJsonObject().get("Name").getAsString(),
                    convertDate(testset1.get("LastUpdateDate").getAsString())
					));
			j++;
		}
	}
	
	public void printTC(JsonArray jarr){
		int j = 1;
		for(JsonElement je : jarr){
			JsonObject tc = je.getAsJsonObject();
			
			System.out.println(String.format("%3d - %s - %s - %s - %s \t%s",
					j,
                    tc.get("FormattedID").getAsString(),
                    tc.get("Name").getAsString(),
                    tc.get("LastVerdict").getAsString(),
                    tc.get("LastBuild").getAsString(),
                    convertDate(tc.get("LastRun").getAsString())
					));
			j++;
		}
	}
	public String convertDate(String strDate){
		return strDate.substring(0, 10);
	}
	
	public void printTCJson(){
		JsonArray jarr = response.getResults();
		JsonObject job = jarr.get(0).getAsJsonObject().get("TestCases").getAsJsonObject();
		JsonObject job1 = getNameFromObject(job, "");
		System.out.println(job1.toString());
	}
	public JsonArray getTCJson(){
		JsonArray jarr = response.getResults();
		JsonObject job = jarr.get(0).getAsJsonObject().get("TestCases").getAsJsonObject();
		JsonObject job1 = getNameFromObject(job, "");
		return job1.get("Results").getAsJsonArray();
		
	}
	public JsonObject getNameFromObject(JsonObject obj, String field){
        String subRef = obj.get("_ref").getAsString();
        GetRequest subRequest = new GetRequest(subRef);
        subRequest.addParam("PageSize", "100");
        GetResponse subResponse = null;
        
		try {
			subResponse = this.rallyRest.get(subRequest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        JsonObject subObj = subResponse.getObject();
        return subObj;
        
//        if(!subObj.isJsonNull() && subObj.has(field))
//        {
//        	return subObj.get(field).getAsString();
//        }
//        
//        return "";
	}
	public void printJson(){
		System.out.println(response.getResults().toString());
	}
	
	public void closeQuery() throws IOException{
		rallyRest.close();
	}
	
	
}
