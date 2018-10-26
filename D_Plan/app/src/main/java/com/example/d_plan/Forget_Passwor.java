package com.example.d_plan;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class Forget_Passwor extends AppCompatActivity {

    EditText emailfield;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget__passwor);

        Toolbar toolbar =(Toolbar)findViewById(R.id.my_toolbar);
        toolbar.setTitle("Reset Password");
        setSupportActionBar(toolbar);
        emailfield = (EditText)findViewById(R.id.resetemailid);
        Button back =(Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Login.class);
                startActivity(i);
            }
        });

        submit = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendresetlink();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(),Login.class);
        startActivity(i);
    }

    private void sendresetlink() {
        String email = emailfield.getText().toString().trim();
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailfield.setError("Enter  a valid email id");
            emailfield.requestFocus();
            return;
        }
        FirebaseAuth mauth = FirebaseAuth.getInstance();
        mauth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                          if(task.isSuccessful()){
                              emailfield.setText("");
                              emailfield.setEnabled(false);
                              submit.setEnabled(false);
                              Toast.makeText(getApplicationContext(),"Email is Send Successfully",Toast.LENGTH_SHORT).show();
                              Intent i = new Intent(getApplicationContext(),Login.class);
                              startActivity(i);
                          }
                          else{
                              Toast.makeText(getApplicationContext(),"Try again Later",Toast.LENGTH_SHORT).show();
                          }
                    }
                }
        );
    }

    private void checkAccountEmailExistInFirebase(String email) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                final boolean[] b = new boolean[1];
                mAuth.fetchProvidersForEmail(email).addOnCompleteListener(new OnCompleteListener<com.google.firebase.auth.ProviderQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<com.google.firebase.auth.ProviderQueryResult> task) {
                        b[0] = !task.getResult().getProviders().isEmpty();
                    }
                });

                if(b[0]==false){
                    Toast.makeText(getApplicationContext(),"Email doesnot exists in our database",Toast.LENGTH_SHORT).show();
                }else{
                    sendresetlink();
                }
            }
}
