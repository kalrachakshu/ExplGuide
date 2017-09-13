package in.hoptec.exploman;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.hoptec.exploman.adapters.GuideAdapter;
import in.hoptec.exploman.database.Guide;
import in.hoptec.exploman.database.Place;
import in.hoptec.exploman.database.Review;

public class Guides extends AppCompatActivity {

    public Context ctx;
    public Activity act;
    @BindView(R.id.write_rev)
    ImageView write_rev;

    @BindView(R.id.splash_view2)
    AVLoadingIndicatorView loading;
    @BindView(R.id.rec)
    RecyclerView rec;

    @BindView(R.id.review_plc)
    LinearLayout review_plc;


    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.address)
    TextView address;

    GenricUser user;
    Place place;

    ArrayList<Guide> guides;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utl.fullScreen(this);
        ctx=this;
        act=this;
        setContentView(R.layout.activity_guides);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        user=utl.readUserData();



        ButterKnife.bind(this);
        place=utl.js.fromJson(getIntent().getStringExtra("cat"),Place.class);

        if(place!=null)
        {
            fill(place);
        }


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        setTitle("");

        load(LOADING);
        String url=Constants.HOST+Constants.API_GET_GUIDES+"?place_id="+place.id;
        AndroidNetworking.get(url).build().getAsJSONArray(new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray response) {


                utl.l(response);

                guides =new ArrayList<Guide>();
                for(int i=0;i<response.length();i++)
                {
                    try {
                        guides.add(utl.js.fromJson(response.get(i).toString(),Guide.class));
                        guides.add(utl.js.fromJson(response.get(i).toString(),Guide.class));
                        guides.add(utl.js.fromJson(response.get(i).toString(),Guide.class));
                        guides.add(utl.js.fromJson(response.get(i).toString(),Guide.class));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                setUpGuides(guides);

            }

            @Override
            public void onError(ANError ANError) {
                load(EMPTY);

            }
        });

    }

    public void fill(Place pla)
    {

        utl.l(pla.toString());
        title.setText("Guides");
        address.setText(pla.name);



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


    public void setUpGuides(ArrayList<Guide> reviews)
    {
        if(reviews.size()==0)
        {
            load(EMPTY);
            return;
        }
        load(LOADED);

        GuideAdapter adapter=new GuideAdapter(ctx, reviews, new GuideAdapter.CallBacks() {
            @Override
            public void save(Guide cat, int id) {

            }

            @Override
            public void car(Guide cat, boolean like) {

            }

            @Override
            public void click(Guide cat, int id, View v) {


                Intent it = new Intent(ctx, GuideDetails.class);

                it.putExtra("guide", utl.js.toJson(cat));


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(act,v, getString(R.string.activity_image_trans));
                    startActivity(it, options.toBundle());
                } else {
                    startActivity(it);
                }


            }
        });
        rec.setLayoutManager(new LinearLayoutManager(ctx));
        rec.setAdapter(adapter);



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
