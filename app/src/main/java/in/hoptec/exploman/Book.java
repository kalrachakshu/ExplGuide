package in.hoptec.exploman;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.facebook.Profile;
import com.firebase.client.Firebase;
import com.google.gson.Gson;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.hoptec.exploman.database.Guide;
import in.hoptec.exploman.database.Place;
import in.hoptec.exploman.database.Request;
import in.hoptec.exploman.utils.GenricCallback;

public class Book extends AppCompatActivity {

    Firebase debug,instance;
    public long SESSION_ID;


    Guide guide;

    Place place;

    GenricUser user;



    Request req=null;
    Gson js;
    public static String TAG="STEP 4 PAY";
    public   Context ctx;
    public   Activity act;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utl.fullScreen(this);
        setContentView(R.layout.activity_book);
        ctx=this;
        act=this;
        ButterKnife.bind(this);
        user=utl.readUserData();
        guide =utl.js.fromJson(getIntent().getStringExtra("guide"),Guide.class);
        place=utl.js.fromJson(getIntent().getStringExtra("place"),Place.class);



        js=new Gson();
        String json=getIntent().getStringExtra("cat");
        req =js.fromJson(json,Request.class);

        //step3Date(1,false, req);
        utl.e(TAG,json);

        utl.l(utl.js.toJson(place));


        upDateUI();




    }

    @BindView(R.id.slot1)
    TextView slot1;

    @BindView(R.id.slot2)
    TextView slot2;


    @BindView(R.id.slot3)
    TextView slot3;

    @BindView(R.id.name)
    TextView name;


    @BindView(R.id.email)
    TextView email;

    @BindView(R.id.phone)
    TextView phone;


    @BindView(R.id.more)
    TextView more;

    @BindView(R.id.categ)
    TextView categ;

    @BindView(R.id.level)
    TextView level;

    @BindView(R.id.pay)
    Button pay;

    AlertDialog dig;Profile profile;
    public String noOnce=null;
    /*************************EMAIL LOGIN**************************************/
    View v=null;
    public void step3Date(final int slot,final boolean datePicked,final Request request){

        AlertDialog.Builder di;
        if(!datePicked||v==null){

            if(dig!=null)
            {
                if(dig.isShowing())
                    dig.dismiss();
            }
            v=act.getLayoutInflater().inflate(R.layout.date_time_picker,null);
            di = new AlertDialog.Builder(ctx);
            di.setView(v);
            dig=di.create();
            dig.show();

        }


        if(v!=null) {
            final DatePicker date = (DatePicker) v.findViewById(R.id.datePicker1);
            final TimePicker time = (TimePicker) v.findViewById(R.id.timePicker1);
             final Button login = (Button) v.findViewById(R.id.login);


            if(datePicked)
            {
                date.setVisibility(View.GONE);
                time.setVisibility(View.VISIBLE);
            }else{
                time.setVisibility(View.GONE);
                date.setVisibility(View.VISIBLE);

            }



            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //dd MM yyyy - HH:ii p
                    Calendar cal=Calendar.getInstance();
                    SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
                    int monthnum=date.getMonth();
                    cal.set(Calendar.MONTH,monthnum);
                    String month_name = month_date.format(cal.getTime());

                 //   utl.snack(act,"min "+time.getCurrentMinute());
                    Integer selMin;
                    if(time.getCurrentMinute()>=30)
                    {
                        selMin=30;
                    }
                    else
                    {
                        selMin=0;
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        time.setMinute(selMin);
                    }
                    else
                    time.setCurrentMinute(selMin);

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(date.getYear(), date.getDayOfMonth(), date.getDayOfMonth(),
                            time.getCurrentHour(),selMin);

                    SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy - hh:mm aa");
                    String output = formatter.format(calendar.getTime()); //eg: "Tue May"


                    utl.e(TAG,"Selected : Slot "+slot+" -> "+output);

                  //  utl.e(TAG,"Selected : Slot "+slot+" -> "+d+" "+t);


                    switch (slot) {
                        case 1:
                            request.slot1 = output;
                            break;
                        case 2:
                            request.slot2 =output;

                            break;
                        case 3:
                            request.slot3 =output;

                            break;
                    }
                    if(!datePicked)
                    {
                        login.setText("DONE");

                        step3Date(slot,true,request);

                    }
                    else {

                        req = request;
                        dig.dismiss();   ;
                        upDateUI();
                    }



                }
            });

        }

    }

    public void createRequest()
    {

        req=new Request();
        req.email=user.user_email;
        req.guideName=guide.name;
        req.placeName=place.name;
        req.location =guide.address;
        req.phone=user.user_phone;
        req.price=guide.rate;
        req.user_id=user.uid;
        req.username=user.user_name;





    }

    public void upDateUI()
    {

        if(req==null)
        createRequest();



        if(req.slot1.equals(""))
        {
            slot1.setError("Pick Timing");
        }
        else {
            slot1.setError(null);
        }



        if(req.slot2.equals(""))
        {
            slot2.setError("Pick Timing");
        }
        else {
            slot2.setError(null);
        }



        if(req.slot3.equals(""))
        {
            slot3.setError("Pick Timing");
        }
        else {
            slot3.setError(null);
        }


        if(!req.slot1.equals(""))
        slot1.setText("Slot 1: "+req.slot1);
        else
            slot1.setText("Slot 1: PICK" );

        if(!req.slot2.equals(""))
        slot2.setText("Slot 2: "+req.slot2);
        else
        slot2.setText("Slot 2: PICK" );


        if(!req.slot3.equals(""))
        slot3.setText("Slot 3: "+req.slot3);
        else
        slot3.setText("Slot 3: PICK" );


        slot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                step3Date(1,false,req);
            }
        });

        slot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                step3Date(2,false,req);
            }
        });

        slot3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                step3Date(3,false,req);
            }
        });

        name.setText("Name : "+req.name);
        name.setVisibility(View.GONE);
        email.setText("E-Mail : "+req.email);
        email.setVisibility(View.GONE);
        phone.setText("Phone : "+req.phone);
        phone.setVisibility(View.GONE);
        more.setText("Pickup : "+req.location);

        categ.setText("Spot : "+req.placeName);
        level.setText("Guide : "+req.guideName);



        pay.setText("Book @  "+req.price+"  /Hour");
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Intent it=new Intent(ctx,Pay.class);
                it.putExtra("cat",req.toString());
                startActivity(it);
