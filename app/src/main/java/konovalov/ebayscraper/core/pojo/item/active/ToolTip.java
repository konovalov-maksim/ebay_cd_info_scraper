
package konovalov.ebayscraper.core.pojo.item.active;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ToolTip {

    @SerializedName("textSpans")
    @Expose
    private List<TextSpan_> textSpans = null;
    @SerializedName("accessibilityText")
    @Expose
    private String accessibilityText;
    @SerializedName("action")
    @Expose
    private Action_ action;

    public List<TextSpan_> getTextSpans() {
        return textSpans;
    }

    public void setTextSpans(List<TextSpan_> textSpans) {
        this.textSpans = textSpans;
    }

    public String getAccessibilityText() {
        return accessibilityText;
    }

    public void setAccessibilityText(String accessibilityText) {
        this.accessibilityText = accessibilityText;
    }

    public Action_ getAction() {
        return action;
    }

    public void setAction(Action_ action) {
        this.action = action;
    }

}
