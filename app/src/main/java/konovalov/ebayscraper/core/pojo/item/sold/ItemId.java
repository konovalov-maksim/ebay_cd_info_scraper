
package konovalov.ebayscraper.core.pojo.item.sold;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemId {

    @SerializedName("textSpans")
    @Expose
    private List<TextSpan____> textSpans = null;
    @SerializedName("value")
    @Expose
    private String value;

    public List<TextSpan____> getTextSpans() {
        return textSpans;
    }

    public void setTextSpans(List<TextSpan____> textSpans) {
        this.textSpans = textSpans;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
