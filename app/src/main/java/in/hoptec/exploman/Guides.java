package in.hoptec.exploman;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.hoptec.exploman.database.Place;

public class Guides extends AppCompatActivity {

    public Context ctx;
    public Activity act;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.address)
    TextView address;

    GenricUser user;
    Place place;
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
        place=utl.js.fromJson(getIntent().getStringExtra("place"),Place.class);

        if(place!=null)
        {
            fill(place);
        }


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        setTitle("");

    }

    public void fill(Place pla)
    {

        utl.l(pla.toString());
        title.setText("Guides");
        address.setText(pla.name);



    }




}
