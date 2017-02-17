package services.restcall;

/**
 * Created by Andrey_Bindyuk on 2/16/2017.
 */
public interface RestCallService {

    public String getServerLogs(String serverName, String trackingId, String fullPathToProject);
}
