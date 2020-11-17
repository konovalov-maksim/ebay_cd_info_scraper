
package konovalov.ebayscraper.core.pojo.aggregated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventProperty {

    @SerializedName("moduledtl")
    @Expose
    private String moduledtl;

    public String getModuledtl() {
        return moduledtl;
    }

    public void setModuledtl(String moduledtl) {
        this.moduledtl = moduledtl;
    }

}
