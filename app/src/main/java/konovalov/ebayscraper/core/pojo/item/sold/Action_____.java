
package konovalov.ebayscraper.core.pojo.item.sold;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Action_____ {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("tracking")
    @Expose
    private Tracking_____ tracking;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tracking_____ getTracking() {
        return tracking;
    }

    public void setTracking(Tracking_____ tracking) {
        this.tracking = tracking;
    }

}
