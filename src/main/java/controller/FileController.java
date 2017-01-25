package controller;

import entities.FileInfo;
import entities.ServerInfo;
import org.springframework.web.bind.annotation.*;
import services.FileService;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
public class FileController {

    private FileService fileService = new FileService();

    @CrossOrigin
    @RequestMapping(value = "/files", method = RequestMethod.POST, headers = "Accept=application/xml")
    public String getFIle(@RequestBody FileInfo fileInfo) throws IOException {
        return fileService.getFile(fileInfo);
    }

    @CrossOrigin
    @RequestMapping(value = "/directories", method = RequestMethod.GET,params = "serverName",headers = "Accept=application/json")
    public List getDirectoriesAndFolders(@RequestParam(value = "serverName") String serverName) throws IOException {
        return fileService.getDirectoriesAndFiles(serverName);
    }

    @CrossOrigin
    @RequestMapping(value = "/serverNames", method = RequestMethod.GET,headers = "Accept=application/json")
    public Set getServerNames() throws IOException {
        return fileService.getServerNames();
    }

    @CrossOrigin
    @RequestMapping(value = "/nieaiLogs", method = RequestMethod.GET,params = {"logPath","trackingId"},headers = "Accept=application/json")
    public List getNieaiResponseByTrackingId(@RequestParam(value = "logPath") String logPath,@RequestParam(value = "trackingId") String trackingId) throws IOException {
        return fileService.getNieaiResponseByTrackingId(logPath,trackingId);
    }
}
