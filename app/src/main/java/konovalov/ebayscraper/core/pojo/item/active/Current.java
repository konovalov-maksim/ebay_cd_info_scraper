
package konovalov.ebayscraper.core.pojo.item.active;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Current {

    @SerializedName("textSpans")
    @Expose
    private List<TextSpan_______________> textSpans = null;
    @SerializedName("accessibilityText")
    @Expose
    private String accessibilityText;

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

}
