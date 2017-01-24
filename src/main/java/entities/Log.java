package entities;

import java.util.Map;

/**
 * Created by Sergey_Chernikov on 1/20/2017.
 */
public class Log {

private Map<String, SystemLog> logLines;

    public Map<String, SystemLog> getLogLines() {
        return logLines;
    }

    public void setLogLines(Map<String, SystemLog> logLines) {
        this.logLines = logLines;
    }
}
