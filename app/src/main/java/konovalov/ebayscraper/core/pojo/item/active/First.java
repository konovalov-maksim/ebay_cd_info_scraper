
package konovalov.ebayscraper.core.pojo.item.active;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class First {

    @SerializedName("disabled")
    @Expose
    private Boolean disabled;
    @SerializedName("textSpans")
    @Expose
    private List<TextSpan_____________> textSpans = null;
    @SerializedName("accessibilityText")
    @Expose
    private String accessibilityText;
    @SerializedName("action")
    @Expose
    private Action_____ action;

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public List<TextSpan_____________> getTextSpans() {
        return textSpans;
    }

    public void setTextSpans(List<TextSpan_____________> textSpans) {
        this.textSpans = textSpans;
    }

    public String getAccessibilityText() {
        return accessibilityText;
    }

    public void setAccessibilityText(String accessibilityText) {
        this.accessibilityText = accessibilityText;
    }

    public Action_____ getAction() {
        return action;
    }

    public void setAction(Action_____ action) {
        this.action = action;
    }

}
