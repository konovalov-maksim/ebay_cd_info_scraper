package konovalov.ebayscraper.core.entities;

import java.io.Serializable;

public class ItemSold implements Serializable {

    private String title;
    private String id;
    private String imgUrl;
    private String format;
    private String avgSoldPrice;
    private String shipping;
    private String totalSold;
    private String totalSales;
    private String bids;
    private String lastSold;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl.replaceAll("^//", "https://");
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getAvgSoldPrice() {
        return avgSoldPrice;
    }

    public void setAvgSoldPrice(String avgSoldPrice) {
        this.avgSoldPrice = avgSoldPrice;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public String getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(String totalSold) {
        this.totalSold = totalSold;
    }

    public String getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(String totalSales) {
        this.totalSales = totalSales;
    }

    public String getBids() {
        return bids;
    }

    public void setBids(String bids) {
        this.bids = bids;
    }

    public String getLastSold() {
        return lastSold;
    }

    public void setLastSold(String lastSold) {
        this.lastSold = lastSold;
    }
}
