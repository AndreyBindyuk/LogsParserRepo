package controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * Created by Andrey_Bindyuk on 2/13/2017.
 */
@RestController
public class ServerLogsController {

    public String getLogsFromServer(String serverName, String trackingId, String fullPathToProject){
        final String uri = "http://"+serverName+":3383/getLogs/?"+"trackingId="+trackingId + "&" + "fullPathToProject="+fullPathToProject;

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        System.out.println(result.getBody());
        return result.getBody();
    }

}
