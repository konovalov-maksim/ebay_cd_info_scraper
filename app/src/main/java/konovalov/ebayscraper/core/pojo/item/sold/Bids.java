
package konovalov.ebayscraper.core.pojo.item.sold;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bids {

    @SerializedName("textSpans")
    @Expose
    private List<TextSpan___________> textSpans = null;

    public List<TextSpan___________> getTextSpans() {
        return textSpans;
    }

    public void setTextSpans(List<TextSpan___________> textSpans) {
        this.textSpans = textSpans;
    }

}
