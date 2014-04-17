import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.rallydev.rest.RallyRestApi;


public class RallyConnector {
	String host = "https://rally1.rallydev.com";
    String username = "tttvan@tma.com.vn";
    String password = "Ktn@051628";
    String wsapiVersion = "v2.0";
    String RingoProjectRef = "/project/7803686457";
    String SoftswitchProjectRef = "/project/9808582196";
    String SkyProjectRef = "/project/7803241224";
//    String workspaceRef = "/workspace/ShoreTel"; 
    String applicationName = "RestExample_GetDefect";
    RallyRestApi restApi;
    
    public RallyConnector() throws URISyntaxException{
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

}
