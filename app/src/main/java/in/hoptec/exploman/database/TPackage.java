package in.hoptec.exploman.database;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shivesh on 13/10/17.
 */

public class TPackage {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("desc")
    @Expose
    public String desc;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("places")
    @Expose
    public String places;
    @SerializedName("guides")
    @Expose
    public String guides;
    @SerializedName("price")
    @Expose
    public String price;
    @SerializedName("start")
    @Expose
    public String start;
    @SerializedName("end")
    @Expose
    public String end;
    @SerializedName("days")
    @Expose
    public String days;
    @SerializedName("place_details")
    @Expose
    public List<Place> placeDetails = null;
    @SerializedName("guide_details")
    @Expose
    public List<Guide> guideDetails = null;

}
