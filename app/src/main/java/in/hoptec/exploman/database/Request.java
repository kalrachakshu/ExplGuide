package in.hoptec.exploman.database;

import com.google.gson.Gson;

/**
 * Created by shivesh on 2/4/17.
 */

public class Request {

    public String phone="",email="",name="", location =""  ;
    public String slot1="",slot2="",slot3="",slot;
     public String placeName ="", guideName ="";
    public String priceInr,priceUsd,price="";
    public String user_id,username;


    @Override
    public  String toString()
    {
        Gson js=new Gson();
        return js.toJson(this);
    }



}
