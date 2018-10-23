package com.example.d_plan;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class User_Intermediate extends AppCompatActivity {

    String dtype,dloc,id;
    TextView tpye,loc;

    Button Share,helpcenters;

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),User_awareness.class);
        i.putExtra("role","");
        startActivity(i);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__intermediate);
        Toolbar toolbar =(Toolbar)findViewById(R.id.my_toolbar);
        assert toolbar != null;
        toolbar.setTitle("User Choice");
        toolbar.getOverflowIcon().setColorFilter(ContextCompat.getColor(this, R.color.common_google_signin_btn_text_light_default), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        dtype = getIntent().getStringExtra("dtype");
        dloc = getIntent().getStringExtra("dloc");
        id = getIntent().getStringExtra("id");

        tpye= (TextView)findViewById(R.id.dtype);
        loc = (TextView)findViewById(R.id.dloc);
        tpye.setText(dtype);
        loc.setText(dloc);

        Share = (Button)findViewById(R.id.share);
        helpcenters = (Button)findViewById(R.id.nearby);

        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent o = new Intent(getApplicationContext(),Share_Activity.class);
                o.putExtra("id",id);
                startActivity(o);
            }
        });
        helpcenters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
            }
        });
    }
}
