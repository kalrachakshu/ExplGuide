package in.hoptec.exploman;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

import java.util.ArrayList;

import in.hoptec.exploman.database.Place;

/**
 * Created by shivesh on 28/6/17.
 */

public class BaseApp extends Application {

    public static String last_url;

    @Override
    public void onCreate()
    {
        Constants.init(this);
        AndroidNetworking.initialize(this);
    }


}
