
package konovalov.ebayscraper.core.pojo.item.active;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pagination {

    @SerializedName("summary")
    @Expose
    private Summary summary;
    @SerializedName("first")
    @Expose
    private First first;
    @SerializedName("prev")
    @Expose
    private Prev prev;
    @SerializedName("current")
    @Expose
    private Current current;
    @SerializedName("next")
    @Expose
    private Next next;
    @SerializedName("itemsPerPage")
    @Expose
    private ItemsPerPage itemsPerPage;

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public First getFirst() {
        return first;
    }

    public void setFirst(First first) {
        this.first = first;
    }

    public Prev getPrev() {
        return prev;
    }

    public void setPrev(Prev prev) {
        this.prev = prev;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public Next getNext() {
        return next;
    }

    public void setNext(Next next) {
        this.next = next;
    }

    public ItemsPerPage getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(ItemsPerPage itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

}
