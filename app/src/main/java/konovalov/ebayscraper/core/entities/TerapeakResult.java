package konovalov.ebayscraper.core.entities;

import java.util.ArrayList;
import java.util.List;

public class TerapeakResult {

    private double avgSoldPrice;
    private double minSoldPrice;
    private double maxSoldPrice;
    private double avgShipping;
    private int freeShipping;
    private int totalSold;
    private double selfThrough;
    private int totalSellers;
    private List<TerapeakItem> items = new ArrayList<>();

    public double getAvgSoldPrice() {
        return avgSoldPrice;
    }

    public void setAvgSoldPrice(double avgSoldPrice) {
        this.avgSoldPrice = avgSoldPrice;
    }

    public double getMinSoldPrice() {
        return minSoldPrice;
    }

    public void setMinSoldPrice(double minSoldPrice) {
        this.minSoldPrice = minSoldPrice;
    }

    public double getMaxSoldPrice() {
        return maxSoldPrice;
    }

    public void setMaxSoldPrice(double maxSoldPrice) {
        this.maxSoldPrice = maxSoldPrice;
    }

    public double getAvgShipping() {
        return avgShipping;
    }

    public void setAvgShipping(double avgShipping) {
        this.avgShipping = avgShipping;
    }

    public int getFreeShipping() {
        return freeShipping;
    }

    public void setFreeShipping(int freeShipping) {
        this.freeShipping = freeShipping;
    }

    public int getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(int totalSold) {
        this.totalSold = totalSold;
    }

    public double getSelfThrough() {
        return selfThrough;
    }

    public void setSelfThrough(double selfThrough) {
        this.selfThrough = selfThrough;
    }

    public int getTotalSellers() {
        return totalSellers;
    }

    public void setTotalSellers(int totalSellers) {
        this.totalSellers = totalSellers;
    }

    public List<TerapeakItem> getItems() {
        return items;
    }

    public void setItems(List<TerapeakItem> items) {
        this.items = items;
    }


}
