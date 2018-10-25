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

public class Helper_Intermediate extends AppCompatActivity {

    String dtype,dloc,id;
    TextView tpye,loc;

    Button update,newhelpcenter,helprequest;

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),local_helper.class);
        i.putExtra("role","local");
        startActivity(i);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helper__intermediate);

        Toolbar toolbar =(Toolbar)findViewById(R.id.my_toolbar);
        assert toolbar != null;
        toolbar.setTitle("What You Can Do?");
        toolbar.getOverflowIcon().setColorFilter(ContextCompat.getColor(this, R.color.common_google_signin_btn_text_light_default), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        dtype = getIntent().getStringExtra("dtype");
        dloc = getIntent().getStringExtra("dloc");
        id = getIntent().getStringExtra("id");

        tpye= (TextView)findViewById(R.id.dtype);
        loc = (TextView)findViewById(R.id.dloc);
        tpye.setText(dtype);
        loc.setText(dloc);

        update = (Button)findViewById(R.id.update);
        newhelpcenter = (Button) findViewById(R.id.organise);
        helprequest = (Button)findViewById(R.id.users);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent o = new Intent(getApplicationContext(),mycenter.class);
                o.putExtra("id",id);
                startActivity(o);
            }
        });


        newhelpcenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent o = new Intent(getApplicationContext(),Helper_Form.class);
                o.putExtra("id",id);
                startActivity(o);
            }
        });

        helprequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent o = new Intent(getApplicationContext(),Help_Requests.class);
                o.putExtra("id",id);
                startActivity(o);
            }
        });
    }
}
