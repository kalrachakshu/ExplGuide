package in.hoptec.exploman;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.JsonSyntaxException;
import com.nineoldandroids.animation.Animator;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.hoptec.exploman.adapters.FlipAdapter;
import in.hoptec.exploman.adapters.ReviewAdapter;
import in.hoptec.exploman.database.Place;
import in.hoptec.exploman.database.Review;
import in.hoptec.exploman.utils.GenricCallback;
import in.hoptec.exploman.views.GoalProgressBar;

public class PlaceDetails extends AppCompatActivity {


    @BindView(R.id.visit0)
    Button visit0;

    @BindView(R.id.rev0)
    Button rev0;

    @BindView(R.id.save0)
    Button save0;



    ImageView flip;


    @BindView(R.id.write_rev)
    ImageView write_rev;

    @BindView(R.id.write_rev2)
    Button write_rev2;

    @BindView(R.id.review_plc)
    LinearLayout review_plc;

    @BindView(R.id.splash_view2)
    AVLoadingIndicatorView loading;

    @BindView(R.id.prog)
    GoalProgressBar prog;



    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.address)
    TextView address;

    @BindView(R.id.rating)
    AppCompatRatingBar rate;

    @BindView(R.id.opt)
    ImageView userimage;

    @BindView(R.id.desc)
    TextView desc;


    @BindView(R.id.review)
    ImageView review;

    @BindView(R.id.go)
    ImageView go;

    @BindView(R.id.bookmark)
    ImageView bookmark;


    @BindView(R.id.rec)
    RecyclerView rec;


    ArrayList<Review> reviews;
    public Context ctx;
    public Activity act;

    GenricUser user;
    Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utl.fullScreen(this);
        setContentView(R.layout.activity_place);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ctx=this;
        act=this;
        user=utl.readUserData();

        flip=(ImageView)findViewById(R.id.flip);


        ButterKnife.bind(this);
        try {
           // place=utl.js.fromJson(getIntent().getStringExtra("guide"),Place.class);

            JSONObject jo=new JSONObject(getIntent().getStringExtra("guide"));
            place=new Place();

            place.name=jo.getString("name");
            place.id=jo.getString("id");
            place.desc=jo.getString("desc");
            place.distance=jo.getString("distance");
            place.lat=jo.getDouble("lat");
            place.lng=jo.getDouble("lng");
            place.address=jo.getString("address");
            place.images=jo.getString("images");
            place.rating=jo.getDouble("rating");
            place.marked=jo.getBoolean("marked");



        } catch (Exception e) {
            e.printStackTrace();
        }

        utl.l(getIntent().getStringExtra("guide"));

          if(place!=null)
        {
            fill(place);
        }


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        setTitle("");





        utl.changeColorDrawable(review,R.color.ic_accent1);
        utl.changeColorDrawable(bookmark,R.color.ic_accent1);
        utl.changeColorDrawable(go,R.color.button_accent1);


        write_rev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                write();
            }
        });

        write_rev2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                write();
            }
        });

        rev0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                write();
            }
        });

        visit0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(act, v, getString(R.string.activity_image_trans));
                startActivity(intent, options.toBundle());
            }
            else {
                startActivity(intent);
            }*/

                //finish();



                Intent intent=new Intent(ctx,Guides.class);
                intent.putExtra("cat",utl.js.toJson(place));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(act, view, getString(R.string.activity_image_trans2));
                    startActivity(intent, options.toBundle());
                }
                else {
                    startActivity(intent);
                }

            }
        });

        save0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

    }

    public void save()
    {



        utl.snack(act,place.name+" added to Favorites !");


    }
    BottomSheetDialog mBottomSheetDialog;
    Integer [] images={R.drawable.one,R.drawable.two,R.drawable.three,R.drawable.four,R.drawable.five};
     Drawable  [] _img;
    Drawable  [] img;
    Float ratingf=5f;
    public void write()
    {




        _img=new Drawable[5];
        img=new Drawable[5];

        for (int i=0;i<5;i++)
            img[i]=getResources().getDrawable(images[i]);

        for (int i=0;i<5;i++)
            _img[i]=getResources().getDrawable(images[i]);

        for (int i=0;i<5;i++)
            DrawableCompat.setTint(_img[i], ContextCompat.getColor(ctx, R.color.grey_400));



         mBottomSheetDialog = new BottomSheetDialog(act);
        View sheetView = act.getLayoutInflater().inflate(R.layout.write_rev, null);

        final EditText
                text=(EditText)sheetView.findViewById(R.id.text);
        Button done=(Button)sheetView.findViewById(R.id.done);

        final LinearLayout rating_holder=(LinearLayout)sheetView.findViewById(R.id.rating_holder);


        for(int i=0;i<rating_holder.getChildCount();i++)
        {
            final  Integer pos=i;
            final ImageView view=(ImageView)rating_holder.getChildAt(i);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view22) {

                    ratingf=pos.floatValue()+1f;
                    utl.l("Rtinf "+ratingf);

                    for(int i=0;i<rating_holder.getChildCount();i++)
                    {

                        final int pp=i;
                        final ImageView view2=(ImageView)rating_holder.getChildAt(pp);

                        view2.setImageDrawable(_img[pp]);

                       // view2.setImageResource(_images[pos]);
                    }
                    view.setImageDrawable(img[pos]);
                //  view.setImageResource(images[pos]);


                }
            });


        }
        for(int i=0;i<rating_holder.getChildCount();i++)
        {

                final ImageView view2 = (ImageView) rating_holder.getChildAt(i);
                utl.changeColorDrawable(view2, R.color.grey_400);


        }

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if((text.getText().toString()).length()<1)
                {
                    text.setError("Must not be empty !");

                }else {
                    mBottomSheetDialog.dismiss();
                    String url=Constants.HOST+Constants.API_GET_PREVIEWS+"?";
                    url+="place_id="+place.id;
                    url+="&user_id="+user.uid;
                    url+="&rating="+ratingf;
                    url+="&message="+ URLEncoder.encode(text.getText().toString());
                    url+="&extra0="+ URLEncoder.encode(utl.getFCMToken());

                    utl.l(url);
                    AndroidNetworking.get(url).build().getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            utl.snack(act,response);
                            getReviews(place.id);
                        }

                        @Override
                        public void onError(ANError ANError) {

                            utl.snack(act,"Error Occured");
                        }
                    });

                }

                /*

 	$place_id=$GET["place_id"];
 	$user_id=$GET["user_id"];
 	$rate=$GET["rate"];
 	$message=$GET["message"];
 	$date=$GET["date"];
 	$extra0=$GET["extra0"];
                 */





            }
        });





        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();







    }
   final int LOADING=12,EMPTY=13,LOADED=14;


     public void load(int state)
    {

        if(state==LOADING)
        {
            loading.setVisibility(View.VISIBLE);
            rec.setVisibility(View.GONE);
            review_plc.setVisibility(View.GONE);
        }


        if(state==EMPTY)
        {
            loading.setVisibility(View.GONE);
            rec.setVisibility(View.GONE);
            review_plc.setVisibility(View.VISIBLE);
            review_plc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    write();
                }
            });
            utl.changeColorDrawable(write_rev,R.color.grey_500);


        }


        if(state==LOADED)
        {
            loading.setVisibility(View.GONE);
            rec.setVisibility(View.VISIBLE);
            review_plc.setVisibility(View.GONE);
        }


    }


    public void getReviews(String place_id)
    {

        load(LOADING);
        String url=Constants.HOST+Constants.API_GET_PREVIEWS+"?place_id="+place_id;
        AndroidNetworking.get(url).build().getAsJSONArray(new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray response) {

                utl.l(response);

                reviews=new ArrayList<Review>();
                for(int i=0;i<response.length();i++)
                {
                    try {
                        reviews.add(utl.js.fromJson(response.get(i).toString(),Review.class));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                setUpReviews(reviews);


            }

            @Override
            public void onError(ANError ANError) {

                utl.l(ANError.getErrorDetail());
                load(EMPTY);
            }
        });



    }
    public void setUpReviews(ArrayList<Review> reviews)
    {
        if(reviews.size()==0)
        {
            load(EMPTY);
            return;
        }
        load(LOADED);

        ReviewAdapter adapter=new ReviewAdapter(ctx, reviews, new ReviewAdapter.CallBacks() {
            @Override
            public void share(Review cat, int id) {

            }

            @Override
            public void like(final Review cat, boolean like) {

                if(cat.userId.equals(user.uid)){
                utl.snack(findViewById(R.id.activity_place), "Delete this comment ? ", "DELETE", new GenricCallback() {
                    @Override
                    public void onStart() {

                        String url=Constants.HOST+Constants.API_GET_PREVIEWS+"?delid="+cat.id;
                        utl.l(url);
                        AndroidNetworking.get(url).build().getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                utl.snack(act,"Deleted !");
                                getReviews(place.id);
                            }

                            @Override
                            public void onError(ANError ANError) {

                            }
                        });
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
            }}

            @Override
            public void click(Review cat, int id, View v) {

            }
        });
        rec.setLayoutManager(new LinearLayoutManager(ctx));
        rec.setAdapter(adapter);

        rec.setNestedScrollingEnabled(false);


    }
    public void fill(Place pla)
    {

        utl.l(pla.toString());
        title.setText(pla.name);
        desc.setText(pla.desc);
        rate.setRating(pla.rating.floatValue());
        address.setText(pla.address);

        strip=pla.getImages();
        flip();

        getReviews(pla.id);



    }


    Handler h;
    Runnable r;
    int trip=0;
    ArrayList<String> strip;
    @BindView(R.id.flipper)
    RecyclerView flipper;

    public void flip()
    {


        FlipAdapter adapter=new FlipAdapter(ctx, strip, new FlipAdapter.CallBacks() {
            @Override
            public void share(String  cat, int id) {

            }

            @Override
            public void like(String cat, boolean like) {

            }

            @Override
            public void click(String cat, int id, View v) {

            }
        });
        flipper.setLayoutManager(new LinearLayoutManager(ctx));
        flipper.setAdapter(adapter);


        if(adapter.feedItemList.size()<2)
            return;

    h=new Handler();
        r=new Runnable() {
            @Override
            public void run() {

                if(trip>=strip.size())
                {
                    trip=0;
                }
                flipper.getLayoutManager().smoothScrollToPosition(flipper,null,trip++);
                // recyclerView.getLayoutManager().smoothScrollToPosition();
                h.postDelayed(r,4000);
            }
        };
        h.postDelayed(r,4000);


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {


        Intent it=new Intent(ctx,Landing.class);
        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(it);
        finish();;

    }
}
