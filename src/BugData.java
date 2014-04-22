import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;






public class BugData {
	
//	HashMap<String, String> hash;
	public static String[] colheaders = {
		"Bugid",
		"Name",
		"CreationDate",
		"Priority",
		"Severity",
		"State",
		"Foundin",
		"Submitter",
		"Release",
		"Tag",
		"Note"
	};
	String[] values;
	Connection conn;
	
	public BugData(Connection _con){
//		hash = new HashMap<>();
//		for(String str : colheaders){
//			hash.put(str, null);
		conn = _con;
//		}
		
	}
	
	public void setData(String...strings){
//		for(int i = 0; i < strings.length; i++){
//			hash.put(colheaders[i], strings[i]);
//		}
		values = strings;
	}
	
	public String formInsertString(){
		
		StringBuffer buf = new StringBuffer("INSERT INTO ost_bugs(");
		for(String str : colheaders){
			buf.append(str + ",");
		}
		buf.deleteCharAt(buf.length() - 1);
		buf.append(") values(");
		
//		Collection<String> vals = hash.values();
		for(String str : values){
//			System.out.println(str);
			buf.append(str + ",");
		}
		buf.deleteCharAt(buf.length() -1);
		buf.append(")");
		return buf.toString();
	}
	
	public void pushToDb(){
		try {
			Statement st = conn.createStatement();
			st.execute(formInsertString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public boolean exists(){
		ResultSet rs = null;
		try {
			Statement st = conn.createStatement();
			rs = st.executeQuery("SELECT bugid FROM ost_bugs WHERE bugid="+values[0]);
			if(rs.next()){
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	
	
	

}
