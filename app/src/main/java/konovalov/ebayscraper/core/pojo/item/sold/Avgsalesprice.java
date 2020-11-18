
package konovalov.ebayscraper.core.pojo.item.sold;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Avgsalesprice {

    @SerializedName("avgsalesprice")
    @Expose
    private Avgsalesprice_ avgsalesprice;
    @SerializedName("averageshipping")
    @Expose
    private Averageshipping averageshipping;

    public Avgsalesprice_ getAvgsalesprice() {
        return avgsalesprice;
    }

    public void setAvgsalesprice(Avgsalesprice_ avgsalesprice) {
        this.avgsalesprice = avgsalesprice;
    }

    public Averageshipping getAverageshipping() {
        return averageshipping;
    }

    public void setAverageshipping(Averageshipping averageshipping) {
        this.averageshipping = averageshipping;
    }

}
