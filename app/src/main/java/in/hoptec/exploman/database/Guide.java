package in.hoptec.exploman.database;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;

import java.util.ArrayList;

import in.hoptec.exploman.utl;

/**
 * Created by shivesh on 19/8/17.
 */

public class Guide {


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
    public String lat;
    @SerializedName("lng")
    @Expose
    public String lng;
    @SerializedName("rate")
    @Expose
    public String rate;
    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("images")
    @Expose
    public String images;
    @SerializedName("rating")
    @Expose
    public String rating;


    @SerializedName("place_id")
    @Expose
    public String placeId;

    public boolean marked=false;

    public MarkerOptions marker;


    public ArrayList<String> getImages()
    {
        ArrayList<String> imags=new ArrayList<>();

        try {
            JSONArray jar=new JSONArray(images);
            for(int i=0;i<jar.length();i++)
            {
                imags.add(jar.get(i).toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return imags;

    }


    @Override
    public String toString() {
        Guide pl=this;
        pl.marker=null;
        return utl.js.toJson(pl);
    }
}
