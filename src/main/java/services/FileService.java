package services;

import entities.FileInfo;
import entities.ServerInfo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

public class FileService {

    public String getFile(FileInfo fileInfo) throws IOException {
        File fullFileInfo = new File(fileInfo.getDomainName() + "\\" + fileInfo.getProjectName() +"\\"+ fileInfo.getLogType());

        FileInputStream fis = new FileInputStream(fullFileInfo);
        byte[] data = new byte[(int) fullFileInfo.length()];
        fis.read(data);
        fis.close();
        return new String(data);
    }

    public List getDirectoriesAndFiles(ServerInfo serverInfo){
        List<String> list = new ArrayList<>();
        try(Stream<Path> paths = Files.walk(Paths.get(getlogsPathByServerName(serverInfo.getServerName())))) {
            paths.forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    list.add("file: " + filePath);
                }else {
                    list.add("directory: " + filePath);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private String getlogsPathByServerName(String serverName){
        String value = "";
        String resourceName = "config.properties";
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        try(InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
            props.load(resourceStream);
            value = props.getProperty(serverName);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }
}
