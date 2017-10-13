package in.hoptec.exploman;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;

import java.net.URLEncoder;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.hoptec.exploman.adapters.ReviewAdapter;
import in.hoptec.exploman.database.Guide;
import in.hoptec.exploman.database.Place;
import in.hoptec.exploman.database.Review;
import in.hoptec.exploman.utils.GenricCallback;

public class GuideDetails extends AppCompatActivity {




    @BindView(R.id.img)
    ImageView imag;

    @BindView(R.id.write_rev)
    ImageView write_rev;

    @BindView(R.id.write_rev2)
    Button write_rev2;

    @BindView(R.id.review_plc)
    LinearLayout review_plc;


    @BindView(R.id.splash_view2)
    AVLoadingIndicatorView loading;


    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.address)
    TextView address;


    @BindView(R.id.rating)
    TextView rating;



    @BindView(R.id.tours)
    TextView hourly;



    @BindView(R.id.rec)
    RecyclerView rec;


    ArrayList<Review> reviews;
    public Context ctx;
    public Activity act;

    Guide guide;

    Place place;

    GenricUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utl.fullScreen(this);
        setContentView(R.layout.activity_guide);

        ctx=this;
        act=this;
        utl.init(getApplicationContext());
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        setTitle("");






        user=utl.readUserData();
        utl.l(user);
        guide =utl.js.fromJson(getIntent().getStringExtra("guide"),Guide.class);
        place=utl.js.fromJson(getIntent().getStringExtra("place"),Place.class);

        fill(guide);

        findViewById(R.id.go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent it = new Intent(ctx, Book.class);

                it.putExtra("guide", utl.js.toJson(guide));
                it.putExtra("place", utl.js.toJson(place));


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(act,findViewById(R.id.gd), getString(R.string.activity_image_trans));
                    startActivity(it, options.toBundle());
                } else {
                    startActivity(it);
                }



            }
        });

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                utl.snack(act,"Guide Bio Here");


            }
        });

    }



    public void fill(Guide pla)
    {

        utl.l(pla.toString());
        title.setText(""+pla.name);
        rating.setText(""+pla.rating);
        address.setText(""+pla.address);
        hourly.setText(""+pla.rate);


        try {
            Picasso.with(ctx).load(pla.getImages().get(0)).placeholder(R.drawable.user).into(imag);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getReviews(pla.id);



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
            utl.changeColorDrawable(write_rev,R.color.offwhite);


        }


        if(state==LOADED)
        {
            loading.setVisibility(View.GONE);
            rec.setVisibility(View.VISIBLE);
            review_plc.setVisibility(View.GONE);
        }
        write_rev2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                write();
            }
        });
        write_rev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                write();
            }
        });


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
                    String url=Constants.HOST+Constants.API_GET_GREVIEWS+"?";
                    url+="guide_id="+ guide.id;
                    url+="&user_id="+user.uid;
                    url+="&rating="+ratingf;
                    url+="&message="+ URLEncoder.encode(text.getText().toString());
                    url+="&extra0="+ URLEncoder.encode(utl.getFCMToken());

                    utl.l(url);
                    AndroidNetworking.get(url).build().getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            utl.snack(act,response);
                            getReviews(guide.id);
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
    public void getReviews(String place_id)
    {

        load(LOADING);
        String url=Constants.HOST+Constants.API_GET_GREVIEWS+"?guide_id="+place_id;
        utl.l(url);
        AndroidNetworking.get(url).build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String respons) {


                utl.l("RESPO\n"+respons);
                JSONArray response;


                try {
                    response=new JSONArray(respons);
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
                } catch (Exception e) {
                    e.printStackTrace();
                }


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

                utl.snack(findViewById(R.id.activity_guide), "Delete this comment ? ", "DELETE", new GenricCallback() {
                    @Override
                    public void onStart() {

                        String url=Constants.HOST+Constants.API_GET_GREVIEWS+"?delid="+cat.id;
                        utl.l(url);
                        AndroidNetworking.get(url).build().getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                utl.snack(act,"Deleted !");
                                getReviews(guide.id);
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
            }

            @Override
            public void click(Review cat, int id, View v) {

            }
        });
        rec.setLayoutManager(new LinearLayoutManager(ctx));
        rec.setAdapter(adapter);

        rec.setNestedScrollingEnabled(false);


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
