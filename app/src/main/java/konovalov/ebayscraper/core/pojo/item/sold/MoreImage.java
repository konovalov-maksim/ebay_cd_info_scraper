
package konovalov.ebayscraper.core.pojo.item.sold;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MoreImage {

    @SerializedName("URL")
    @Expose
    private String uRL;

    public String getURL() {
        return uRL;
    }

    public void setURL(String uRL) {
        this.uRL = uRL;
    }

}
