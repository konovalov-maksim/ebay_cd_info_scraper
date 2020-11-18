
package konovalov.ebayscraper.core.pojo.item.sold;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Next {

    @SerializedName("disabled")
    @Expose
    private Boolean disabled;
    @SerializedName("textSpans")
    @Expose
    private List<TextSpan_________________> textSpans = null;
    @SerializedName("accessibilityText")
    @Expose
    private String accessibilityText;
    @SerializedName("action")
    @Expose
    private Action______ action;

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public List<TextSpan_________________> getTextSpans() {
        return textSpans;
    }

    public void setTextSpans(List<TextSpan_________________> textSpans) {
        this.textSpans = textSpans;
    }

    public String getAccessibilityText() {
        return accessibilityText;
    }

    public void setAccessibilityText(String accessibilityText) {
        this.accessibilityText = accessibilityText;
    }

    public Action______ getAction() {
        return action;
    }

    public void setAction(Action______ action) {
        this.action = action;
    }

}
