
package konovalov.ebayscraper.core.pojo.aggregated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataItem {

    @SerializedName("header")
    @Expose
    private Header header;
    @SerializedName("value")
    @Expose
    private Value value;
    @SerializedName("tooltip")
    @Expose
    private Tooltip tooltip;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public Tooltip getTooltip() {
        return tooltip;
    }

    public void setTooltip(Tooltip tooltip) {
        this.tooltip = tooltip;
    }

}
