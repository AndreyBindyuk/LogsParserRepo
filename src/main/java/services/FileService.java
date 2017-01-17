package services;

import entities.FileInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileService {

    public String getFile(FileInfo fileInfo) throws IOException {
        File fullFileInfo = new File(fileInfo.getDomainName() + "\\" + fileInfo.getProjectName() +"\\"+ fileInfo.getLogType());

        FileInputStream fis = new FileInputStream(fullFileInfo);
        byte[] data = new byte[(int) fullFileInfo.length()];
        fis.read(data);
        fis.close();
        return new String(data);
    }
}
