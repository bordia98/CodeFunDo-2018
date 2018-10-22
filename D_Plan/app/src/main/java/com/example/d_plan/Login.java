package com.example.d_plan;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            Intent i = new Intent(getApplicationContext(),ToDoActivity.class);
            startActivity(i);
        }
    }

    EditText email,password;
    ProgressBar pgbar;
    private FirebaseAuth mAuth;
    RadioButton admin,local;
    String selector= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Toolbar signin = (Toolbar)findViewById(R.id.my_signintoolbar);
        setSupportActionBar(signin);

        mAuth = FirebaseAuth.getInstance();
        TextView newuser = (TextView) findViewById(R.id.newuser);
        newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Sign_Up.class);
                startActivity(i);
            }
        });
        pgbar=(ProgressBar)findViewById(R.id.pgbar);
        pgbar.setVisibility(View.GONE);
        email=(EditText)findViewById(R.id.emailfield);
        password=(EditText)findViewById(R.id.passwordfield);

        admin = (RadioButton)findViewById(R.id.admin);
        local = (RadioButton)findViewById(R.id.local);

        TextView forgetpassword = (TextView)findViewById(R.id.forgetpassword);
        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(getApplicationContext(),Passwordreset.class);
//                startActivity(i);
            }
        });

        Button skipsignin = (Button) findViewById(R.id.skip);
        skipsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selector="";
                Intent i = new Intent(getApplicationContext(),ToDoActivity.class);
                i.putExtra("role",selector);
                startActivity(i);
            }
        });
        Button login = (Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logintheuser();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        System.exit(0);
    }

    private void logintheuser() {

        String emailid = email.getText().toString().trim();
        String passwordid = password.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(emailid).matches()) {
            email.setError("Enter  a valid email id");
            email.requestFocus();
            return;
        }

        if(admin.isChecked()){
            selector="admin";
        }


        if(local.isChecked()){
            selector="local";
        }

        if(selector.length()==0){
            admin.setError("Select a Role");
            return;
        }


        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            //instead of my id here will come the id of the admin governing
            if (selector.equals("admin")){
                if(emailid.equals("bhavyabordia@gmail.com")){
                    mAuth.signInWithEmailAndPassword(emailid, passwordid)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent i = new Intent(getApplicationContext(), Disaster_adding.class);
                                        i.putExtra("role",selector);
                                        startActivity(i);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Please check your credentials", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(getApplicationContext(),"You dont have admin rights",Toast.LENGTH_SHORT).show();
                }
            }
            else{
                if(emailid.equals("bhavyabordia@gmail.com")){
                    Toast.makeText(getApplicationContext(),"You don't have to access local Page",Toast.LENGTH_SHORT).show();
                }
                else{
                    mAuth.signInWithEmailAndPassword(emailid, passwordid)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent i = new Intent(getApplicationContext(), ToDoActivity.class);
                                        i.putExtra("role",selector);
                                        startActivity(i);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Please check your credentials", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        } else {
            Toast.makeText(getApplicationContext(),"NO Internet Access",Toast.LENGTH_SHORT).show();
        }
    }
}
