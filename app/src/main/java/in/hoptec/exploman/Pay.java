package in.hoptec.exploman;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.TextView;

import com.facebook.Profile;
import com.google.gson.Gson;

import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.hoptec.exploman.database.Request;
import in.hoptec.exploman.utils.AdvancedWebView;

public class Pay extends AppCompatActivity {
    Request req;
    Gson js;
    public static String TAG="STEP 4 PAY";
    public static Context ctx;
    public static Activity act;

    @BindView(R.id.webview)
    AdvancedWebView webView;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
/*
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        */

        ctx=this;
        act=this;
        ButterKnife.bind(this);


        js=new Gson();
        String json=getIntent().getStringExtra("cat");
        req =js.fromJson(json,Request.class);

        //step3Date(1,false, req);
        utl.e(TAG,json);
//http://feelinglone.com/pay/payu_pay.php?
// name=xyz
// &email=xyz%40domain.com
// &phone=9000000001
// &info=TEST
// &slot1=23+March+2017+-+08%3A25+am
// &slot2=02+March+2017+-+03%3A10+am
// &slot3=10+March+2017+-+09%3A30+am
// &price=1000
// &location=TEST
// &catid=5
// &levelid=3
// &user_id=5902
// &firstname=xyz
/*

        String url;
        url=Constants.HOST+"/pay/payu_pay.php?";
        if(req.location.toLowerCase().contains("usd")||!req.curr.toLowerCase().equals("inr"))
        {
            url =Constants.HOST+"/pay/payp_pay.php?";
        }
        url+="email="+ URLEncoder.encode(req.email);
        url+="&name="+URLEncoder.encode(req.username);;
        url+="&phone="+URLEncoder.encode(req.phone);;
        url+="&info="+URLEncoder.encode(req.location);;
        url+="&slot1="+URLEncoder.encode(req.slot1);;
        url+="&slot2="+URLEncoder.encode(req.slot2);;
        url+="&slot3="+URLEncoder.encode(req.slot3);;
        url+="&price="+URLEncoder.encode(req.price);;
        url+="&location="+URLEncoder.encode(req.location);;
        url+="&catid="+URLEncoder.encode(req.catId);;
        url+="&levelid="+URLEncoder.encode(req.levelId);;
        url+="&firstname="+URLEncoder.encode(req.name);;
        url+="&user_id="+URLEncoder.encode(req.user_id);;

        utl.e(TAG,"Pay url : "+url);
        webView.addJavascriptInterface(new JavaScriptInterface(this), "Android");
        webView.loadUrl(url);
*/






    }




    public class JavaScriptInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        JavaScriptInterface(Context c) {
            mContext = c;
        }

        /** Show a toast from the web page */
        @JavascriptInterface
        public void showToast(String toast) {
           // toast=toast.replace("_11_","\"");
       //     Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
          //  utl.e(TAG,"RESPONSE FROM PAYG : "+toast);

           // finish();
        }


        @JavascriptInterface
        public void success(String toast) {
            toast=toast.replace("_11_","\"");

//            Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
            showStatus(toast);


        }


        @JavascriptInterface
        public void failure(String toast) {
            toast=toast.replace("_11_","\"");
            utl.e(TAG,"RESPONSE FROM PAYG : "+toast);
            showStatus(toast);
        }



    }



    AlertDialog dig;Profile profile;
    public String noOnce=null;
    /*************************EMAIL LOGIN**************************************/
    View v=null;



        public void showStatus(String json)
    {
/*

       final PayUResp payUResp=js.fromJson(json,PayUResp.class);

        final AlertDialog.Builder di;

        v=act.getLayoutInflater().inflate(R.layout.pay_status,null);
        di = new AlertDialog.Builder(ctx);
        di.setView(v);
        dig=di.create();
        dig.show();


        if(v!=null) {
            final TextView txnid = (TextView) v.findViewById(R.id.txnid);
            final TextView amount = (TextView) v.findViewById(R.id.amount);
            final TextView status = (TextView) v.findViewById(R.id.status);
            final ImageView img = (ImageView) v.findViewById(R.id.img);

            final Button login = (Button) v.findViewById(R.id.login);

            txnid.setText("TXN ID : "+payUResp.txnid);
            amount.setText("Amount : "+payUResp.amount);
            status.setText("Status : "+payUResp.status);

            if(payUResp.status.toLowerCase().equals("failure")||payUResp.status.toLowerCase().contains("fail")) {

                img.setImageResource(R.drawable.order_failed);
              //  Picasso.with(ctx).load(R.drawable.order_failed).into(img);
            }  else {


                img.setImageResource(R.drawable.order_placed);

                //Picasso.with(ctx).load(R.drawable.order_placed).into(img);
            }
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dig.dismiss();;

                    Intent intent = new Intent(getApplicationContext(), Home.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    finish();
                }
            });


        }
*/


    }


    @Override
    public void onBackPressed()
    {

        String t="Are you sure you want to Cancel ?";

        View rootView = act.getWindow().getDecorView().getRootView();
        //Snackbar snackbar = Snackbar.make(rootView, Html.fromHtml("<font color=\"#fff\">"+t+"</font>" ), Snackbar.LENGTH_LONG);
        Snackbar snackbar = Snackbar.make(rootView, ""+t , Snackbar.LENGTH_LONG);

        snackbar.setActionTextColor(act.getResources().getColor(R.color.blue_100));

        snackbar.setAction("CANCEL", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(act.getResources().getColor(R.color.red_300));

// change snackbar text color
        int snackbarTextId = android.support.design.R.id.snackbar_text;
        TextView textView = (TextView)snackbarView.findViewById(snackbarTextId);
        textView.setTextColor(act.getResources().getColor(R.color.white));


        if(utl.DISPLAY_ENABLED)
            snackbar.show();


     //   utl.snack(this,"You cant go back at this stage !");




    }


}
