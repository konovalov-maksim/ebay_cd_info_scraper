
package konovalov.ebayscraper.core.pojo.item.sold;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Prev {

    @SerializedName("disabled")
    @Expose
    private Boolean disabled;
    @SerializedName("textSpans")
    @Expose
    private List<TextSpan_______________> textSpans = null;
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

    public List<TextSpan_______________> getTextSpans() {
        return textSpans;
    }

    public void setTextSpans(List<TextSpan_______________> textSpans) {
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
