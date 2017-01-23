package controller;

import entities.FileInfo;
import entities.ServerInfo;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import services.FileService;

import java.io.IOException;
import java.util.List;

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
}