*/


                utl.snack(act.findViewById(R.id.activity_book), "Payment Gateway Not Set ! Use Wallet ?", "USE", new GenricCallback() {
                    @Override
                    public void onStart() {

                        book();

                    }

                    @Override
                    public void onDo(Object obj) {

                    }

                    @Override
                    public void onDo(Object obj, Object obj2) {

                    }

                    @Override
                    public void onDone(Object obj) {

                    }
                });


            }
        });
    }

    public void book()
    {

        /*

    place_ids
    guide_id
    user_id
    amount_total
    status
         */
        String url=Constants.HOST+Constants.API_GET_NEW_BOOK+"?"+
                "place_ids="+place.id+
                "guide_id="+guide.id+
                "user_id="+user.uid+
                "amount_total="+guide.rate+
                "status="+ URLEncoder.encode("PAID VIA WALLET");

        utl.l(url);
        utl.showDig(true,ctx);

        ;
        AndroidNetworking.get(url).build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                utl.showDig(false,ctx);
                utl.l(response);
                utl.diag(ctx, "Booking Confirmed !", "Booking is Complete . You Can view the transaction and Booking details from Home page .",false, "View", new utl.ClickCallBack() {
                    @Override
                    public void done(DialogInterface dialogInterface) {

                        Intent it=new Intent(ctx,Tours.class);
                        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(it);
                        finish();;

                    }
                });
            }

            @Override
            public void onError(ANError ANError) {
                utl.showDig(false,ctx);

            }
        });



    }


}
