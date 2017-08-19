package in.hoptec.exploman;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.BitmapRequestListener;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mikepenz.crossfadedrawerlayout.view.CrossfadeDrawerLayout;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.MiniDrawer;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.interfaces.ICrossfader;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.MiniDrawerItem;
import com.mikepenz.materialdrawer.model.MiniProfileDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialize.util.UIUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;

import in.hoptec.exploman.adapters.PopUpAdapter;
import in.hoptec.exploman.database.Place;
import in.hoptec.exploman.utils.GenricCallback;

public class Landing extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Toolbar toolbar;
    GenricUser user;

    public  Context ctx;
    public  Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx=this;
        act=this;



        setContentView(R.layout.activity_home);
        toolbar=(Toolbar)findViewById(R.id.toolbar);

        user=utl.readUserData();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        city = (TextView)  findViewById(R.id.toolbar_title);
        city.setText("Explo Oman");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        setUpLocation();
        setUpDrawer(savedInstanceState);




    }

    /************************   APIS  ****************************/

    public void showMarkers(ArrayList<Place> places)
    {

        mMap.clear();

        if(places.size()<1)
        {
            utl.hideSoftKeyboard(act);
            utl.snack(act,"No Results !");
            return;
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

//the include method will calculate the min and max bound.


        for (Place place:places
             ) {


            mark(place.lat,place.lng,place.name,false);
            builder.include(getMarker(place.lat,place.lng,place.name).getPosition());



        }

        LatLngBounds bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

        mMap.animateCamera(cu);




    }
    ArrayList<Place> places;
    public void getNearby()
    {


        //TODO change range
        String url=Constants.HOST+Constants.API_GET_PLACES+"?mode=coor&range=100000&lat="+loc.getLatitude()+"lng="+loc.getLongitude();
        utl.l(url);

        AndroidNetworking.get(url).build().getAsJSONArray(new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray response) {

                places=new ArrayList<Place>();
              for(int i=0;i<response.length();i++){

                  try {
                      places.add(utl.js.fromJson(response.get(i).toString(),Place.class));
                  } catch (JSONException e) {
                      e.printStackTrace();
                  }



              }showMarkers(places);




            }

            @Override
            public void onError(ANError ANError) {
                utl.l("getNearby 164"+ANError.getErrorDetail());
            }
        });



    }

    public void search(String query)
    {

        String url=Constants.HOST+Constants.API_GET_PLACES+"?mode=search&query="+ URLEncoder.encode(query);
        utl.l(url);

        AndroidNetworking.get(url).build().getAsJSONArray(new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray response) {

                places=new ArrayList<Place>();
                for(int i=0;i<response.length();i++){

                    try {
                        places.add(utl.js.fromJson(response.get(i).toString(),Place.class));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                showMarkers(places);




            }

            @Override
            public void onError(ANError ANError) {
                utl.l("getNearby 164"+ANError.getErrorDetail());
            }
        });


    }

    /**********************   MAP   ********************************/
    public void mark(double lat,double lng,String title,boolean animate)
    {

        LatLng latLng = new LatLng(lat, lng);

        MarkerOptions mk= new MarkerOptions().position(latLng).title(""+title);
         mk.icon(BitmapDescriptorFactory.fromResource(R.drawable.ub__pin_pickup));
        mMap.addMarker(mk);
       if(animate)
        goToMarker(mk);



    }
    public CameraPosition getCam(double lat,double lng)
    {

        CameraPosition position =
                new CameraPosition.Builder().target(new LatLng(lat, lng))
                        .zoom(8.5f)
                        .bearing(0)
                        .tilt(25)
                        .build();


    return position;
    }

    public MarkerOptions getMarker(double lat,double lng,String title)
    {
        LatLng latLng = new LatLng(lat, lng);

        MarkerOptions mk= new MarkerOptions().position(latLng).title(""+title);

        return mk;
    }
    public void goToMarker(MarkerOptions mrk) {
        changeCamera(CameraUpdateFactory.newCameraPosition(getCam(mrk.getPosition().latitude,mrk.getPosition().longitude)), new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
               // Toast.makeText(getBaseContext(), "Animation to Sydney complete", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
               // Toast.makeText(getBaseContext(), "Animation to Sydney canceled", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * Change the camera position by moving or animating the camera depending on the state of the
     * animate toggle button.
     */
    private void changeCamera(CameraUpdate update, GoogleMap.CancelableCallback callback) {
             int duration = 1000;
                // The duration must be strictly positive so we make it at least 1.
                mMap.animateCamera(update, Math.max(duration, 1), callback);

               // mMap.animateCamera(update, callback);

    }
boolean ald=false;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {

            marker.showInfoWindow();

            utl.snack(findViewById(R.id.activity_home), "" + marker.getTitle(), "VIEW", new GenricCallback() {
                @Override
                public void onStart() {


                    utl.snack(act,"UNDER CONSTRUCTION !");
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

            return false;
        }
    });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {


                utl.snack(findViewById(R.id.activity_home), "" + marker.getTitle(), "VIEW", new GenricCallback() {
                    @Override
                    public void onStart() {


                        utl.snack(act,"UNDER CONSTRUCTION !");
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

        // Add a marker in Sydney and move the camera
         mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(final Marker marker) {

                // Getting view from the layout file info_window_layout
                View v = getLayoutInflater().inflate(R.layout.mp_info, null);



                // Getting the position from the marker
                LatLng latLng = marker.getPosition();

                // Getting reference to the TextView to set latitude
                final ImageView im=(ImageView)v.findViewById(R.id.image);
                TextView tvLat = (TextView) v.findViewById(R.id.name);


                for (final Place place:places
                        ) {


                    if(marker.getPosition().latitude==place.lat&&marker.getPosition().longitude==place.lng)
                    {
                        try {


                              Picasso.with(ctx).load(place.getImages().get(0)).into(im, new Callback() {
                                  @Override
                                  public void onSuccess() {
                                      utl.l("picsaseo loded");
                                      if (marker != null && marker.isInfoWindowShown()) {
                                          marker.hideInfoWindow();
                                          marker.showInfoWindow();
                                      }
                                  }

                                  @Override
                                  public void onError() {
                                      utl.l("picsaseo err");

                                  }
                              });

                            place.marked=true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        tvLat.setText(place.name);
                        break;


                    }



                }


                    return v;

            }

        });

    }


    /*********************** LOCATION ************************/
    FusedLocationProviderClient mFusedLocationClient;
    public void setUpLocation()
    {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        try {
            //noinspection MissingPermission
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {

                                utl.l("Location : "+location);
                                utl.writeFile("loc",utl.js.toJson(location));
                                getCity();

                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        String  locj=utl.readFile("loc");
        if(locj!=null) {

            loc=utl.js.fromJson(locj,Location.class);
            city.setText("");
            getCity();
        }

    }


    String json="";
    Location loc;
    TextView city;
    public void getCity ()   {

        Location loc=null;
        String  locj=utl.readFile("loc");
        if(locj!=null) {

            loc=utl.js.fromJson(locj,Location.class);
            city.setText(loc.getLatitude()+","+loc.getLongitude());
        }

        String url="http://maps.googleapis.com/maps/api/geocode/json?latlng="+(loc!=null?loc.getLatitude()+","+loc.getLongitude():"28.592,76.990")+"&sensor=true";
        utl.l(url);
        AndroidNetworking.get(url).build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObj) {

                try {
                    String Status = jsonObj.getString("status");
                    if (Status.equalsIgnoreCase("OK")) {
                        JSONArray Results = jsonObj.getJSONArray("results");
                        JSONObject zero = Results.getJSONObject(0);
                        JSONArray address_components = zero
                                .getJSONArray("address_components");

                        for (int i = 0; i < address_components.length(); i++) {
                            JSONObject zero2 = address_components
                                    .getJSONObject(i);
                            String long_name = zero2.getString("long_name");
                            JSONArray mtypes = zero2.getJSONArray("types");
                            String Type = mtypes.getString(0);
                            if (Type.equalsIgnoreCase("administrative_area_level_1")) {
                                // Address2 = Address2 + long_name + ", ";
                                String City = long_name;
                                Log.d(" CityName --->", City + "");
                                city.setText(City);

                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError ANError) {

            }
        });

    }

 /************************ DRAWER **************************/

 @Override
 protected void onSaveInstanceState(Bundle outState) {
     //add the values which need to be saved from the drawer to the bundle
     outState = result.saveInstanceState(outState);
     //add the values which need to be saved from the accountHeader to the bundle
     outState = headerResult.saveInstanceState(outState);
     super.onSaveInstanceState(outState);
 }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
 @Override
 public boolean onCreateOptionsMenu(Menu menu) {
     MenuInflater inflater = getMenuInflater();
     inflater.inflate(R.menu.menu_search, menu);
     SearchView searchView =
             (SearchView) menu.findItem(R.id.menu_search).getActionView();
     searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
         @Override
         public boolean onQueryTextSubmit(String query) {

            // utl.snack(act,""+query);

             search(query);

             return false;
         }

         @Override
         public boolean onQueryTextChange(String newText) {
             return false;
         }
     });




     return true;
 }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId()==R.id.location)
        {
            if(loc!=null)
            {
                mMap.clear();
                mark( loc.getLatitude(),loc.getLongitude(),"Me",true);
                getNearby();
            }
            else{
                utl.snack(act,"Location Unavilable !");
            }

        }


        return super.onOptionsItemSelected(item);




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
}
