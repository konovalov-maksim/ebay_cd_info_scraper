
package konovalov.ebayscraper.core.pojo.item.sold;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Averageshipping {

    @SerializedName("textSpans")
    @Expose
    private List<TextSpan________> textSpans = null;

    public List<TextSpan________> getTextSpans() {
        return textSpans;
    }

    public void setTextSpans(List<TextSpan________> textSpans) {
        this.textSpans = textSpans;
    }

}
