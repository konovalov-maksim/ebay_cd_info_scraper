
package konovalov.ebayscraper.core.pojo.item.active;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListingPrice {

    @SerializedName("listingPrice")
    @Expose
    private ListingPrice_ listingPrice;
    @SerializedName("listingShipping")
    @Expose
    private ListingShipping listingShipping;

    public ListingPrice_ getListingPrice() {
        return listingPrice;
    }

    public void setListingPrice(ListingPrice_ listingPrice) {
        this.listingPrice = listingPrice;
    }

    public ListingShipping getListingShipping() {
        return listingShipping;
    }

    public void setListingShipping(ListingShipping listingShipping) {
        this.listingShipping = listingShipping;
    }

}
