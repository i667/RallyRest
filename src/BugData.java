import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
//	String url = "jdbc:mysql://localhost:3306/test";
//    String user = "root";
//    String password = "";
    boolean connected = true;
//    Connection conn = null;
//    Statement st = null;
//    ResultSet rs = null;
	
	public BugData(){
		hash = new HashMap<>();
		for(String str : colheaders){
			hash.put(str, null);
		}
//		try {
//			conn = DriverManager.getConnection(url, user, password);
//			st = conn.createStatement();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			connected = false;
//		}
	}
	
	public void setData(String...strings){
		for(int i = 0; i < strings.length; i++){
			hash.put(colheaders[i], strings[i]);
		}
		values = strings;
	}
	
	public String formInsertString(){
		
		StringBuffer buf = new StringBuffer("INSERT INTO ost_bugs(");
		StringBuffer val = new StringBuffer(" values(");
		for(String str : hash.keySet()){
			if(hash.get(str) != null){
				buf.append("`"+str+"`" + ",");
				val.append(hash.get(str) + ",");
			}
			
		}
		buf.deleteCharAt(buf.length() - 1);
		buf.append(")");
		val.deleteCharAt(val.length() - 1);
		val.append(")");
		return buf.toString() + val.toString();
	}
	
	public String formUpdateString(){
		
		String[] updateField = {
			"Priority",
			"Severity",
			"State",
			"Rejected",
			"Foundin",
			"Release",
			"Tag",
			"Note"
		};
		StringBuffer buf = new StringBuffer("UPDATE ost_bugs SET ");
//		buf.append(String.format("Severity = \'%s\', `State`=\'%s\', Foundin=\'%s\', Tag=\'%s\', Note=\'%s\'", args))
		for(String str : updateField){
			if(hash.get(str) != null){
				buf.append("`"+str+"`" + "=" + hash.get(str) + ",");
			}
			
		}
		buf.deleteCharAt(buf.length() -1);
		buf.append(" WHERE Bugid=" + hash.get("Bugid"));
		
		
		return buf.toString();
	}
	
	public void insertToDb(Connection conn){
//		if(!connected){
//			System.out.println("Connect database error!");
//			return;
//		}
		Statement st = null;
		try {
			st = conn.createStatement();
			String insertStr = formInsertString();
			System.out.println("Executr this:\n" + insertStr);
			st.execute(formInsertString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(st != null){
				try {
					st.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public void updateToDb(Connection conn){
		Statement st = null;
		try {
			st = conn.createStatement();
			String updateStr = formUpdateString();
			System.out.println("Execute this:\n" + updateStr);
			st.executeUpdate(updateStr);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(st != null){
				try {
					st.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public boolean exists(Connection conn){
//		if(!connected){
//			System.out.println("Connect database error!");
//			return false;
//		}
		ResultSet res = null;
		try {
			Statement st = conn.createStatement();
			res = st.executeQuery("SELECT Bugid FROM ost_bugs WHERE bugid="+hash.get("Bugid"));
			if(res.next()){
				res.close();
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
//	public void closeConnection(){
//		
//		try {
//			if(st != null){
//				st.close();
//			}
//			if(conn != null){
//				conn.close();
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	
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
		if(state.equalsIgnoreCase("Rejected")){
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
		hash.put("Note", String.format("'%s'", note));
	}
	public String getNote(){
		return hash.get("Note");
	}
	
	public void setRejected(String rej){
		hash.put("Rejected", rej);
	}
	
	public void saveWithJson(JsonObject def){
		setId(def.get("FormattedID").getAsString());
		setName(def.get("Name").getAsString().replace("\'", "\\\'"));
		setCreationDate(convertDate(def.get("CreationDate").getAsString()));
		setPriority(def.get("Priority").getAsString());
		setSeverity(def.get("Severity").getAsString());
		setState(def.get("State").getAsString());
		
		
//		JsonObject foundObj = def.get("FoundInBuild").getAsJsonObject();
//		String found = "";
		if(!def.get("FoundInBuild").toString().equalsIgnoreCase("null")){
			String found = def.get("FoundInBuild").getAsString();
			if(found.length() < 40){
				setFoundin(found);
			}
		}
	
		if(!def.get("SubmittedBy").toString().equalsIgnoreCase("null")){
			setSubmitter(def.get("SubmittedBy").getAsJsonObject().get("_refObjectName").getAsString());
		}
				
		if(!def.get("Release").toString().equalsIgnoreCase("null")){
			JsonObject releaseObj = def.get("Release").getAsJsonObject();
			setRelease(releaseObj.get("_refObjectName").getAsString());
		}
		
	
		
		JsonObject tagObj = def.get("Tags").getAsJsonObject();
		
		if(tagObj.get("Count").getAsInt() !=0 ){
			JsonArray tagArr = tagObj.get("_tagsNameArray").getAsJsonArray();
			setTag(getTagString(tagArr));
		}
		String note = def.get("Notes").getAsString();
		if(note.length() < 265){
			note = note.replace("\'", "\\\'");
			setNote(note);
		}
		
			
		
	}
	public String convertDate(String strDate){
		String[] array = strDate.split("T");
//		StringBuffer strbuf = new StringBuffer(array[1]);
//		strbuf.deleteCharAt(strbuf.length()-1);
		return array[0];
		
	}
	public String getTagString(JsonArray arrayObj) {
		StringBuffer sbuf = new StringBuffer();
		for(int i =0; i < arrayObj.size(); i++){
			sbuf.append(arrayObj.get(i).getAsJsonObject().get("Name").getAsString() +",");
		}
		sbuf.deleteCharAt(sbuf.length()-1);
		return sbuf.toString();
	}

}
