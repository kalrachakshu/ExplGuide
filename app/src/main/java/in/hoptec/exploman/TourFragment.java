package in.hoptec.exploman;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import in.hoptec.exploman.database.TPackage;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TourFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TourFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TourFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TourFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TourFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TourFragment newInstance(TPackage param1, String param2) {
        TourFragment fragment = new TourFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, utl.js.toJson(param1));
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Context ctx;
    public Activity act;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    public View view;

    public View up,dow;

    public void pd(boolean sho)
    {
        if(sho)
        {
            TourPackages.load(TourPackages.LOADING);
        }
        else
        {
            TourPackages.load(TourPackages.LOADED);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ctx=getContext();
        act=getActivity();
        view= inflater.inflate(R.layout.fragment_tour, container, false);

        final TextView zzz= (TextView)view.findViewById(R.id.go);
        final ImageView img=(ImageView)view.findViewById(R.id.img);


        AndroidNetworking.initialize(ctx);
        dow=view.findViewById(R.id.dow);
        up=view.findViewById(R.id.up);
        up.setVisibility(View.GONE);
        WindowManager windowManager = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        int height = windowManager.getDefaultDisplay().getHeight();

        final TPackage cat=getItem();
        //  cat.name="AdverT";
        img.setVisibility(View.VISIBLE);


            try {
                pd(true);
                utl.l("Package Img : "+cat.image);
                Picasso.with(ctx).load(cat.image).error(R.drawable.bg_black).into(img, new Callback() {
                    @Override
                    public void onSuccess() {
                        pd(false);

                    }

                    @Override
                    public void onError() {
                        pd(false);

                    }
                });
               /* pd(true);
                AndroidNetworking.get(cat.imageUrl).build().getAsBitmap(new BitmapRequestListener() {
                    @Override
                    public void onResponse(Bitmap response) {

                        Glide.with(ctx).load(response).into(img);


                    }

                    @Override
                    public void onError(ANError ANError) {
                        Picasso.with(getContext()).load(R.drawable.back_spl).into(img);

                    }
                });*/
                // Picasso.with(getContext()).load(cat.imageUrl).error(R.drawable.back_spl).placeholder(R.drawable.back_spl).into(img);
            } catch (Exception e) {
                Picasso.with(getContext()).load(R.drawable.bg_black).into(img);
                utl.l("NO IMG URL : " + cat.image);
            }




        zzz.setText(cat.name);
        zzz.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                //  ((TextView)zzz).setTextColor(getContext().getResources().getColor(R.color.black));

                int colorFrom = getResources().getColor(R.color.transblack);
                int colorTo = getResources().getColor(R.color.red);


                if(motionEvent.equals(MotionEvent.ACTION_BUTTON_RELEASE))
                {
                    //colorFrom = getResources().getColor(R.color.red);
                    //colorTo = getResources().getColor(R.color.white);

                }

                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                colorAnimation.setDuration(800); // milliseconds
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        zzz.setBackgroundColor((int) animator.getAnimatedValue());
                    }

                });

                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        colorAnimation.start();

                        break;

                    default:
                        break;
                }





                return false;
            }
        });


        zzz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //TransitionDrawable transition = (TransitionDrawable) getContext().getResources().getDrawable(R.drawable.btn_sel_tns_transit);
                //transition.startTransition(1000);
                //  utl.toast(getContext(),"Action !");

            }
        });








        return view;
    }


    public TPackage getItem()
    {
        TPackage item=new TPackage();
        item=(new Gson()).fromJson(getArguments().getString("param1"),TPackage.class);
        return item;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
