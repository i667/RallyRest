
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


public class MainRally {

	public static void main(String[] args) throws URISyntaxException, IOException {
		
		String usage = "Usage: java -jar QueryTC.jar <testset1[,testset2...]>";
		if(args.length != 1){
			System.out.println("1"+usage);
			return;
		}
		String arg = args[0];
		if(!arg.matches("[a-zA-Z0-9,]*")){
			System.out.println(usage);
			return;
		}
		
		String[] all_testset = arg.split(",");
		List<String> tc_list = new ArrayList<String>();
		for(int i=0; i < all_testset.length; i++){
			System.out.println("Process TestSet "+all_testset[i]);
			QueryTestSet1 qts = new QueryTestSet1(all_testset[i]);
			qts.processAllTC();
			tc_list.addAll(qts.getList());
			qts.closeQuery();
		}
		
		System.out.println("Save to excel file: Export.xlsx");
		QueryTestSet1.saveToExcel("Export.xlsx", tc_list);
//		QueryTestSet1.printList(tc_list);
		System.out.println("Finish!");
		
		
//		QueryTestSet1 qts = new QueryTestSet1();
//		qts.processAllTC();
//		qts.printList();
//		qts.saveToExcel("export.xlsx");
//		qts.isRun(null);
		
//		System.out.println("Finish!");
//		qts.closeQuery();
        
//		qd.printJson();
		
	}

}
