
package konovalov.ebayscraper.core.pojo.aggregated;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrendsLabel {

    @SerializedName("textSpans")
    @Expose
    private List<TextSpan____> textSpans = null;
    @SerializedName("accessibilityText")
    @Expose
    private String accessibilityText;
    @SerializedName("action")
    @Expose
    private Action action;

    public List<TextSpan____> getTextSpans() {
        return textSpans;
    }

    public void setTextSpans(List<TextSpan____> textSpans) {
        this.textSpans = textSpans;
    }

    public String getAccessibilityText() {
        return accessibilityText;
    }

    public void setAccessibilityText(String accessibilityText) {
        this.accessibilityText = accessibilityText;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

}
