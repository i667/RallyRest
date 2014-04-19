import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.poi.hslf.model.Hyperlink;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rallydev.rest.RallyRestApi;
import com.rallydev.rest.request.QueryRequest;
import com.rallydev.rest.response.QueryResponse;
import com.rallydev.rest.util.Fetch;
import com.rallydev.rest.util.Ref;

public class QueryTestSet {

	RallyRestApi rallyRest;
	Fetch testset_fetch;
	QueryRequest qtestset;
	QueryResponse response;
	public static String SkyProjectRef = "/project/7803686457";
	
	public QueryTestSet() throws URISyntaxException {
		this.rallyRest = new RallyConnector().getRally();
		
		testset_fetch = new Fetch("FormattedID","Name");
		this.query();
	}
	
	public void query(){
		qtestset = new QueryRequest("testset");
		qtestset.setProject(SkyProjectRef);
		qtestset.setFetch(testset_fetch);		
		qtestset.setOrder("FormattedID desc");
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
		for(int i = 0; i < 2; i++){
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
			for(int i = 0; i < testset_fetch.size(); i++){
				String t01 = testset_fetch.get(i);
				String t02 = testset1.get(t01).getAsString();
				row.createCell(i).setCellValue(t02);
				if(i == 0){
					row.getCell(0).setHyperlink(getHyperLink(testset1.get("_ref").getAsString(), helper));
					row.getCell(0).setCellStyle(hlink_style);
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
	
	public org.apache.poi.ss.usermodel.Hyperlink getHyperLink(String _ref, CreationHelper helper){
		String link = null;
		String projecId = "7803686457";
		String oid = Ref.getOidFromRef(_ref);
		link = String.format("https://rally1.rallydev.com/#/%sud/detail/testset/%s/run",
				projecId, oid);
		
		org.apache.poi.ss.usermodel.Hyperlink hyperlink = helper.createHyperlink(Hyperlink.LINK_URL);
		hyperlink.setAddress(link);
		
		return hyperlink;
	}
	public void closeQuery() throws IOException{
		rallyRest.close();
	}
}
