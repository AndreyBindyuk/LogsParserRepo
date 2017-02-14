package controller;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import exeption.SFTPURIExceplion;
import org.springframework.web.bind.annotation.*;
import services.FileService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class FileController {

    private FileService fileService = new FileService();

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
    @RequestMapping(value = "/bankLogs", method = RequestMethod.GET,params = {"serverName","bankPath","trackingId"},headers = "Accept=application/json")
    public Map<String,List> getResponseByProjectFolder(@RequestParam(value = "serverName") String serverName,@RequestParam(value = "bankPath") String bankPath,@RequestParam(value = "trackingId") String trackingId) throws IOException, SftpException, JSchException, SFTPURIExceplion {
        return fileService.getResponseByProjectFolder(serverName, bankPath, trackingId);
    }
}
