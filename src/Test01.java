
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rallydev.rest.RallyRestApi;
import com.rallydev.rest.request.QueryRequest;
import com.rallydev.rest.response.QueryResponse;
import com.rallydev.rest.util.Fetch;
import com.rallydev.rest.util.QueryFilter;
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
		
		
//		Workbook wb = new HSSFWorkbook();
//	    //Workbook wb = new XSSFWorkbook();
//	    CreationHelper createHelper = wb.getCreationHelper();
//	    Sheet sheet = wb.createSheet("new sheet");
//
//	    // Create a row and put some cells in it. Rows are 0 based.
//	    Row row = sheet.createRow((short)0);
//	    // Create a cell and put a value in it.
//	    Cell cell = row.createCell(0);
//	    cell.setCellValue(1);
//
//	    // Or do it on one line.
//	    row.createCell(1).setCellValue(1.2);
//	    row.createCell(2).setCellValue(
//	         createHelper.createRichTextString("This is a string"));
//	    row.createCell(3).setCellValue(true);
//	    
//	    Row row2 = sheet.createRow(1);
//	    // Create a cell and put a value in it.
//	    Cell cell1 = row2.createCell(0);
//	    cell1.setCellValue(1);
//
//	    // Or do it on one line.
//	    row2.createCell(1).setCellValue(1.2);
//	    row2.createCell(2).setCellValue(
//	         "This is a string");
//	    row2.createCell(3).setCellValue(true);
//
//	    // Write the output to a file
//	    FileOutputStream fileOut = new FileOutputStream("workbook.xls");
//	    wb.write(fileOut);
//	    fileOut.close();
		
	}

}
