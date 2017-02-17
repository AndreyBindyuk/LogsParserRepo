package controller;

import org.springframework.web.bind.annotation.*;
import services.logsservice.LogsService;
import services.logsservice.impl.LogsServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class FileController {
    private LogsService logsService = new LogsServiceImpl();

    @RequestMapping(value = "/directories", method = RequestMethod.GET, params = "serverName", headers = "Accept=application/json")
    public List getDirectoriesAndFolders(@RequestParam(value = "serverName") String serverName) {
        return logsService.getDirectoriesAndFiles(serverName);
    }

    @RequestMapping(value = "/serverNames", method = RequestMethod.GET, headers = "Accept=application/json")
    public Set getServerNames() {
        return logsService.getServerNames();
    }

    @RequestMapping(value = "/bankLogs", method = RequestMethod.GET, params = {"serverName", "bankPath", "trackingId"}, headers = "Accept=application/json")
    public Map<String, List> getResponseByProjectFolder(@RequestParam(value = "serverName") String serverName, @RequestParam(value = "bankPath") String bankPath, @RequestParam(value = "trackingId") String trackingId) {
//        System.out.println(serverName);
//        System.out.println(trackingId);
        return logsService.getLogsResponse(serverName, bankPath, trackingId);
    }
}
