package entities;

import java.util.List;
import java.util.Map;

/**
 * Created by User on 24.01.2017.
 */
public class SystemLog {

    private  Map<XMLType, List<Line>> logLine;

    public Map<XMLType, List<Line>> getLogLine() {
        return logLine;
    }

    public void setLogLine(Map<XMLType, List<Line>> logLine) {
        this.logLine = logLine;
    }
}
