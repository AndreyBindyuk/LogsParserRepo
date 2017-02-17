package services.logsservice.impl;

import services.logsservice.LogsService;
import services.restcall.RestCallService;
import services.restcall.impl.RestCallServiceImpl;
import utils.LogsUtil;

import java.util.*;

/**
 * Created by Andrey_Bindyuk on 2/16/2017.
 */
public class LogsServiceImpl implements LogsService {
    private LogsUtil logsUtil = new LogsUtil();
    private RestCallService restCallService = new RestCallServiceImpl();

    @Override
    public List getDirectoriesAndFiles(String serverName) {
        List<String> list = new ArrayList<>();
        for (String s : logsUtil.getFilesFromServer(logsUtil.getFullLogsPathByServerName(serverName), serverName)) {
            list.add(s.substring((logsUtil.getFullLogsPathByServerName(serverName) + "/").length(), s.length()));
        }
        return list;
    }

    @Override
    public Set getServerNames() {
        return logsUtil.getServerNames();
    }


    @Override
    public Map<String, List> getLogsResponse(String serverName, String serverPath, String trackingId) {
        String fullPathToProject = logsUtil.getValueProperty(serverName, serverPath).getAbsolutePath();
        String result = restCallService.getServerLogs(serverName, trackingId, fullPathToProject);
        return convertStringToMap(result);
    }

    private Map<String, List> convertStringToMap(String stringResult) {
        Map<String, List> stringListMap = new LinkedHashMap<>();
        List<String> stringArrayList;
        List list;
        List<String> myList = new ArrayList<>(Arrays.asList(stringResult.split("],")));
        for (String s : myList) {
            s += "]";
            String stringLists = s.substring(s.indexOf(" : ") + 3, s.length());
            String str = stringLists.replace("[", "").replace("]", "");
            List<String> filterList = new ArrayList<>(Arrays.asList(str.split(";")));
            list = new ArrayList<>();
            for (String s1 : filterList) {
                stringArrayList = new ArrayList<>(Arrays.asList(s1.split(">,")));
                list.add(stringArrayList);
            }
            stringListMap.put(s.substring(1, s.indexOf(" : ")), list);

        }
        return stringListMap;
    }


}
