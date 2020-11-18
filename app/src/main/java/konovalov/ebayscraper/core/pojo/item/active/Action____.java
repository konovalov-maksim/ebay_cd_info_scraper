
package konovalov.ebayscraper.core.pojo.item.active;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Action____ {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("signInRequired")
    @Expose
    private Boolean signInRequired;
    @SerializedName("tracking")
    @Expose
    private Tracking____ tracking;
    @SerializedName("URL")
    @Expose
    private String uRL;

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

    public Boolean getSignInRequired() {
        return signInRequired;
    }

    public void setSignInRequired(Boolean signInRequired) {
        this.signInRequired = signInRequired;
    }

    public Tracking____ getTracking() {
        return tracking;
    }

    public void setTracking(Tracking____ tracking) {
        this.tracking = tracking;
    }

    public String getURL() {
        return uRL;
    }

    public void setURL(String uRL) {
        this.uRL = uRL;
    }

}
