package konovalov.ebayscraper.core.entities;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TerapeakResult implements Serializable {

    private final String query;
    private Double avgSoldPrice;
    private Double avgListingPrice;
    private Integer totalSold;
    private Integer totalActive;
    private Double selfThrough;
    private Status status = Status.NEW;
    private boolean isSoldInfoSet = false;
    private boolean isActiveInfoSet = false;
    private List<ItemActive> activeItems = new ArrayList<>();
    private List<ItemSold> soldItems = new ArrayList<>();

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

    public List<ItemActive> getActiveItems() {
        return activeItems;
    }

    public void setActiveItems(List<ItemActive> activeItems) {
        this.activeItems = activeItems;
    }

    public List<ItemSold> getSoldItems() {
        return soldItems;
    }

    public void setSoldItems(List<ItemSold> soldItems) {
        this.soldItems = soldItems;
    }

    @Nullable
    public Double getSoldRatio() {
        try {
            return round(totalSold * 100.0 / totalActive, 2);
        } catch (Exception e) {
            return null;
        }
    }

    //the formula for "Current Value" is (Avg sold price) * (1+sell-through)
    @Nullable
    public Double getCurValue() {
        try {
            return round(avgSoldPrice * (1 + selfThrough), 2);
        } catch (Exception e) {
            return null;
        }
    }

    public String getStatusString() {
        return status.statusName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TerapeakResult result = (TerapeakResult) o;
        return isSoldInfoSet == result.isSoldInfoSet &&
                isActiveInfoSet == result.isActiveInfoSet &&
                Objects.equals(query, result.query) &&
                Objects.equals(avgSoldPrice, result.avgSoldPrice) &&
                Objects.equals(avgListingPrice, result.avgListingPrice) &&
                Objects.equals(totalSold, result.totalSold) &&
                Objects.equals(totalActive, result.totalActive) &&
                Objects.equals(selfThrough, result.selfThrough) &&
                status == result.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(query, avgSoldPrice, avgListingPrice, totalSold, totalActive, selfThrough, status, isSoldInfoSet, isActiveInfoSet);
    }

    @Override
    public String toString() {
        return "TerapeakResult{" +
                "query='" + query + '\'' +
                ", avgSoldPrice=" + avgSoldPrice +
                ", avgListingPrice=" + avgListingPrice +
                ", totalSold=" + totalSold +
                ", totalActive=" + totalActive +
                ", selfThrough=" + selfThrough +
                ", status=" + status +
                ", isSoldInfoSet=" + isSoldInfoSet +
                ", isActiveInfoSet=" + isActiveInfoSet +
                '}';
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
