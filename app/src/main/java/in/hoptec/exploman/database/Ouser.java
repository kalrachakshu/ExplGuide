package in.hoptec.exploman.database;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shivesh on 30/8/17.
 */

public class Ouser {


    @SerializedName("user_name")
    @Expose
    public String userName;
    @SerializedName("user_fname")
    @Expose
    public String userFname;
    @SerializedName("user_password")
    @Expose
    public String userPassword;
    @SerializedName("suid")
    @Expose
    public String suid;
    @SerializedName("auth")
    @Expose
    public String auth;
    @SerializedName("user_email")
    @Expose
    public String userEmail;
    @SerializedName("user_image")
    @Expose
    public String userImage;
    @SerializedName("user_created")
    @Expose
    public String userCreated;
    @SerializedName("extra0")
    @Expose
    public String extra0;
    @SerializedName("extra1")
    @Expose
    public String extra1;
    @SerializedName("extra2")
    @Expose
    public String extra2;
    @SerializedName("uid")
    @Expose
    public String uid;


}
