package in.hoptec.exploman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViewById(R.id.activity_home).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                utl.logout();
                onBackPressed();


                return false;
            }
        });

        utl.diagA(this,"Under Construction ","Welcome ! "+utl.getUser().getDisplayName()
                +" \nThis is demo version of ExplOman ! \nLong Press Map  to logout");

    }
}
