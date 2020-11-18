package konovalov.ebayscraper.core.entities;

public class TerapeakResult {

    private final String query;
    private Double avgSoldPrice;
    private Double avgListingPrice;
    private Integer totalSold;
    private Integer totalActive;
    private Double selfThrough;
    private Status status;
    private boolean isSoldInfoSet = false;
    private boolean isActiveInfoSet = false;

    public TerapeakResult(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public Double getAvgSoldPrice() {
        return avgSoldPrice;
    }

    public void setAvgSoldPrice(Double avgSoldPrice) {
        this.avgSoldPrice = avgSoldPrice;
    }

    public Double getAvgListingPrice() {
        return avgListingPrice;
    }

    public void setAvgListingPrice(Double avgListingPrice) {
        this.avgListingPrice = avgListingPrice;
    }

    public Integer getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(Integer totalSold) {
        this.totalSold = totalSold;
    }

    public Integer getTotalActive() {
        return totalActive;
    }

    public void setTotalActive(Integer totalActive) {
        this.totalActive = totalActive;
    }

    public Double getSelfThrough() {
        return selfThrough;
    }

    public void setSelfThrough(Double selfThrough) {
        this.selfThrough = selfThrough;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isSoldInfoSet() {
        return isSoldInfoSet;
    }

    public void setSoldInfoSet(boolean soldInfoSet) {
        isSoldInfoSet = soldInfoSet;
    }

    public boolean isActiveInfoSet() {
        return isActiveInfoSet;
    }

    public void setActiveInfoSet(boolean activeInfoSet) {
        isActiveInfoSet = activeInfoSet;
    }
}
