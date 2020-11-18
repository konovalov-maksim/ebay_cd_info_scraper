
package konovalov.ebayscraper.core.pojo.item.active;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FormatList {

    @SerializedName("textSpans")
    @Expose
    private List<TextSpan______> textSpans = null;
    @SerializedName("accessibilityText")
    @Expose
    private String accessibilityText;

    public List<TextSpan______> getTextSpans() {
        return textSpans;
    }

    public void setTextSpans(List<TextSpan______> textSpans) {
        this.textSpans = textSpans;
    }

    public String getAccessibilityText() {
        return accessibilityText;
    }

    public void setAccessibilityText(String accessibilityText) {
        this.accessibilityText = accessibilityText;
    }

}
