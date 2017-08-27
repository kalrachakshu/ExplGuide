package in.hoptec.exploman;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.hoptec.exploman.adapters.FlipAdapter;
import in.hoptec.exploman.adapters.ReviewAdapter;
import in.hoptec.exploman.database.Place;
import in.hoptec.exploman.database.Review;
import in.hoptec.exploman.views.GoalProgressBar;

public class PlaceDetails extends AppCompatActivity {


    ImageView flip;


    @BindView(R.id.write_rev)
    ImageView write_rev;

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

    Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ctx=this;
        act=this;

        flip=(ImageView)findViewById(R.id.flip);


        ButterKnife.bind(this);
        place=utl.js.fromJson(getIntent().getStringExtra("place"),Place.class);

        if(place!=null)
        {
            fill(place);
        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        setTitle("");


        utl.changeColorDrawable(review,R.color.ic_accent1);
        utl.changeColorDrawable(bookmark,R.color.ic_accent1);
        utl.changeColorDrawable(go,R.color.button_accent1);



    }
    public void write()
    {}
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
            public void like(Review cat, boolean like) {

            }

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
}
