package entities;

public class FileInfo {
    private String domainName;
    private String projectName;
    private String logType;

    public FileInfo(){}

    public FileInfo(String domainName, String projectName, String logType) {
        this.domainName = domainName;
        this.projectName = projectName;
        this.logType = logType;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }
}
