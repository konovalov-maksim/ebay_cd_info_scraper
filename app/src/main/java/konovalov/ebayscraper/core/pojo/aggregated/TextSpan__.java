
package konovalov.ebayscraper.core.pojo.aggregated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TextSpan__ {

    @SerializedName("text")
    @Expose
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
