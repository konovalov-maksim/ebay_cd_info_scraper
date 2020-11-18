
package konovalov.ebayscraper.core.pojo.item.active;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Listing {

    @SerializedName("image")
    @Expose
    private Image image;
    @SerializedName("itemId")
    @Expose
    private ItemId itemId;
    @SerializedName("title")
    @Expose
    private Title title;
    @SerializedName("formatList")
    @Expose
    private List<FormatList> formatList = null;
    @SerializedName("moreImages")
    @Expose
    private List<MoreImage> moreImages = null;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public ItemId getItemId() {
        return itemId;
    }

    public void setItemId(ItemId itemId) {
        this.itemId = itemId;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public List<FormatList> getFormatList() {
        return formatList;
    }

    public void setFormatList(List<FormatList> formatList) {
        this.formatList = formatList;
    }

    public List<MoreImage> getMoreImages() {
        return moreImages;
    }

    public void setMoreImages(List<MoreImage> moreImages) {
        this.moreImages = moreImages;
    }

}
