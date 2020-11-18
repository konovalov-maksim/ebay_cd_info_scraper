
package konovalov.ebayscraper.core.pojo.item.active;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Close {

    @SerializedName("textSpans")
    @Expose
    private List<TextSpan___> textSpans = null;
    @SerializedName("accessibilityText")
    @Expose
    private String accessibilityText;
    @SerializedName("action")
    @Expose
    private Action___ action;

    public List<TextSpan___> getTextSpans() {
        return textSpans;
    }

    public void setTextSpans(List<TextSpan___> textSpans) {
        this.textSpans = textSpans;
    }

    public String getAccessibilityText() {
        return accessibilityText;
    }

    public void setAccessibilityText(String accessibilityText) {
        this.accessibilityText = accessibilityText;
    }

    public Action___ getAction() {
        return action;
    }

    public void setAction(Action___ action) {
        this.action = action;
    }

}
