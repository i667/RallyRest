import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class BugData {
	
	HashMap<String, String> hash;
	public static String[] colheaders = {
		"Bugid",
		"Name",
		"CreationDate",
		"Priority",
		"Severity",
		"State",
		"Rejected",
		"Foundin",
		"Submitter",
		"Release",
		"Tag",
		"Note"
	};
	String[] values;
	Connection conn;
	
	public BugData(){
		hash = new HashMap<>();
		for(String str : colheaders){
			hash.put(str, null);
		}
	}
	public BugData(Connection _con){
		hash = new HashMap<>();
		for(String str : colheaders){
			hash.put(str, null);
		}
		conn = _con;
		
	}
	
	public void setData(String...strings){
//		for(int i = 0; i < strings.length; i++){
//			hash.put(colheaders[i], strings[i]);
//		}
		values = strings;
	}
	
	public String formInsertString(){
		
		StringBuffer buf = new StringBuffer("INSERT INTO ost_bugs(");
		StringBuffer val = new StringBuffer(" values(");
//		for(String str : colheaders){
//			buf.append(str + ",");
//		}
		for(String str : hash.keySet()){
			if(hash.get(str) != null){
				buf.append(str + ",");
				val.append(hash.get(str) + ",");
			}
			
		}
		buf.deleteCharAt(buf.length() - 1);
		buf.append(")");
		val.deleteCharAt(val.length() - 1);
		val.append(")");
		return buf.toString() + val.toString();
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
	
	public void setId(String id){
		hash.put("Bugid", String.format("\'%s\'", id));
	}
	public void setName(String name){
		hash.put("Name", String.format("\'%s\'", name));
	}
	public void setCreationDate(String date){
		hash.put("CreationDate", String.format("\'%s\'", date));
	}
	public void setPriority(String pri){
		hash.put("Priority", String.format("\'%s\'", pri));
	}
	public void setSeverity(String sev){
		hash.put("Severity", String.format("\'%s\'", sev));
	}
	public void setState(String state){
		hash.put("State", String.format("\'%s\'", state));
		if(state == "Rejected"){
			setRejected("1");
		}
		else{
			setRejected("0");
		}
	}
	public void setFoundin(String found){
		hash.put("Foundin", String.format("\'%s\'", found));
	}
	public void setSubmitter(String submitter){
		hash.put("Submitter", String.format("\'%s\'", submitter));
	}
	public void setRelease(String release){
		hash.put("Release", String.format("\'%s\'", release));
	}
	public void setTag(String tag){
		hash.put("Tag", String.format("\'%s\'", tag));
	}
	public void setNote(String note){
		hash.put("Note", String.format("\'%s\'", note));
	}
	public void setRejected(String rej){
		hash.put("Rejected", rej);
	}

}
