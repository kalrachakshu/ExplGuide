package in.hoptec.exploman.adapters;

/**
 * Created by shivesh on 19/8/17.
 */



import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import in.hoptec.exploman.R;
public class PopUpAdapter implements InfoWindowAdapter{
    LayoutInflater mInflater;
    Map<Marker, String> imageStringMapMarker;
    Context context;

    public PopUpAdapter(LayoutInflater i,  Map<Marker, String> imageStringMapMarker2, Context context ){
        mInflater = i;
        imageStringMapMarker = imageStringMapMarker2;
    }

    @Override
    public View getInfoContents(final Marker marker) {

        View v = mInflater.inflate(R.layout.mp_info, null);

        ImageView ivThumbnail = (ImageView) v.findViewById(R.id.image);
        String urlImage = imageStringMapMarker.get(marker).toString();
        Picasso.with(context).load(Uri.parse(urlImage)).resize(250,250).into(ivThumbnail);

        return v;

    }

    @Override
    public View getInfoWindow(Marker marker) {
        // TODO Auto-generated method stub
        return null;
    }
}