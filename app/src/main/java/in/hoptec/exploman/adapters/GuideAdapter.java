package in.hoptec.exploman.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.squareup.picasso.Picasso;

import java.util.List;

import in.hoptec.exploman.R;
import in.hoptec.exploman.database.Guide;
import in.hoptec.exploman.utl;


public class GuideAdapter extends RecyclerView.Adapter<GuideAdapter.CustomViewHolder> {
    public List<Guide> feedItemList;
    private Context ctx;

    public GuideAdapter(Context context, List<Guide> feedItemList, CallBacks cab) {
        this.feedItemList = feedItemList;
        this.ctx = context;
        this.cab=cab;
        AndroidNetworking.initialize(ctx);
       }

    public Integer colors[] ={R.color.green,R.color.yellow,R.color.blue,R.color.red,R.color.orange};
    public static int width,height;
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {


        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_guide,  null, false);
      //  view.setLayoutParams(new RecyclerView.LayoutParams(width, RecyclerView.LayoutParams.WRAP_CONTENT));

        WindowManager windowManager = (WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        height = windowManager.getDefaultDisplay().getHeight();
        width = windowManager.getDefaultDisplay().getWidth();


        Double w,h;
        w=width/1.0;
        w=w-w*0.04;
        h=height/2.4;
        RelativeLayout.LayoutParams par=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                utl.pxFromDp(ctx,150f).intValue());
        if(utl.dpFromPx(ctx,h.floatValue())<150);
          ;//    par=new RelativeLayout.LayoutParams(w.intValue(), utl.pxFromDp(ctx,200F).intValue());



        view.setLayoutParams(par);

//        YoYo.with(Techniques.SlideInLeft).duration(500).playOn( view);
        return viewHolder;
    }

    public static class Qant{
        public int quan=1;
    }


    int count=0;
    @Override
    public void onBindViewHolder(final CustomViewHolder cv, final int i) {
               //Setting text view title
      final Guide cat=feedItemList.get(i);
        final int id=i;
        final Qant qn=new Qant();


        if(count>=4)
            count=0;

        Double dp=new Double(i);
        dp=dp%4;

        utl.changeColorDrawable(cv.car_i,R.color.light_blue_700);
        utl.changeColorDrawable(cv.rate_i,R.color.light_blue_700);
        utl.changeColorDrawable(cv.save_i,R.color.pink_500);

        cv.car.setTextColor(ctx.getResources().getColor(R.color.light_blue_700));
        cv.rate.setTextColor(ctx.getResources().getColor(R.color.light_blue_700));
        cv.save.setTextColor(ctx.getResources().getColor(R.color.pink_500));




        final int col=colors[dp.intValue()];

        cv.name.setText( (cat.name));
        cv.desc.setText( (cat.address));
        cv.rating.setRating(Float.parseFloat(cat.rating));

        cv.rate.setText(cat.rate+"﷼ ");

        //cv.time.setText(cat.getCreatedAt());

        try {
            utl.l(cat.getImages().get(0));

            Picasso.with(ctx).load(cat.getImages().get(0)).placeholder(R.drawable.user).into(cv.guideImage);

//          cv.line.setBackgroundColor(ctx.getResources().getColor(col));
            count++;


           // utl.changeColorDrawable(cv.opt,R.color.grey_600);


        } catch (Exception e) {


            e.printStackTrace();
        }

        cv.save_h.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                cab.save(cat,  id);
                return false;
            }
        });

        cv.car_h.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                cab.car(cat,  true);
                return false;
            }
        });

        cv.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cab.click(cat,id,cv.view);
            }
        });
       // cv.line.setVisibility(View.INVISIBLE);


    }

    int qan;
    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    CallBacks cab;
    public static interface CallBacks{

    public void save(Guide cat, int id);

    public void car(Guide cat, boolean like);

    public void click(Guide cat, int id, View v);
    }

public class CustomViewHolder extends RecyclerView.ViewHolder
{
    public View view;

    public ImageView guideImage,like,play;
    public LinearLayout car_h,rate_h,save_h;
    public ImageView car_i,rate_i,save_i;
    public TextView car ,rate ,save ;
    public TextView name, desc,time;
    public WebView wb;
    public View line;
    public AppCompatRatingBar rating;


    public CustomViewHolder(View itemView) {
        super(itemView);
        view=itemView.findViewById(R.id.textc);

       // wb=(WebView) itemView.findViewById(R.id.web);
        guideImage =(ImageView) itemView.findViewById(R.id.ivUserAvatar);
        name =(TextView) itemView.findViewById(R.id.tvName);
        desc =(TextView) itemView.findViewById(R.id.tvComment);
        rating=(AppCompatRatingBar) itemView.findViewById(R.id.rating);

        car_h=(LinearLayout)itemView.findViewById(R.id.car_h);
        rate_h=(LinearLayout)itemView.findViewById(R.id.rate_h);
        save_h=(LinearLayout)itemView.findViewById(R.id.save_h);

        car_i=(ImageView)itemView.findViewById(R.id.car_i);
        rate_i=(ImageView)itemView.findViewById(R.id.rate_i);
        save_i=(ImageView)itemView.findViewById(R.id.save_i);

        car=(TextView)itemView.findViewById(R.id.car);
        rate=(TextView)itemView.findViewById(R.id.rate);
        save=(TextView)itemView.findViewById(R.id.save);
    }
}


public class Dummy
{

}






}
