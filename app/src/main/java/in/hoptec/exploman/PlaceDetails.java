package in.hoptec.exploman;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.hoptec.exploman.database.Place;
import in.hoptec.exploman.views.GoalProgressBar;

public class PlaceDetails extends AppCompatActivity {


    ImageView flip;



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


    @BindView(R.id.scrl)
    NestedScrollView scrl;


    @BindView(R.id.review)
    ImageView review;

    @BindView(R.id.go)
    ImageView go;

    @BindView(R.id.bookmark)
    ImageView bookmark;


    @BindView(R.id.rec)
    RecyclerView rec;


    public Context ctx;
    public Activity act;

    Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        flip=(ImageView)findViewById(R.id.flip);


        ButterKnife.bind(this);
        place=utl.js.fromJson(getIntent().getStringExtra("place"),Place.class);

        if(place!=null)
        {
            fill(place);
        }



    }

    public void fill(Place pla)
    {

        title.setText(pla.name);
        desc.setText(pla.desc);
        rate.setRating(pla.rating.floatValue());
        address.setText(pla.address);

        strip=pla.getImages();
        flip();




    }


    Handler h;
    Runnable r;
    int dr=0;
    ArrayList<String> strip;
    public void flip()
    {



        h=new Handler();
        r=new Runnable() {
            @Override
            public void run() {

                utl.l("Streip size "+strip.size());


                Picasso.with(ctx).load(strip.get(dr)).placeholder(R.drawable.placeholder).into(flip);
                if(dr<strip.size()-1)
                {
                    dr++;
                }
                else {
                    dr=0;
                }
                // flip.setImageResource(d);
                YoYo.with(Techniques.SlideInLeft).withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                YoYo.with(Techniques.SlideOutRight).withListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animator) {
                                        h.postDelayed(r, 10);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animator) {

                                    }
                                }).duration(200).playOn(flip);

                            }
                        },5000);

                        //   h.postDelayed(r,5000);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                }).duration(200).playOn(flip);
            }
        };


        h.postDelayed(r, 100);


    }




}
