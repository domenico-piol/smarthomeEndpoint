package name.piol;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

//
// Testing with HTTPie commandline:  'http --form POST http://localhost:8080/smarthome/wakeup/MYHOST'
//

@Path("/")
public class SmarthomeWakeup {

    @ConfigProperty(name = "smarthome.wakeup.endpoints")
    String endpoints;

    Map<String, String> endpointsMap = new HashMap<String,String>();


    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/smarthome/wakeup/{host}")
    public String wakeup(@PathParam("host") String host) {
        initializeConfig();
        
        try {
            String[] cmd = {"/bin/bash", "-c", "wol " + endpointsMap.get(host)};
            Process proc = Runtime.getRuntime().exec(cmd);
            proc.waitFor();

	        BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedReader eReader = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            
            String line;
            
            while ((line=reader.readLine()) != null) {
                System.out.println(line);   
            }
            
            while ((line=eReader.readLine()) != null) {
                System.out.println(line);   
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "WAKING UP: " + host + " " + endpointsMap.get(host);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/smarthome/hosts")
    public String hosts() {
        initializeConfig();
        
        return "Hosts: " + endpointsMap.toString();
    }

    private void initializeConfig() {
        String[] strArray = endpoints.split(",");

        for (int i = 0; i < strArray.length; i++) {
            String data = strArray[i];
            String[] keyValue = data.split("=");
            endpointsMap.put(keyValue[0], keyValue[1]);
        }
    }
}
