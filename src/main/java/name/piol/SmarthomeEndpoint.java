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
// Start the container with:  
//      podman run -d --name smarthome-endpoint --network host -e smarthome.wakeup.endpoints='HEARTOFGOLD=08:BF:B8:01:33:17,IMAC=10:DD:B1:BD:FE:C2' --add-host=ilo-dl360-gen8.piol.local:192.168.1.15 localhost/smarthome-endpoint:v2    
//
// Testing with HTTPie commandline:  
//      http --form POST http://localhost:8080/smarthome/wakeup/MYHOST
//


@Path("/")
public class SmarthomeEndpoint {

    @ConfigProperty(name = "smarthome.wakeup.endpoints")
    String endpoints;

    Map<String, String> endpointsMap = new HashMap<String,String>();


    private void initializeConfig() {
        String[] strArray = endpoints.split(",");

        for (int i = 0; i < strArray.length; i++) {
            String data = strArray[i];
            String[] keyValue = data.split("=");
            endpointsMap.put(keyValue[0], keyValue[1]);
        }
    }



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


    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/smarthome/homelab/pushpowerbutton")
    public String homelabPushPowerButton() {
        initializeConfig();

        String HTTPIE_CMD = "https --verify=no -a Administrator:Y5KK8KFY --ignore-stdin POST https://ilo-dl360-gen8.piol.local/redfish/v1/Systems/1/Actions/ComputerSystem.Reset/ ResetType=PushPowerButton";
        
        try {
            String[] cmd = {"/bin/bash", "-c", HTTPIE_CMD};
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

        return "POWER BUTTON on HOMELAB pressed (powering down gracefully)";
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/smarthome/homelab/poweron")
    public String homelabPowerOn() {
        initializeConfig();

        String HTTPIE_CMD = "https --verify=no -a Administrator:Y5KK8KFY --ignore-stdin POST https://ilo-dl360-gen8.piol.local/redfish/v1/Systems/1/Actions/ComputerSystem.Reset/ ResetType=On";
        
        try {
            String[] cmd = {"/bin/bash", "-c", HTTPIE_CMD};
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

        return "HOMELAB powering on";
    }


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/smarthome/homelab/powerstate")
    public String homelabGetPowerState() {
        initializeConfig();

        String HTTPIE_CMD = "https --verify=no -a Administrator:Y5KK8KFY --ignore-stdin --no-stream GET https://ilo-dl360-gen8.piol.local/redfish/v1/Systems/1/ | jq '.PowerState'";
        String state = "UNKNOWN";
        
        try {
            String[] cmd = {"/bin/bash", "-c", HTTPIE_CMD};
            Process proc = Runtime.getRuntime().exec(cmd);

            proc.waitFor();

	        BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedReader eReader = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            
            String line = "";
            
            while ((line = reader.readLine()) != null) {
                state = line;
            }
            
            while ((line = eReader.readLine()) != null) {
                System.out.println(line);   
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "HOMELAB power state is: " + state;
    }

}
