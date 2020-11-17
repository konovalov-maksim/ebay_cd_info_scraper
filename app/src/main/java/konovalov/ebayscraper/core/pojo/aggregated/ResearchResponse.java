
package konovalov.ebayscraper.core.pojo.aggregated;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResearchResponse {

    @SerializedName("__type")
    @Expose
    private String type;
    @SerializedName("searchResultsTitle")
    @Expose
    private SearchResultsTitle searchResultsTitle;
    @SerializedName("sections")
    @Expose
    private List<Section> sections = null;
    @SerializedName("trendsLabel")
    @Expose
    private TrendsLabel trendsLabel;
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

    public SearchResultsTitle getSearchResultsTitle() {
        return searchResultsTitle;
    }

    public void setSearchResultsTitle(SearchResultsTitle searchResultsTitle) {
        this.searchResultsTitle = searchResultsTitle;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public TrendsLabel getTrendsLabel() {
        return trendsLabel;
    }

    public void setTrendsLabel(TrendsLabel trendsLabel) {
        this.trendsLabel = trendsLabel;
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
