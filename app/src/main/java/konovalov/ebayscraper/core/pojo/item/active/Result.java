
package konovalov.ebayscraper.core.pojo.item.active;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("listing")
    @Expose
    private Listing listing;
    @SerializedName("listingPrice")
    @Expose
    private ListingPrice listingPrice;
    @SerializedName("bids")
    @Expose
    private Bids bids;
    @SerializedName("watchers")
    @Expose
    private Watchers watchers;
    @SerializedName("promoted")
    @Expose
    private Promoted promoted;
    @SerializedName("startDate")
    @Expose
    private StartDate startDate;

    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

    public ListingPrice getListingPrice() {
        return listingPrice;
    }

    public void setListingPrice(ListingPrice listingPrice) {
        this.listingPrice = listingPrice;
    }

    public Bids getBids() {
        return bids;
    }

    public void setBids(Bids bids) {
        this.bids = bids;
    }

    public Watchers getWatchers() {
        return watchers;
    }

    public void setWatchers(Watchers watchers) {
        this.watchers = watchers;
    }

    public Promoted getPromoted() {
        return promoted;
    }

    public void setPromoted(Promoted promoted) {
        this.promoted = promoted;
    }

    public StartDate getStartDate() {
        return startDate;
    }

    public void setStartDate(StartDate startDate) {
        this.startDate = startDate;
    }

}
