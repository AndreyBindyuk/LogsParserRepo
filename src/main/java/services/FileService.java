package services;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import controller.ServerLogsController;
import exeption.SFTPURIExceplion;
import network.sftp.SFTPConnector;
import parser.LogParserByLine;

public class FileService {
    private String resourceName = "config.properties";
    private ServerLogsController serverLogsController = new ServerLogsController();

    public List getDirectoriesAndFiles(String serverName) {
        List<String> list = new ArrayList<>();
        for(String s:getFilesFromServer(getlogsPathByServerName(serverName),serverName)){
            list.add(s.substring((getlogsPathByServerName(serverName)+"/").length(),s.length()));
        }
        return list;
    }

    private String getlogsPathByServerName(String serverName) {
        String value = "";
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
            props.load(resourceStream);
            value = props.getProperty(serverName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }


    public Set getServerNames() throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        props.load(loader.getResourceAsStream(resourceName));
        return props.keySet();
    }

    public Map<String, List> getResponseByProjectFolder(String serverName, String serverPath, String trackingId) throws IOException {
        Map<String, List> stringListMap = new LinkedHashMap<>();
        List<String> stringArrayList;
        List list;
        String fullPathToProject = getValueProperty(serverName, serverPath).getAbsolutePath();
        String result = String.valueOf(serverLogsController.getLogsFromServer(serverName,trackingId,fullPathToProject));
        List<String> myList = new ArrayList<>(Arrays.asList(result.split("],")));
        for(String s: myList){
            s += "]";
            String stringLists = s.substring(s.indexOf(" : ") + 3, s.length());
            String str = stringLists.replace("[","").replace("]","");
            List<String> filterList = new ArrayList<>(Arrays.asList(str.split(";")));
            list = new ArrayList<>();
            for(String s1: filterList){
                stringArrayList = new ArrayList<>(Arrays.asList(s1.split(">,")));
                list.add(stringArrayList);
            }
            stringListMap.put(s.substring(1,s.indexOf(" : ")),list);

        }
        stringListMap.entrySet().removeIf(entry -> Objects.equals(entry.getValue().get(0).toString(), "[]"));
        return stringListMap;
    }

    public File getValueProperty(String serverName, String path) throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        props.load(loader.getResourceAsStream(resourceName));
        return new File(props.getProperty(serverName) +"/"+ path);
    }


    private List<String> getFilesFromServer(String bankPath,String serverName){
        SFTPConnector sftpConnector = new SFTPConnector();
        List list = null;
        try {
            sftpConnector.open("sftp://nitibuat:Dec@2016#"+serverName);
            list = sftpConnector.getTreeFileList(bankPath.replace("\\","/"));
        } catch (SFTPURIExceplion | JSchException | IOException | SftpException sftpuriExceplion) {
            sftpuriExceplion.printStackTrace();
        }finally {
            sftpConnector.close();
        }
        return list;
    }


}
