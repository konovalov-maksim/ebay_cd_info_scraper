
package konovalov.ebayscraper.core.pojo.item.sold;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventProperty___ {

    @SerializedName("sid")
    @Expose
    private String sid;
    @SerializedName("trkp")
    @Expose
    private String trkp;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getTrkp() {
        return trkp;
    }

    public void setTrkp(String trkp) {
        this.trkp = trkp;
    }

}
