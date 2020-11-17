
package konovalov.ebayscraper.core.pojo.aggregated;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchResultsTitle {

    @SerializedName("textSpans")
    @Expose
    private List<TextSpan> textSpans = null;
    @SerializedName("accessibilityText")
    @Expose
    private String accessibilityText;

    public List<TextSpan> getTextSpans() {
        return textSpans;
    }

    public void setTextSpans(List<TextSpan> textSpans) {
        this.textSpans = textSpans;
    }

    public String getAccessibilityText() {
        return accessibilityText;
    }

    public void setAccessibilityText(String accessibilityText) {
        this.accessibilityText = accessibilityText;
    }

}
