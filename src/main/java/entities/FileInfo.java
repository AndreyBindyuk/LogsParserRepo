package entities;

public class FileInfo {
    private String serverName;
    private String logPath;
    private String trackingId;

    public FileInfo(){}

    public FileInfo(String serverName, String logPath, String trackingId) {
        this.serverName = serverName;
        this.logPath = logPath;
        this.trackingId = trackingId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }
}
