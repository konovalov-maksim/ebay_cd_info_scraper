package konovalov.ebayscraper.core.entities;

public class TerapeakResult {

    private final String query;
    private double avgSoldPrice;
    private double avgListingPrice;
    private int totalSold;
    private int totalActive;
    private double selfThrough;
    private Status status;
    private boolean isSoldInfoSet = false;
    private boolean isActiveInfoSet = false;

    public TerapeakResult(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public double getAvgSoldPrice() {
        return avgSoldPrice;
    }

    public void setAvgSoldPrice(double avgSoldPrice) {
        this.avgSoldPrice = avgSoldPrice;
    }

    public double getAvgListingPrice() {
        return avgListingPrice;
    }

    public void setAvgListingPrice(double avgListingPrice) {
        this.avgListingPrice = avgListingPrice;
    }

    public int getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(int totalSold) {
        this.totalSold = totalSold;
    }

    public int getTotalActive() {
        return totalActive;
    }

    public void setTotalActive(int totalActive) {
        this.totalActive = totalActive;
    }

    public double getSelfThrough() {
        return selfThrough;
    }

    public void setSelfThrough(double selfThrough) {
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
