
package konovalov.ebayscraper.core.pojo.item.active;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListingPrice_ {

    @SerializedName("textSpans")
    @Expose
    private List<TextSpan_______> textSpans = null;

    public List<TextSpan_______> getTextSpans() {
        return textSpans;
    }

    public void setTextSpans(List<TextSpan_______> textSpans) {
        this.textSpans = textSpans;
    }

}
