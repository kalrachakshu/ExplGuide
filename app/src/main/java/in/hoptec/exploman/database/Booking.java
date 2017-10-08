package in.hoptec.exploman.database;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shivesh on 9/10/17.
 */

public class Booking {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("start_time")
    @Expose
    public String startTime;
    @SerializedName("end_time")
    @Expose
    public String endTime;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("guide_id")
    @Expose
    public String guideId;
    @SerializedName("place_ids")
    @Expose
    public String placeIds;
    @SerializedName("amount_total")
    @Expose
    public String amountTotal;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("amount_paid")
    @Expose
    public String amountPaid;
    @SerializedName("txn_id")
    @Expose
    public String txnId;
    @SerializedName("guide")
    @Expose
    public Guide guide;
}
