package in.hoptec.exploman;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.mikepenz.crossfadedrawerlayout.view.CrossfadeDrawerLayout;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.MiniDrawer;
import com.mikepenz.materialdrawer.interfaces.ICrossfader;
import com.mikepenz.materialdrawer.model.MiniDrawerItem;
import com.mikepenz.materialdrawer.model.MiniProfileDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialize.util.UIUtils;

import org.json.JSONArray;

import java.util.ArrayList;

import in.hoptec.exploman.adapters.ReviewAdapter;
import in.hoptec.exploman.adapters.TransAdapter;
import in.hoptec.exploman.database.Booking;
import in.hoptec.exploman.database.Review;
import in.hoptec.exploman.utils.GenricCallback;

public class Tours extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */

     private ViewPager mViewPager;
    public static GenricUser user;
    public static Context ctx;
    public Activity act;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tours);

        ctx=getApplicationContext();
        act=this;
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        user=utl.readUserData();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
       // mViewPager.setCurrentItem(1,true);
       // mViewPager.setCurrentItem(1,true);

        setTitle("");
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                utl.snack(act, "Refill Wallet ", "ADD", new GenricCallback() {
                    @Override
                    public void onStart() {

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
        setUpDrawer(savedInstanceState);

    }

    public static void setUpReviews(ArrayList<Booking> reviews,RecyclerView rec)
    {

        TransAdapter adapter=new TransAdapter(ctx, reviews, new TransAdapter.CallBacks() {
            @Override
            public void share(Booking cat, int id) {

            }

            @Override
            public void like(final Booking cat, boolean like) {

            }

            @Override
            public void click(Booking cat, int id, View v) {

            }
        });
        rec.setLayoutManager(new LinearLayoutManager(ctx));
        rec.setAdapter(adapter);

        rec.setNestedScrollingEnabled(false);


    }


    public static void getBookings(final RecyclerView rec)
    {

        String url= Constants.HOST+Constants.API_GET_BOOKINGS+"?user_id="+user.uid;

        utl.l(url);
        utl.showDig(true,ctx);

        AndroidNetworking.get(url).build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String respons) {
                try{
                    utl.showDig(false,ctx);


                    utl.l(respons);


                    JSONArray response=new JSONArray(respons);

                    ArrayList<Booking> reviews=new ArrayList<Booking>();
                    for(int i=0;i<response.length();i++)
                    {
                        try {
                            reviews.add(utl.js.fromJson(response.get(i).toString(),Booking.class));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    setUpReviews(reviews,rec);







                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError ANError) {
                utl.showDig(false,ctx);

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_tours, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tours, container, false);
               RecyclerView  rec=(RecyclerView) rootView.findViewById(R.id.rec);

            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

                getBookings(rec);
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "My Tours";
                case 1:
                    return "Wallet";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }





















    private AccountHeader headerResult = null;
    private Drawer result = null;
    private CrossfadeDrawerLayout crossfadeDrawerLayout = null;
    public void setUpDrawer(Bundle savedInstanceState)
    {
        final IProfile profile = new ProfileDrawerItem().withName(""+user.user_fname).withEmail(""+user.user_email).withIcon(user.user_image).withTypeface(utl.getFace(utl.CLAN_PRO_NORMAL,ctx));


        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.bg_black)
                .addProfiles(
                        profile
                )
                .withSavedInstance(savedInstanceState)
                .build();

        final PrimaryDrawerItem home=new PrimaryDrawerItem().withName("Home").withIcon(GoogleMaterial.Icon.gmd_home).withIdentifier(1).withTypeface(utl.getFace(utl.CLAN_PRO_NORMAL,ctx)),
                tours=new PrimaryDrawerItem().withName("My Tours").withIcon(FontAwesome.Icon.faw_map_marker).withTypeface(utl.getFace(utl.CLAN_PRO_NORMAL,ctx)),
                wallet=new PrimaryDrawerItem().withName("Wallet").withIcon(FontAwesome.Icon.faw_google_wallet).withTypeface(utl.getFace(utl.CLAN_PRO_NORMAL,ctx)),
                help=new PrimaryDrawerItem().withName("Help & Support").withIcon(GoogleMaterial.Icon.gmd_help).withTypeface(utl.getFace(utl.CLAN_PRO_NORMAL,ctx)),
                logout=new PrimaryDrawerItem().withName("Logout").withIcon(GoogleMaterial.Icon.gmd_delete).withTypeface(utl.getFace(utl.CLAN_PRO_NORMAL,ctx));

        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withDrawerLayout(R.layout.cross_fade)
                .withDrawerWidthDp(72)
                .withGenerateMiniDrawer(true)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(

                        home,tours,wallet, new SectionDrawerItem() ,help,logout

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if(drawerItem==logout)
                        {


                            utl.logout();
                            finish();
                            return false;
                        }else if(drawerItem==home)
                        {

                            onBackPressed();

                        }
                        if (drawerItem instanceof Nameable) {
                            Toast.makeText(ctx, ((Nameable) drawerItem).getName().getText(ctx), Toast.LENGTH_SHORT).show();
                        }
                        //we do not consume the event and want the Drawer to continue with the event chain
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .build();


        //get the CrossfadeDrawerLayout which will be used as alternative DrawerLayout for the Drawer
        //the CrossfadeDrawerLayout library can be found here: https://github.com/mikepenz/CrossfadeDrawerLayout
        crossfadeDrawerLayout = (CrossfadeDrawerLayout) result.getDrawerLayout();

        //define maxDrawerWidth
        crossfadeDrawerLayout.setMaxWidthPx(DrawerUIUtils.getOptimalDrawerWidth(this));
        //add second view (which is the miniDrawer)
        final MiniDrawer miniResult = result.getMiniDrawer();
        //build the view for the MiniDrawer
        View view = miniResult.build(this);
        //set the background of the MiniDrawer as this would be transparent
        view.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(this, com.mikepenz.materialdrawer.R.attr.material_drawer_background, com.mikepenz.materialdrawer.R.color.material_drawer_background));
        //we do not have the MiniDrawer view during CrossfadeDrawerLayout creation so we will add it here
        crossfadeDrawerLayout.getSmallView().addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //define the crossfader to be used with the miniDrawer. This is required to be able to automatically toggle open / close
        miniResult.withCrossFader(new ICrossfader() {
            @Override
            public void crossfade() {
                boolean isFaded = isCrossfaded();
                crossfadeDrawerLayout.crossfade(400);

                //only close the drawer if we were already faded and want to close it now
                if (isFaded) {
                    result.getDrawerLayout().closeDrawer(GravityCompat.START);
                }
            }

            @Override
            public boolean isCrossfaded() {
                return crossfadeDrawerLayout.isCrossfaded();
            }
        });


        /**
         * NOTE THIS IS A HIGHLY CUSTOM ANIMATION. USE CAREFULLY.
         * this animate the height of the profile to the height of the AccountHeader and
         * animates the height of the drawerItems to the normal drawerItems so the difference between Mini and normal Drawer is eliminated
         **/

        final double headerHeight = DrawerUIUtils.getOptimalDrawerWidth(this) * 9d / 16d;
        final double originalProfileHeight = UIUtils.convertDpToPixel(72, this);
        final double headerDifference = headerHeight - originalProfileHeight;
        final double originalItemHeight = UIUtils.convertDpToPixel(64, this);
        final double normalItemHeight = UIUtils.convertDpToPixel(48, this);
        final double itemDifference = originalItemHeight - normalItemHeight;
        crossfadeDrawerLayout.withCrossfadeListener(new CrossfadeDrawerLayout.CrossfadeListener() {
            @Override
            public void onCrossfade(View containerView, float currentSlidePercentage, int slideOffset) {
                for (int i = 0; i < miniResult.getAdapter().getItemCount(); i++) {
                    IDrawerItem drawerItem = miniResult.getAdapter().getItem(i);
                    if (drawerItem instanceof MiniProfileDrawerItem) {
                        MiniProfileDrawerItem mpdi = (MiniProfileDrawerItem) drawerItem;
                        mpdi.withCustomHeightPx((int) (originalProfileHeight + (headerDifference * currentSlidePercentage / 100)));
                    } else if (drawerItem instanceof MiniDrawerItem) {
                        MiniDrawerItem mdi = (MiniDrawerItem) drawerItem;
                        mdi.withCustomHeightPx((int) (originalItemHeight - (itemDifference * currentSlidePercentage / 100)));
                    }
                }
                miniResult.getAdapter().notifyDataSetChanged();
            }
        });

    }





    @Override
    public void onBackPressed() {


        Intent it=new Intent(ctx,Landing.class);
        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(it);
        finish();;

    }






}
