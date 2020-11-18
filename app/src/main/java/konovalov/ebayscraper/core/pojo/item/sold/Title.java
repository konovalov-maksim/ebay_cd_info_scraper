
package konovalov.ebayscraper.core.pojo.item.sold;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Title {

    @SerializedName("textSpans")
    @Expose
    private List<TextSpan_____> textSpans = null;
    @SerializedName("action")
    @Expose
    private Action___ action;
    @SerializedName("postAccessibilityClippedText")
    @Expose
    private String postAccessibilityClippedText;

    public List<TextSpan_____> getTextSpans() {
        return textSpans;
    }

    public void setTextSpans(List<TextSpan_____> textSpans) {
        this.textSpans = textSpans;
    }

    public Action___ getAction() {
        return action;
    }

    public void setAction(Action___ action) {
        this.action = action;
    }

    public String getPostAccessibilityClippedText() {
        return postAccessibilityClippedText;
    }

    public void setPostAccessibilityClippedText(String postAccessibilityClippedText) {
        this.postAccessibilityClippedText = postAccessibilityClippedText;
    }

}
