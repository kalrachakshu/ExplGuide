package in.hoptec.exploman;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;

import java.util.ArrayList;

import in.hoptec.exploman.adapters.TPAdapter;
import in.hoptec.exploman.database.TPackage;
import me.kaelaela.verticalviewpager.VerticalViewPager;
import me.kaelaela.verticalviewpager.transforms.ZoomOutTransformer;

public class TourPackages extends AppCompatActivity implements TourFragment.OnFragmentInteractionListener {

    public static AVLoadingIndicatorView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        utl.fullScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_packages);
        loading=(AVLoadingIndicatorView)findViewById(R.id.splash_view2);
        getShowcases();
    }





    public void getShowcases()
    {


        load(LOADING);
        String url=Constants.HOST+Constants.API_GET_TOUR_PACKAGES;
        utl.l("URL : "+url);
         AndroidNetworking.get(url).build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        utl.l("Response offers "+response);

                        offers = new ArrayList<>();
                        load(LOADED);

                        try{


                            JSONArray jo=new JSONArray(response);

                            for(int i=0;i<jo.length();i++)
                            {
                                TPackage pk=utl.js.fromJson(jo.get(i).toString(),TPackage.class);
                                offers.add(pk);
                            }






                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }


                        setUpLists(offers);




                    }

                    @Override
                    public void onError(ANError ANError) {

                        utl.l(""+ANError.getErrorDetail());
                        load(LOADED);
                    }
                });


    }
    int i=0;
    ArrayList<TPackage> offers;
    private void setUpLists(final ArrayList<TPackage> offers) {
        VerticalViewPager viewPager = (VerticalViewPager) findViewById(R.id.vertical_viewpager);
        viewPager.setPageTransformer(false, new ZoomOutTransformer());
        //viewPager.setPageTransformer(true, new StackTransformer());
        String title = "ContentFragment";
        final TPAdapter.Holder holder= new TPAdapter.Holder(getSupportFragmentManager());

        i=0;
        for(TPackage item:offers)
        {
            holder.add(TourFragment.newInstance(item, ""+i));

            i++;
        }

        final TPAdapter adapter= holder.set();


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {





            }

            @Override
            public void onPageSelected(int position) {
                try {
                    View view,dow,up;
                    view=adapter.getItem(position).getView();
                    dow=view.findViewById(R.id.dow);
                    up=view.findViewById(R.id.up);
                    if(position==0)
                    {
                        up.setVisibility(View.GONE);
                    }
                    else {
                        up.setVisibility(View.VISIBLE);
                    }

                    if(position>=offers.size()-1)
                    {
                        dow.setVisibility(View.GONE);
                    }
                    else {
                        dow.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        viewPager.setAdapter(adapter);

        //If you setting other scroll mode, the scrolled fade is shown from either side of display.
        viewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }




    @Override
    public void onBackPressed() {


        Intent it=new Intent(this,Landing.class);
        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(it);
        finish();;

    }


    final static int LOADING=12,EMPTY=13,LOADED=14;


    public static void load(int state)
    {

        if(state==LOADING)
        {
            loading.setVisibility(View.VISIBLE);
        }


        if(state==EMPTY)
        {
            loading.setVisibility(View.GONE);


        }


        if(state==LOADED)
        {
            loading.setVisibility(View.GONE);
        }


    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
