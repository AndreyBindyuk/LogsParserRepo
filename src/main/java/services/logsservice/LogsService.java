package services.logsservice;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Andrey_Bindyuk on 2/16/2017.
 */
public interface LogsService {

    public List getDirectoriesAndFiles(String serverName);
    public Set getServerNames();
    public Map<String, List> getLogsResponse(String serverName, String serverPath, String trackingId);

}
