
package konovalov.ebayscraper.core.pojo.item.sold;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Columnheader {

    @SerializedName("headerId")
    @Expose
    private String headerId;
    @SerializedName("text")
    @Expose
    private Text text;
    @SerializedName("sortOrder")
    @Expose
    private String sortOrder;
    @SerializedName("sortable")
    @Expose
    private Boolean sortable;
    @SerializedName("contentType")
    @Expose
    private String contentType;
    @SerializedName("toolTip")
    @Expose
    private ToolTip toolTip;

    public String getHeaderId() {
        return headerId;
    }

    public void setHeaderId(String headerId) {
        this.headerId = headerId;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getSortable() {
        return sortable;
    }

    public void setSortable(Boolean sortable) {
        this.sortable = sortable;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public ToolTip getToolTip() {
        return toolTip;
    }

    public void setToolTip(ToolTip toolTip) {
        this.toolTip = toolTip;
    }

}
