
package konovalov.ebayscraper.core.pojo.item.sold;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Totalsales {

    @SerializedName("textSpans")
    @Expose
    private List<TextSpan__________> textSpans = null;

    public List<TextSpan__________> getTextSpans() {
        return textSpans;
    }

    public void setTextSpans(List<TextSpan__________> textSpans) {
        this.textSpans = textSpans;
    }

}
