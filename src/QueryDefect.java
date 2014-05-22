import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rallydev.rest.RallyRestApi;
import com.rallydev.rest.request.GetRequest;
import com.rallydev.rest.request.QueryRequest;
import com.rallydev.rest.response.QueryResponse;
import com.rallydev.rest.util.Fetch;
import com.rallydev.rest.util.QueryFilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.rallydev.rest.response.*;


public class QueryDefect {
	
	
	RallyRestApi rallyRest;
	Fetch defect_fetch;
	QueryRequest qdefect;
	QueryResponse response;
	String url = "jdbc:mysql://localhost:3306/test";
    String user = "root";
    String password = "";
    boolean connected = true;
    Connection conn = null;
    Properties configs;
	
	public QueryDefect() throws URISyntaxException{
		this.rallyRest = new RallyConnector().getRally();
//		bug = new BugData();
//		try {
//			conn = DriverManager.getConnection(url, user, password);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			connected = false;
//		}
		defect_fetch = new Fetch("FormattedID","Name", "CreationDate", "Priority", "Severity",  "State", "FoundInBuild", "SubmittedBy", "Release", "Tags", "Notes");
		configs = new Properties();
		readConfig();
	}
	
//	public BugData getBugData() {
//		return this.bug;
//	}
	
	public boolean openConnection(){
		try {
			conn = DriverManager.getConnection(url, user, password);
			return true;
		} catch (SQLException e) {
//			e.printStackTrace();
			return false;
		}
//		if(conn != null){
//			System.out.println("Connect to db OK!");
//			try {
//				conn.close();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return true;
//		}else{
//			System.out.println("Problem connect to db");
//			return false;
//		}
	}
	
	public void query(){
		qdefect = new QueryRequest("defect");
		qdefect.setFetch(defect_fetch);
//		qdefect.setQueryFilter(new QueryFilter("Name", "contains", "RT:"));
		qdefect.setQueryFilter(new QueryFilter("FormattedID", "=", "de8916"));
		qdefect.setOrder("FormattedID desc");
//		qdefect.setPageSize(1);
		qdefect.setLimit(5000);
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
	
	
	public void pushToDb(){
		int j = 1;
		for(JsonElement je : response.getResults()){
			JsonObject def = je.getAsJsonObject();
			
			if(!def.get("Name").getAsString().startsWith("RT:")){
				continue;
			}
			//print to check
			System.out.println(String.format("\t%d\t%s - %s",
					j,
                    def.get("FormattedID").getAsString(),
                    def.get("Name").getAsString()));
			
			BugData bug = new BugData();
			bug.saveWithJson(def);
			if(!bug.exists(conn)){
				System.out.println("Not exist, insert to db");
				bug.insertToDb(conn);
			}else{
				System.out.println("Update to db");
				bug.updateToDb(conn);
			}
			j++;
			
		}
	}
	
	public void printJson(){
		System.out.println(response.getResults().toString());
	}
	
	public void closeQuery() throws IOException{
		rallyRest.close();
		try{
			if(conn != null){
				conn.close();
			}
				
		}catch(SQLException e){
			
		}
	}
	
	public void readConfig(){
//		configs = new Properties();
		FileInputStream fis = null;
		File fileconfig = new File("config.cfg");
		if(!fileconfig.exists()){
			createDefaultConfigs();
			return;
		}
		
		try{
			fis = new FileInputStream("config.cfg");
			configs.load(fis);
			this.url = String.format("jdbc:mysql://%s:3306/%s", configs.getProperty("host"), configs.getProperty("database"));
			this.user = configs.getProperty("username");
			this.password = configs.getProperty("password");
			
		} catch(FileNotFoundException fnf){
			fnf.printStackTrace();
		} catch (IOException ioe){
			ioe.printStackTrace();
		}finally{
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	public void createDefaultConfigs(){
		OutputStream outs = null;
		try{
			outs = new FileOutputStream("config.cfg");
			configs.setProperty("host", "localhost");
			configs.setProperty("username", "root");
			configs.setProperty("password", "");
			configs.setProperty("database", "test");
			configs.store(outs, null);
		}catch(FileNotFoundException fnf){
			fnf.printStackTrace();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}finally{
			if(outs != null){
				try {
					outs.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
