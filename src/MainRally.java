
import java.io.IOException;
import java.net.URISyntaxException;


public class MainRally {

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
        
//		QueryDefect qd = new QueryDefect();
//		if(qd.openConnection()){
//			System.out.println("Check connection ok!");
//			qd.query();
//			qd.pushToDb();
//			System.out.println("Close query!");
//	        qd.closeQuery();
//		}
//		else{
//			System.out.println("Check connection fail!");
//		}
		
		QueryTestSet1 qts = new QueryTestSet1();
		qts.printTCJson();		
//		System.out.println("Save to excel...");
//		qts.saveToExcel("testset.xlsx");
		System.out.println("Finish!");
		qts.closeQuery();
        
//		qd.printJson();
		
	}

}
