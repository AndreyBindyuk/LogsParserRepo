package network.sftp;

import com.jcraft.jsch.*;
import exeption.SFTPURIExceplion;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    public List <String> getTreeFileList(String rootPath) throws JSchException {
        Channel channel = session.openChannel("sftp");
        channel.connect();
        return getTreeList(rootPath, channel);
    }


    public String getContent(String path) throws SftpException, SFTPURIExceplion, JSchException, IOException {
        return getFileContent(path);
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

    public void close(){
        session.disconnect();
    }

    private String getFileContent(String path) throws SftpException, SFTPURIExceplion, JSchException, IOException {

        BufferedInputStream in = getBufferedContent(path);
        byte[] contents = new byte[1024];

        int bytesRead = 0;
        String strFileContents = null;
        while((bytesRead = in.read(contents)) != -1) {
            strFileContents += new String(contents, 0, bytesRead);
        }
        return  strFileContents;
    }



    private BufferedInputStream getBufferedContent(String path) throws SFTPURIExceplion, JSchException, SftpException, IOException {
            String fileName = getFilename(path);
            ChannelSftp channelSftp = getSFTPChannel(session, path);
            return new BufferedInputStream(channelSftp.get(fileName));

    }

    private boolean validateURL(String connectionURL){
        return connectionURL.matches(URL_RATTERN);
    }

    private String getHost(String connectionURL){
        return connectionURL.substring(connectionURL.lastIndexOf('@')+1, connectionURL.length());
    }

    private String getUser(String connectionURL){
        return connectionURL.substring(connectionURL.lastIndexOf('/')+1, connectionURL.lastIndexOf(':'));
    }

    private String getPassword(String connectionURL){
        return connectionURL.substring(connectionURL.lastIndexOf(':')+1, connectionURL.lastIndexOf('@'));
    }

    private String getFilename(String path){
        return path.substring(path.lastIndexOf('/')+1, path.length());
    }

    private String getAbsolutePath(String path){
        return path.substring(0,path.lastIndexOf('/')+1);
    }

    private Session getSession(String user, String password, String host) throws JSchException {
        Session session = null;
        jsch = new JSch();
        session = jsch.getSession(user,host,22);
        session.setPassword(password);
        session.setConfig(properties);
        session.connect();
        return session;
    }


    private ChannelSftp getSFTPChannel(Session session, String path) throws JSchException, SftpException {
        Channel channel = null;
        ChannelSftp channelSftp = null;
        channel = session.openChannel("sftp");
        channel.connect();
        channelSftp = (ChannelSftp)channel;
        channelSftp.cd(getAbsolutePath(path));

        return channelSftp;
    }

    private List<String> getTreeList(String path, Channel channel){
        List<String> treeFileList = new ArrayList<>();
        try {
            ChannelSftp sftp = (ChannelSftp) channel;
            java.util.Vector<ChannelSftp.LsEntry> flLst = sftp.ls(path);

            for(int i = 0; i<flLst.size() ;i++){
                ChannelSftp.LsEntry entry = flLst.get(i);
                SftpATTRS attr = entry.getAttrs();
                if(!attr.isDir()){
                    treeFileList.add(path + "/" + entry.getFilename());
                }

                if(attr.isDir() && !entry.getFilename().equals("..") && !entry.getFilename().equals(".")){
                    treeFileList.addAll(getTreeList(path+"/"+entry.getFilename(),channel));
                }
            }

        } catch (SftpException e) {
            return treeFileList;
        }
        return treeFileList;
    }

}
