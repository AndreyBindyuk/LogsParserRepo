package network.sftp;

import com.jcraft.jsch.*;
import exeption.SFTPURIExceplion;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Sergey_Chernikov on 1/23/2017.
 */
public class SFTPConnector {

    private final String URL_RATTERN = "sftp:\\/\\/.*\\:.*\\@.*";
    private Properties properties;
    private JSch jsch;
    private Session session;

    public SFTPConnector() {
        properties = new Properties();
        properties.put("StrictHostKeyChecking", "no");
    }

    public String get(String connectionURL, String path) throws SftpException, SFTPURIExceplion, JSchException, IOException {
        SFTPConnector sftpConnector = new SFTPConnector();

        sftpConnector.open(connectionURL);

        BufferedInputStream in = sftpConnector.getFileContent(path);
        byte[] contents = new byte[1024];

        int bytesRead = 0;
        String strFileContents = null;
        while((bytesRead = in.read(contents)) != -1) {
            strFileContents += new String(contents, 0, bytesRead);
        }

        sftpConnector.close();
        return  strFileContents;
    }

    public void open(String connectionURL) throws SFTPURIExceplion, JSchException, SftpException, IOException {

        if (!validateURL(connectionURL)){
            throw new SFTPURIExceplion("Incorrect Connection URL");
        }

        String host = getHost(connectionURL);
        String user = getUser(connectionURL);
        String password = getPassword(connectionURL);
        session = getSession(user, password, host);
    }

    public BufferedInputStream getFileContent(String path) throws SFTPURIExceplion, JSchException, SftpException, IOException {
            String fileName = getFilename(path);
            ChannelSftp channelSftp = getSFTPChannel(session, path);
            return new BufferedInputStream(channelSftp.get(fileName));

    }

    public void close(){
        session.disconnect();
    }

    public boolean validateURL(String connectionURL){
        return connectionURL.matches(URL_RATTERN);
    }

    public String getHost(String connectionURL){
        return connectionURL.substring(connectionURL.lastIndexOf('@')+1, connectionURL.length());
    }

    public String getUser(String connectionURL){
        return connectionURL.substring(connectionURL.lastIndexOf('/')+1, connectionURL.lastIndexOf(':'));
    }

    public String getPassword(String connectionURL){
        return connectionURL.substring(connectionURL.lastIndexOf(':')+1, connectionURL.lastIndexOf('@'));
    }

    public String getFilename(String path){
        return path.substring(path.lastIndexOf('/')+1, path.length());
    }

    public String getAbsolutePath(String path){
        return path.substring(0,path.lastIndexOf('/')+1);
    }

    public Session getSession(String user, String password, String host) throws JSchException {
        Session session = null;
        jsch = new JSch();
        session = jsch.getSession(user,host,22);
        session.setPassword(password);
        session.setConfig(properties);
        session.connect();
        return session;
    }


    public ChannelSftp getSFTPChannel(Session session, String path) throws JSchException, SftpException {
        Channel channel = null;
        ChannelSftp channelSftp = null;
        channel = session.openChannel("sftp");
        channel.connect();
        channelSftp = (ChannelSftp)channel;
        channelSftp.cd(getAbsolutePath(path));

        return channelSftp;
    }
}
