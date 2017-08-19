package in.hoptec.exploman.database;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shivesh on 19/8/17.
 */

public class Place {


    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("desc")
    @Expose
    public String desc;
    @SerializedName("lat")
    @Expose
    public Double lat;
    @SerializedName("lng")
    @Expose
    public Double lng;
    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("images")
    @Expose
    public String images;
    @SerializedName("rating")
    @Expose
    public String rating;
    @SerializedName("distance")
    @Expose
    public String distance;


}
