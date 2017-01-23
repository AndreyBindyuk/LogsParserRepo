package parser;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.*;
import exeption.ParserXMLException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sergey_Chernikov on 1/20/2017.
 */
public class LogParser {

    private static final String START_LINE_PATTERN = "\\[(\\d{4})-(\\d{2})-(\\d{2}) (\\d{2}):(\\d{2}):(\\d{2},(\\d{3}))\\]";
    private static final String LINE_PATTERN = "((DEBUG).*(\\<\\?xml.*\\>))";

    public String minimiseLog(String log) throws FileNotFoundException {
        return log.replaceAll("\\n", "").replaceAll(START_LINE_PATTERN, "\n");
    }

    public Line getLogLine(String logType, String msg) throws ParserXMLException {
        Line line = new Line();
        XMLUtil xmlUtil = new XMLUtil();
        Document document = xmlUtil.createDocument(msg);
        line.setFlowId(getTrackingId(document));
        line.setXmlType(getXMLType(document));
        line.setMessage(msg);
        line.setLevel(logType);
        return line;
    }

    public String getTrackingId(Document document){
        String trackId;
        NodeList srcMsgId = document.getElementsByTagName("SrcMsgId");
        NodeList msgId = document.getElementsByTagName("MsgId");
        if(srcMsgId.item(0)!=null){
            trackId = srcMsgId.item(0).getTextContent();
        } else if(msgId.item(0)!=null){
            trackId = msgId.item(0).getTextContent();
        } else {
            trackId = "";
        }

        return trackId;
    }

    public Schema getXMLSchema(Document document) throws ParserXMLException {
        try {
            return Schema.valueOf(document.getFirstChild().getNodeName());
        } catch (Exception e){
            throw new ParserXMLException("Incorrect XML schema");
        }

    }

    private Direction getDirection(Document document, Schema schema) throws ParserXMLException {

        String direction = "";
        int count=0;

        if(schema.equals(Schema.UFXMsg)){
            direction =
                    document.getElementsByTagName(schema.name())
                            .item(0)
                            .getAttributes()
                            .getNamedItem("direction").toString();
        } else{
            direction = document.getElementsByTagName("srv_req").item(0) != null ? "Rq" : "Rs";
        }
        return direction.equals("Rq") ? Direction.RQ : Direction.RP;
    }

    public XMLType getXMLType(Document document) throws ParserXMLException {
        Schema schema = getXMLSchema(document);
        Direction direction = getDirection(document, schema);
        XMLType  xmlType = getTypeBySchema(schema,direction);
        return getTypeBySchema(schema,direction);
    }

    public XMLType getTypeBySchema(Schema schema,Direction direction){
        String type = "";
        if (schema.equals(Schema.NIServices) || schema.equals(Schema.ENBDServices)){
            type = "CLIENT";
        } else if (schema.equals(Schema.UFXMsg)) {
            type = "UFX";
        } else if (schema.equals(Schema.NIEAIServices)) {
            type = "NIEAI";
        }
        return XMLType.valueOf(type+"_"+direction);
    }

    public Log minimiseLog(){
    return new Log();
    }

    public String getJSONLog(String log) throws JsonProcessingException, ParserXMLException, FileNotFoundException {
        log = minimiseLog(log);
        Log logObj = new Log();
        logObj.setLogLines(matchLog(log));
        return  new ObjectMapper().writeValueAsString(logObj);
    }

    private Map<String, SystemLog> matchLog(String log) throws ParserXMLException {

        Pattern pattern = Pattern.compile(LINE_PATTERN);
        Matcher matcher = pattern.matcher(log);

        return fillLog(pattern, matcher);
    }

    private Map<String, SystemLog> fillLog(Pattern pattern, Matcher matcher) throws ParserXMLException {

        Map<String, SystemLog> logLines = new HashMap<String, SystemLog>();
        while (matcher.find()){

            String level = matcher.group(2); // get LEVEL
            String msg = matcher.group(3); //get XML

            Line lineObj = getLogLine(level, msg);

            SystemLog systemLog = logLines.get(lineObj.getFlowId()); // is flowExists?

            if(!isNull(systemLog)){

                Map<XMLType, List<Line>> logLinesMap = systemLog.getLogLine();
                List<Line> lines = logLinesMap.get(lineObj.getXmlType());

                if(!isNull(lines)){
                    lines.add(lineObj);
                } else {
                    List<Line> newLines = new ArrayList<>();
                    newLines.add(lineObj);
                    logLinesMap.put(lineObj.getXmlType(), newLines);
                }


            } else {


                SystemLog  newSystemLog = new SystemLog();
                Map newSystemLogMap = new HashMap<XMLType, List<Line>>();
                List listLines = new ArrayList<Line>();
                listLines.add(lineObj);
                newSystemLog.setLogLine(newSystemLogMap);

                newSystemLogMap.put(lineObj.getXmlType(), listLines);

                logLines.put(lineObj.getFlowId(), newSystemLog);

            }
        }
        return logLines;
    }

    private boolean isNull(Object value){
        return value != null ? false : true;
    }

    public static void main(String[] args) throws FileNotFoundException, JsonProcessingException, ParserXMLException {

        LogParser logParser = new LogParser();
        FileUtil fileUtil = new FileUtil();
        String json = logParser.getJSONLog(fileUtil.getContent("C:\\nitib-way4broker.log"));
        System.out.println(json);


    }
}
