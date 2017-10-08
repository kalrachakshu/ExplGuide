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
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.squareup.picasso.Picasso;

import java.util.List;

import in.hoptec.exploman.R;
import in.hoptec.exploman.database.Booking;
import in.hoptec.exploman.utl;


public class TransAdapter extends RecyclerView.Adapter<TransAdapter.CustomViewHolder> {
    public List<Booking> feedItemList;
    private Context ctx;

    public TransAdapter(Context context, List<Booking> feedItemList, CallBacks cab) {
        this.feedItemList = feedItemList;
        this.ctx = context;
        this.cab=cab;
        AndroidNetworking.initialize(ctx);
       }

    public Integer colors[] ={R.color.green,R.color.yellow,R.color.blue,R.color.red,R.color.orange};
    public static int width,height;
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {


        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_trans,  null, false);
      //  view.setLayoutParams(new RecyclerView.LayoutParams(width, RecyclerView.LayoutParams.WRAP_CONTENT));

        WindowManager windowManager = (WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        height = windowManager.getDefaultDisplay().getHeight();
        width = windowManager.getDefaultDisplay().getWidth();


        Double w,h;
        w=width/1.0;
        w=w-w*0.04;
        h=height/2.4;
        CardView.LayoutParams par=new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        if(utl.dpFromPx(ctx,h.floatValue())<200);
      //  par=new LinearLayout.LayoutParams(w.intValue(), utl.pxFromDp(ctx,170F).intValue());


        //par.bottomMargin=4;
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
      final Booking cat=feedItemList.get(i);
        final int id=i;
        final Qant qn=new Qant();


        if(count>=4)
            count=0;

        Double dp=new Double(i);
        dp=dp%4;

        final int col=colors[dp.intValue()];

        cv.title.setText( ("Transaction ID : 00000"+cat.id));
        cv.sub.setText( "Date: "+cat.startTime+"\nStatus: "+cat.status+"\nGuide: "+cat.guide.name);
        cv.rate.setText(("Rs. "+cat.amountTotal));
        //cv.time.setText(cat.getCreatedAt());


        cv.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                cab.like(cat,  true);
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

    public void share(Booking cat, int id);

    public void like(Booking cat, boolean like);

    public void click(Booking cat, int id, View v);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder
    {
        public View view;

        public TextView title, sub,time;
        public WebView wb;
        public View line;
        public TextView rate;


        public CustomViewHolder(View itemView) {
            super(itemView);
            view=itemView.findViewById(R.id.container);

            title =(TextView) itemView.findViewById(R.id.name);
            sub =(TextView) itemView.findViewById(R.id.add);
            rate =(TextView) itemView.findViewById(R.id.pic);


        }
    }



    public class Dummy
{

}






}
