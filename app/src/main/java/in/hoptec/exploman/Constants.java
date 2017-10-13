package in.hoptec.exploman;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by shivesh on 28/6/17.
 */

public class Constants {


//    public static String HOST="http://192.168.1.103/exploman";

    public static String HOST="http://explooman.com";
   // public static String HOST="https://bigpitch.000webhostapp.com/explooman";
    //  public static String HOST="http://10.42.0.81/exploman";

    public static String API_USER_REG_GET="/api/createuser.php";
    public static String API_USER_LOGIN_GET="/api/login.php";

    //http://127.0.0.1/exploman/api/get_places.php?mode=coor&lat=22.98&lng=57.53

    //@ GET mode=coor|search   lat,lng | query
    public static String API_GET_PLACES="/api/get_places.php";

    //@ GET mode=coor|search   lat,lng | query
    public static String API_GET_GUIDES="/api/get_guides.php";

    //@ GET mode
    /*
    write : place_id , user_id

   	$id=$GET["id"];
 	$place_id=$GET["place_id"];
 	$user_id=$GET["user_id"];
 	$rate=$GET["rate"];
 	$message=$GET["message"];
 	$date=$GET["date"];
 	$extra0=$GET["extra0"];

 	get_all :
 	place_id

 	del :
 	delid


     */
    public static String API_GET_PREVIEWS="/api/preview.php";



    //@ GET mode
    /*
    write : guide_id , user_id

   	$id=$GET["id"];
 	$place_id=$GET["guide_id"];
 	$user_id=$GET["user_id"];
 	$rate=$GET["rate"];
 	$message=$GET["message"];
 	$date=$GET["date"];
 	$extra0=$GET["extra0"];

 	get_all :
 	guide_id

 	del :
 	delid


     */
    public static String API_GET_GREVIEWS="/api/greview.php";

    //@GET user_id or guide_id and none for all bookings
    public static String API_GET_BOOKINGS="/api/get_bookings.php";


    //@GET
    /*
    place_ids
    guide_id
    user_id
    amount_total
    status
     */
    public static String API_GET_NEW_BOOK="/api/init_book.php";



    public static boolean IS_ANIMATED_BG_SPLASH=false;
    public static boolean isPdCancelable=true;

    public static String folder;
    public static String datafile;

    public static Context ctx;

    public static void init(Context context)
    {
        utl.init(ctx);
        ctx=context;
    }

    private static String FIRE_BASE="https://test-a0930.firebaseio.com/";

    public static String fireURL()
    {
        return Constants.FIRE_BASE+ utl.refineString(ctx.getResources().getString(R.string.app_name),"");
    }


    public static String getFolder()
    {
        folder = Environment.getExternalStorageDirectory().getPath().toString()+"/."+ utl.refineString(ctx.getResources().getString(R.string.app_name),"");
        return folder;
    }


    public static String userDataFile()
    {
        folder = Environment.getExternalStorageDirectory().getPath().toString()+"/."+ utl.refineString(ctx.getResources().getString(R.string.app_name),"");

        File file=new File(folder);
        if(!file.exists())
        {
            file.mkdir();
        }
        datafile=folder+"/firebaseUser.json";
        return datafile;
    }



    public static String localDataFile()
    {
        folder = Environment.getExternalStorageDirectory().getPath().toString()+"/."+ utl.refineString(ctx.getResources().getString(R.string.app_name),"");

        File file=new File(folder);
        if(!file.exists())
        {
            file.mkdir();
        }
        datafile=folder+"/data.json";
        return datafile;
    }



    public static String getApp()
    {
        return utl.refineString(ctx.getResources().getString(R.string.app_name),"");
    }











}
