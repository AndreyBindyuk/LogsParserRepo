package services;

import entities.FileInfo;
import entities.ServerInfo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class FileService {

    private String resourceName = "config.properties";

//    public String getFile(FileInfo fileInfo) throws IOException {
////        File fullFileInfo = new File(fileInfo.getDomainName() + "\\" + fileInfo.getProjectName() +"\\"+ fileInfo.getLogType());
//
//        FileInputStream fis = new FileInputStream(fullFileInfo);
//        byte[] data = new byte[(int) fullFileInfo.length()];
//        fis.read(data);
//        fis.close();
//        return new String(data);
//    }

    public List getDirectoriesAndFiles(String serverName){
        List<String> list = new ArrayList<>();
        try(Stream<Path> paths = Files.walk(Paths.get(getlogsPathByServerName(serverName)))) {
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

    private String getlogsPathByServerName(String serverName){
        String value = "";
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


    public Set getServerNames() throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        props.load(loader.getResourceAsStream(resourceName));
        return props.keySet();
    }


    public List<String> getNieaiResponseByTrackingId(String serverName,String path, String trackingId) throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        props.load(loader.getResourceAsStream(resourceName));
        File file = new File(props.getProperty(serverName)+path);
        return getNieaiResponseByTrackingId(convertEachLineToString(file),trackingId);
    }


    private List<String> convertEachLineToString(File file){
        List<String> nieai = new ArrayList<>();
        List<String> common = new ArrayList<>();
        StringBuilder listString = new StringBuilder();
        String startNieai = "<NIEAIServices>";
        String endNieai = "</NIEAIServices>";

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.startsWith(startNieai) && line.endsWith(endNieai)){
                    nieai.add(line.substring(0, line.length()));
                }else if(line.startsWith(startNieai)  && !line.endsWith(endNieai)){
                    while (((line = br.readLine()) != null) && !line.endsWith(endNieai)){
                        common.add(line);
                    }
                    for(String s: common){
                        if(!s.equals("    </body>")){
                            listString.append(s);
                        }else {
                            nieai.add(String.valueOf(startNieai+listString+"</body>"+endNieai).replaceAll("\\s",""));
                        }
                    }
                    common.clear();
                    listString.setLength(0);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nieai;
    }

    private List<String> getNieaiResponseByTrackingId(List<String> nieaiLists, String trackingId){
        List<String> stringList = new ArrayList<>();
        for(String s: nieaiLists){
            if(s.contains(trackingId)){
                stringList.add(s);
            }

        }
        return stringList;
    }
}
