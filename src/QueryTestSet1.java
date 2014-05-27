import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

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

public class QueryTestSet1 {

	RallyRestApi rallyRest;
	Fetch testset_fetch;
	QueryRequest qtestset;
	QueryResponse response;
	int total_tc = 0;
	String tc_ref = "";
	public static String SkyProjectRef = "/project/7803686457";
	List<String> alltc;
	String tsid ="";
	int count_norun = 0;
	String testset = "TS363";
	
	public QueryTestSet1(String ts) throws URISyntaxException {
		testset = ts;
		this.rallyRest = new RallyConnector().getRally();
		alltc = new ArrayList<String>();
		
		testset_fetch = new Fetch("FormattedID","Name","TestCases");
		this.query();
		JsonObject obj = getTCsObj();
		tc_ref = obj.get("_ref").getAsString();
//		System.out.println("finish get testcases ref!");
		total_tc = obj.get("Count").getAsInt();
		System.out.println("Total testcase in this TestSet: " + total_tc);
		tsid = getTSId();

//		System.out.println("finish contructor!");
	}
	
//	public QueryTestSet1(String joe){
//		alltc = new ArrayList<String>();
//		alltc.add("413|TC17456|Transfer - PSTN-SIP/TLS - from SIP/TLS to SIP/UDP - 3 minutes|Critical||||https://rally1.rallydev.com/#/7803686457ud/detail/testcase/12783488517");
//		alltc.add("419|TC18395|Call Park - SIP/TLS calls SIP/UDP - SIP/TLS parks SIP/TLS retrieves|Important||||https://rally1.rallydev.com/#/7803686457ud/detail/testcase/14028862164");
//	}
	public List<String> getList(){
		return alltc;
	}
	
	public void setTestSet(String ts){
		testset = ts;
	}
	
