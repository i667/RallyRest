import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import com.rallydev.rest.RallyRestApi;


public class RallyConnector {
	String host = "https://rally1.rallydev.com";
    String username = "tttvan@tma.com.vn";
    String password = "Ktn@051628";
    String wsapiVersion = "v2.0";
    String applicationName = "RestExample_GetDefect";
    RallyRestApi restApi;
    Properties configs;
    
    public RallyConnector() throws URISyntaxException{
    	configs = new Properties();
    	readConfig();
    	restApi = new RallyRestApi(new URI(host), username, password);
		restApi.setWsapiVersion(wsapiVersion);
		restApi.setApplicationName(applicationName);
    }
    public RallyRestApi getRally(){
    	return restApi;
    }
    public void closeConnection(){
    	try {
			restApi.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void readConfig(){
//		configs = new Properties();
		FileInputStream fis = null;
		File fileconfig = new File("config_rally.cfg");
		if(!fileconfig.exists()){
			createDefaultConfigs();
			return;
		}
		
		try{
			fis = new FileInputStream("config_rally.cfg");
			configs.load(fis);
			
			String conf_user = configs.getProperty("username");
			String conf_pass = configs.getProperty("username");
			
			if(conf_user == null || conf_user.isEmpty() || conf_pass == null || conf_pass.isEmpty()){
				return;
			}
			
			this.username = configs.getProperty("username");
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
			outs = new FileOutputStream("config_rally.cfg");
			configs.setProperty("username", "");
			configs.setProperty("password", "");
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
