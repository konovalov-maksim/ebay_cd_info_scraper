
package konovalov.ebayscraper.core.pojo.item.sold;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tracking {

    @SerializedName("eventFamily")
    @Expose
    private String eventFamily;
    @SerializedName("eventAction")
    @Expose
    private String eventAction;
    @SerializedName("operationId")
    @Expose
    private String operationId;
    @SerializedName("flushImmediately")
    @Expose
    private Boolean flushImmediately;
    @SerializedName("eventProperty")
    @Expose
    private EventProperty eventProperty;

    public String getEventFamily() {
        return eventFamily;
    }

    public void setEventFamily(String eventFamily) {
        this.eventFamily = eventFamily;
    }

    public String getEventAction() {
        return eventAction;
    }

    public void setEventAction(String eventAction) {
        this.eventAction = eventAction;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public Boolean getFlushImmediately() {
        return flushImmediately;
    }

    public void setFlushImmediately(Boolean flushImmediately) {
        this.flushImmediately = flushImmediately;
    }

    public EventProperty getEventProperty() {
        return eventProperty;
    }

    public void setEventProperty(EventProperty eventProperty) {
        this.eventProperty = eventProperty;
    }

}
