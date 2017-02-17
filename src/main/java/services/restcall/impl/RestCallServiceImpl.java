package services.restcall.impl;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import services.restcall.RestCallService;

import java.util.Arrays;

/**
 * Created by Andrey_Bindyuk on 2/16/2017.
 */
public class RestCallServiceImpl implements RestCallService {

    @Override
    public String getServerLogs(String serverName, String trackingId, String fullPathToProject) {
        final String uri = "http://"+serverName+":3383?trackingId="+trackingId + "&fullPathToProject="+fullPathToProject;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        return result.getBody();
    }
}
