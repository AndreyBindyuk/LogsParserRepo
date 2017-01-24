package parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by Sergey_Chernikov on 1/20/2017.
 */
public class FileUtil {

    public File getFileOverSFTP(String login, String password, String host,String port){
        return new File("");
    }

    public File getFile(String path){
        return new File(path);
    }

    public String getFileContent(File file) throws FileNotFoundException {
        return new Scanner(file).useDelimiter("\\Z").next();
    }

    public String getContent(String path) throws FileNotFoundException {
        return getFileContent(getFile(path));
    }

    public String getContentg(String path) throws FileNotFoundException {
        return new FileUtil().getContent(path);
    }
}
