
package konovalov.ebayscraper.core.pojo.item.active;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Summary {

    @SerializedName("textSpans")
    @Expose
    private List<TextSpan____________> textSpans = null;
    @SerializedName("accessibilityText")
    @Expose
    private String accessibilityText;

    public List<TextSpan____________> getTextSpans() {
        return textSpans;
    }

    public void setTextSpans(List<TextSpan____________> textSpans) {
        this.textSpans = textSpans;
    }

    public String getAccessibilityText() {
        return accessibilityText;
    }

    public void setAccessibilityText(String accessibilityText) {
        this.accessibilityText = accessibilityText;
    }

}
