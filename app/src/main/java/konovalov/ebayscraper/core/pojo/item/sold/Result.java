
package konovalov.ebayscraper.core.pojo.item.sold;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("listing")
    @Expose
    private Listing listing;
    @SerializedName("avgsalesprice")
    @Expose
    private Avgsalesprice avgsalesprice;
    @SerializedName("itemssold")
    @Expose
    private Itemssold itemssold;
    @SerializedName("totalsales")
    @Expose
    private Totalsales totalsales;
    @SerializedName("bids")
    @Expose
    private Bids bids;
    @SerializedName("datelastsold")
    @Expose
    private Datelastsold datelastsold;

    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

    public Avgsalesprice getAvgsalesprice() {
        return avgsalesprice;
    }

    public void setAvgsalesprice(Avgsalesprice avgsalesprice) {
        this.avgsalesprice = avgsalesprice;
    }

    public Itemssold getItemssold() {
        return itemssold;
    }

    public void setItemssold(Itemssold itemssold) {
        this.itemssold = itemssold;
    }

    public Totalsales getTotalsales() {
        return totalsales;
    }

    public void setTotalsales(Totalsales totalsales) {
        this.totalsales = totalsales;
    }

    public Bids getBids() {
        return bids;
    }

    public void setBids(Bids bids) {
        this.bids = bids;
    }

    public Datelastsold getDatelastsold() {
        return datelastsold;
    }

    public void setDatelastsold(Datelastsold datelastsold) {
        this.datelastsold = datelastsold;
    }

}