	public void query(){
		qtestset = new QueryRequest("testset");
//		qtestset.setProject(SkyProjectRef);
		qtestset.setFetch(testset_fetch);		
		qtestset.setOrder("FormattedID desc");
		qtestset.setQueryFilter(new QueryFilter("FormattedID", "=", testset));
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
//			ts_formattedId = response.getget("FormattedID").toString();
		}
		
	}
	
	public static void saveToExcel(String filename, List<String> tclist){
		Workbook wb = new XSSFWorkbook();
		CreationHelper helper = wb.getCreationHelper();
		
		Sheet sheet1 = wb.createSheet("TestCases");
		CellStyle hlink_style = wb.createCellStyle();
		Font hlink_font = wb.createFont();
		hlink_font.setColor(IndexedColors.BLUE.getIndex());
	    hlink_style.setFont(hlink_font);
		//set headers
		Row row1 = sheet1.createRow(0);
		String[] headers = {"TestSet", "ID", "Name", "Priority", "Last Verdict", "Last Build", "Last Run"};
		for(int i = 0; i < headers.length; i++){
			row1.createCell(i).setCellValue(headers[i]);
		}
		
		int j = 1;
		for(String line : tclist){
			
			Row row = sheet1.createRow(j);
//			line = line.replace("|", "::");
//			System.out.println(line);
			
			String[] line_arr = line.split("::");
//			System.out.println("after split: "+ line_arr.length);
			
			//#
			for(int i=0; i < line_arr.length -1; i++){
//				System.out.println(String.format("i = %d, val= %s",i,line_arr[i]));
				row.createCell(i).setCellValue(line_arr[i]);
				if(i == 1){
					row.getCell(i).setHyperlink(getHyperLink(line_arr[7], helper));
					row.getCell(i).setCellStyle(hlink_style);
				}
			}
			
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
	
	
	public static Hyperlink getHyperLink(String link, CreationHelper helper){
		
		Hyperlink hyperlink = helper.createHyperlink(Hyperlink.LINK_URL);
		hyperlink.setAddress(link);
		
		return hyperlink;
	}
	
	public String getTCUrl(JsonObject obj){
		String tcid = obj.get("ObjectID").getAsString();
		return String.format("https://rally1.rallydev.com/#/7803686457ud/detail/testcase/%s", tcid);
	}
	
	
	public void storeTCToList(JsonObject json, int count){
		int j = count;
		JsonArray jarr = json.get("Results").getAsJsonArray();		
		
		for(JsonElement je : jarr){
			String priority = "";
			String last_verdict ="";
			String last_build ="";
			String last_run="";
			JsonObject tc = je.getAsJsonObject();
			try{
				if(!tc.get("Priority").toString().equalsIgnoreCase("null")){
					priority = tc.get("Priority").getAsString();
				}		
				
				if(isRun(tc)){
					last_verdict = tc.get("LastVerdict").getAsString();
					last_build = tc.get("LastBuild").getAsString();
					last_run = convertDate(tc.get("LastRun").getAsString());
				}
				else{
					count_norun++;
				}
//				if(!tc.get("LastVerdict").toString().equalsIgnoreCase("null")){
//					last_verdict = tc.get("LastVerdict").getAsString();
//				}				
//				if(!tc.get("LastBuild").toString().equalsIgnoreCase("null")){
//					last_build = tc.get("LastBuild").getAsString();
//				}
//				if(!tc.get("LastRun").toString().equalsIgnoreCase("null")){
//					last_run = convertDate(tc.get("LastRun").getAsString());
//				}
				alltc.add(String.format("%s::%s::%s::%s::%s::%s::%s::%s",
						testset,
	                    tc.get("FormattedID").getAsString(),
	                    tc.get("Name").getAsString(),
	                    priority,	                    
	                    last_verdict,
	                    last_build,
	                    last_run,
	                    getTCUrl(tc)
						));
				System.out.println("Current process testcase "+j);
				j++;
			}catch(Exception ex){
				ex.printStackTrace();
				System.out.println(tc.toString());
				System.exit(0);
			}
		}
	}
	
	public static void printList(List<String> all){
		for(int i=0; i< all.size(); i++){
			System.out.println(all.get(i));
		}
//		System.out.println("total: "+total_tc);
//		System.out.println("no run: "+count_norun);
	}
	
	
	public String convertDate(String strDate){
		return strDate.substring(0, 10);
	}
	
	public JsonObject getTCsObj(){
		JsonArray jarr = response.getResults();
		JsonObject job = jarr.get(0).getAsJsonObject().get("TestCases").getAsJsonObject();
		return job;
		
	}
	
	public boolean isRun(JsonObject tcjson){
		
		JsonObject results_obj = tcjson.get("Results").getAsJsonObject();
		int total_result = results_obj.get("Count").getAsInt();
		
		String results_ref = results_obj.get("_ref").getAsString();
		
		int runtime = (total_result / 50) + 1;
		int start = 1;
		for (int i = 0; i < runtime; i++){
			JsonObject js_res = getJsonFromRef(results_ref, start);
			if(js_res.toString().contains(tsid)){
				return true;
			}			
			start += 50;
		}
		return false;
		
		
//		String results_ref = "https://rally1.rallydev.com/slm/webservice/v2.0/testcase/11505968067/Results";
		
	}
	
	public String getTSId()	{
		
		JsonArray jarr = response.getResults();
		JsonObject job = jarr.get(0).getAsJsonObject();
		String _ref = job.get("_ref").getAsString();
//		String _ref = "https://rally1.rallydev.com/slm/webservice/v2.0/testset/19216894233";
		int begin = _ref.indexOf("testset/") + 8;
		
		
		return _ref.substring(begin);
	}
	
	public JsonObject getJsonFromRef(String _ref, int start){
//        String subRef = obj.get("_ref").getAsString();
        GetRequest subRequest = new GetRequest(_ref);
        subRequest.addParam("pagesize", "50");
        subRequest.addParam("start", String.valueOf(start));
        GetResponse subResponse = null;
        
		try {
			subResponse = this.rallyRest.get(subRequest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return subResponse.getObject();
//        JsonObject subObj = subResponse.getObject();
//        return subObj.get("Results").getAsJsonArray();
        
	}
	
	public void processAllTC(){
		int total_run = (total_tc / 50) + 1;
		int start = 1;
		JsonObject json;
		for(int i=0; i < total_run; i++){
			json = getJsonFromRef(tc_ref, start);
			storeTCToList(json, start);
			start += 50;
		}
			
	}
	
	public void printJson(){
		System.out.println(response.getResults().toString());
	}
	
	public void closeQuery() throws IOException{
		rallyRest.close();
	}
	
	
}
