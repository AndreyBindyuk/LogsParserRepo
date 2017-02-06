package services;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;
import parser.LogParserByLine;

public class FileService {
    private String resourceName = "config.properties";

    public List getDirectoriesAndFiles(String serverName) {
        List<String> list = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(getlogsPathByServerName(serverName)))) {
            paths.forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    list.add(String.valueOf(filePath.toAbsolutePath()).substring(getlogsPathByServerName(serverName).length()));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
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


    public List getNieaiResponseByTrackingId(String serverName, String path, String trackingId) throws IOException {
        List list = new ArrayList<>();
        if (getValueProperty(serverName, path).getName().contains("WARBA") && (getValueProperty(serverName, path).getName().contains("w4") || getValueProperty(serverName, path).getName().contains("way4"))) {
            list.add(getNieaiResponseByTrackingId(new LogParserByLine().parseLogsFileByLine(getValueProperty(serverName, path), "NIEAIServices"), trackingId));
            list.add(getNieaiResponseByTrackingId(new LogParserByLine().parseLogsFileByLine(getValueProperty(serverName, path), "UFXMsg"), trackingId));
        } else if (getValueProperty(serverName, path).getName().contains("WARBA") && (getValueProperty(serverName, path).getName().contains("tcc") || getValueProperty(serverName, path).getName().contains("adapter"))) {
            list.add(getNieaiResponseByTrackingId(new LogParserByLine().parseLogsFileByLine(getValueProperty(serverName, path), "NIServices"), trackingId));
        }
        return list;//must be simplified

    }

    public Map<String, List> getNieaiResponseByProjectFolder(String serverName, String serverPath, String trackingId) throws IOException {
        Map<String, List> stringListMap = new LinkedHashMap<>();
        List strings = getFilesFromProjectDirectory(getValueProperty(serverName, serverPath).getAbsolutePath());
        for (int i = 0; i < strings.size(); i++) {
            List list = new ArrayList<>();
            if (strings.get(i).toString().contains("WARBA") && (strings.get(i).toString().contains("tcc") || strings.get(i).toString().contains("adapter"))) {
                list.add(getNieaiResponseByTrackingId(new LogParserByLine().parseLogsFileByLine(new File(strings.get(i).toString()), "NIServices"), trackingId));

            } else if (strings.get(i).toString().contains("WARBA") && (strings.get(i).toString().contains("w4") || strings.get(i).toString().contains("way4"))) {
                list.add(getNieaiResponseByTrackingId(new LogParserByLine().parseLogsFileByLine(new File(strings.get(i).toString()), "NIEAIServices"), trackingId));
                list.add(getNieaiResponseByTrackingId(new LogParserByLine().parseLogsFileByLine(new File(strings.get(i).toString()), "UFXMsg"), trackingId));
            }
            stringListMap.put(strings.get(i).toString(), list);
            stringListMap.entrySet().removeIf(entry -> Objects.equals(entry.getValue().get(0).toString(), "[]"));

        }
        return stringListMap;
    }

    public File getValueProperty(String serverName, String path) throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        props.load(loader.getResourceAsStream(resourceName));
        return new File(props.getProperty(serverName) + path);
    }

    private List<String> getNieaiResponseByTrackingId(List<String> nieaiLists, String trackingId) {
        List<String> stringList = new ArrayList<>();
        for (String s : nieaiLists) {
            if (s.contains(trackingId)) {
                stringList.add(s);
            }

        }
        return stringList;
    }

    private List getFilesFromProjectDirectory(String projectPath) {
        List list = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(projectPath))) {
            paths.forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    list.add(String.valueOf(filePath));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


}
