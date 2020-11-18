
package konovalov.ebayscraper.core.pojo.item.active;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Open {

    @SerializedName("textSpans")
    @Expose
    private List<TextSpan__> textSpans = null;
    @SerializedName("accessibilityText")
    @Expose
    private String accessibilityText;
    @SerializedName("action")
    @Expose
    private Action__ action;

    public List<TextSpan__> getTextSpans() {
        return textSpans;
    }

    public void setTextSpans(List<TextSpan__> textSpans) {
        this.textSpans = textSpans;
    }

    public String getAccessibilityText() {
        return accessibilityText;
    }

    public void setAccessibilityText(String accessibilityText) {
        this.accessibilityText = accessibilityText;
    }

    public Action__ getAction() {
        return action;
    }

    public void setAction(Action__ action) {
        this.action = action;
    }

}
