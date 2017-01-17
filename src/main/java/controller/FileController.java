package controller;

import entities.FileInfo;
import org.springframework.web.bind.annotation.*;
import services.FileService;

import java.io.IOException;

@RestController
public class FileController {

    private FileService fileService = new FileService();

    @CrossOrigin
    @RequestMapping(value = "/countriess", method = RequestMethod.POST, headers = "Accept=application/xml")
    public String getFIle(@RequestBody FileInfo fileInfo) throws IOException {
        return fileService.getFile(fileInfo);
    }
}
