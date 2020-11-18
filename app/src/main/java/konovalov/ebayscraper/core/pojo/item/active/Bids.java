
package konovalov.ebayscraper.core.pojo.item.active;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bids {

    @SerializedName("textSpans")
    @Expose
    private List<TextSpan_________> textSpans = null;

    public List<TextSpan_________> getTextSpans() {
        return textSpans;
    }

    public void setTextSpans(List<TextSpan_________> textSpans) {
        this.textSpans = textSpans;
    }

}
