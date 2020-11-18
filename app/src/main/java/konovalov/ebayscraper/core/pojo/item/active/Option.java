
package konovalov.ebayscraper.core.pojo.item.active;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Option {

    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("selected")
    @Expose
    private Boolean selected;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

}
