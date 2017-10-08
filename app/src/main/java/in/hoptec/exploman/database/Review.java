package in.hoptec.exploman.database;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shivesh on 27/8/17.
 */

public class Review {


    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("place_id")
    @Expose
    public String placeId;
    @SerializedName("user_fname")
    @Expose
    public String userFname;
    @SerializedName("user_image")
    @Expose
    public String userImage;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("rating")
    @Expose
    public String rating;
    @SerializedName("date")
    @Expose
    public String date;
    @SerializedName("extra0")
    @Expose
    public String extra0;
    @SerializedName("id")
    @Expose
    public String id;



    public String getCreatedAt()
    {


        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = dateFormat.parse(date);

            String monthname=(String)android.text.format.DateFormat.format("MMMM", date1);


            return ""+date1.getDate()+" "+monthname;

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return date;
    }



}
