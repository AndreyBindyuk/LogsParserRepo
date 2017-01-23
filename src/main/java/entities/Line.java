package entities;

import java.util.Date;

/**
 * Created by Sergey_Chernikov on 1/20/2017.
 */
public class Line {

    private String level;
    private Date timestamp;
    private String componentId;
    private String serviceId;
    private String processId;
    private String flowId;
    private String message;
    private XMLType xmlType;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public XMLType getXmlType() {
        return xmlType;
    }

    public void setXmlType(XMLType xmlType) {
        this.xmlType = xmlType;
    }
}
