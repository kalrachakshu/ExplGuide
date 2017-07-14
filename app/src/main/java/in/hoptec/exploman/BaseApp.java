package in.hoptec.exploman;

import android.app.Application;

/**
 * Created by shivesh on 28/6/17.
 */

public class BaseApp extends Application {

    @Override
    public void onCreate()
    {
        Constants.init(this);
    }


}
