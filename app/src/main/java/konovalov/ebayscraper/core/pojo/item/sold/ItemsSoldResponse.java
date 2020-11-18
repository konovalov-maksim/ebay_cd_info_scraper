
package konovalov.ebayscraper.core.pojo.item.sold;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemsSoldResponse {

    @SerializedName("__type")
    @Expose
    private String type;
    @SerializedName("columnheader")
    @Expose
    private List<Columnheader> columnheader = null;
    @SerializedName("modalColumn")
    @Expose
    private ModalColumn modalColumn;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;
    @SerializedName("pagination")
    @Expose
    private Pagination pagination;
    @SerializedName("meta")
    @Expose
    private Meta meta;
    @SerializedName("debugUrl")
    @Expose
    private String debugUrl;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Columnheader> getColumnheader() {
        return columnheader;
    }

    public void setColumnheader(List<Columnheader> columnheader) {
        this.columnheader = columnheader;
    }

    public ModalColumn getModalColumn() {
        return modalColumn;
    }

    public void setModalColumn(ModalColumn modalColumn) {
        this.modalColumn = modalColumn;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public String getDebugUrl() {
        return debugUrl;
    }

    public void setDebugUrl(String debugUrl) {
        this.debugUrl = debugUrl;
    }

}
