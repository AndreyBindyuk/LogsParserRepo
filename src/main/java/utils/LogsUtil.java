package utils;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import exeption.SFTPURIExceplion;
import network.sftp.SFTPConnector;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Created by Andrey_Bindyuk on 2/16/2017.
 */
public class LogsUtil {
    private String resourceName = "config.properties";

    public File getValueProperty(String serverName, String path) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        try {
            props.load(loader.getResourceAsStream(resourceName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new File(props.getProperty(serverName) +"/"+ path);
    }

    public String getFullLogsPathByServerName(String serverName) {
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

    public List<String> getFilesFromServer(String bankPath,String serverName){
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

    public Set getServerNames() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        try {
            props.load(loader.getResourceAsStream(resourceName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props.keySet();
    }
}
