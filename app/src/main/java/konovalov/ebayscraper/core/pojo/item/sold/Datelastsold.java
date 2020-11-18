
package konovalov.ebayscraper.core.pojo.item.sold;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datelastsold {

    @SerializedName("textSpans")
    @Expose
    private List<TextSpan____________> textSpans = null;

    public List<TextSpan____________> getTextSpans() {
        return textSpans;
    }

    public void setTextSpans(List<TextSpan____________> textSpans) {
        this.textSpans = textSpans;
    }

}
