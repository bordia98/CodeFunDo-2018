package com.example.d_plan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.sync.MobileServiceSyncContext;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.ColumnDataType;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.MobileServiceLocalStoreException;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.SQLiteLocalStore;
import com.microsoft.windowsazure.mobileservices.table.sync.synchandler.SimpleSyncHandler;
import com.squareup.okhttp.OkHttpClient;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Helper_Form extends AppCompatActivity implements LocationListener {

    private MobileServiceClient mClient;
    private MobileServiceTable<Local_help> ltable;
    EditText mob, name,currcap,maxcap;
    TextView lat,lng;
    Button makecenter;
    String did;
    private FirebaseAuth mAuth;

    protected LocationManager locationManager;
    protected LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper__form);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        assert toolbar != null;
        toolbar.setTitle("Make Center");
        toolbar.getOverflowIcon().setColorFilter(ContextCompat.getColor(this, R.color.common_google_signin_btn_text_light_default), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);

        did = getIntent().getStringExtra("id");
        try {
            mClient = new MobileServiceClient("https://d-plan.azurewebsites.net", this);

            // Extend timeout from default of 10s to 20s
            mClient.setAndroidHttpClientFactory(new OkHttpClientFactory() {
                @Override
                public OkHttpClient createOkHttpClient() {
                    OkHttpClient client = new OkHttpClient();
                    client.setReadTimeout(20, TimeUnit.SECONDS);
                    client.setWriteTimeout(20, TimeUnit.SECONDS);
                    return client;
                }
            });

            ltable = mClient.getTable(Local_help.class);
            initLocalStore().get();
            lat = (TextView) findViewById(R.id.latitude);
            lng = (TextView) findViewById(R.id.longitude);
            name = (EditText) findViewById(R.id.name);
            mob = (EditText) findViewById(R.id.number);
            maxcap = (EditText)findViewById(R.id.max_capacity);
            currcap = (EditText)findViewById(R.id.current_count);

            makecenter = (Button) findViewById(R.id.makecenter);

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(),"No permissions given",Toast.LENGTH_SHORT).show();
                // Can ask for permission here is permission were not given
                return;
            }
            else {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            }

            makecenter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(lat.getText().toString().length()!=0 && lng.getText().toString().length()!=0)
                        add_center(v);
                    else{
                        Toast.makeText(getApplicationContext(),"Please wait while we get Your location",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (MalformedURLException e) {
            createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        } catch (Exception e){
            createAndShowDialog(e, "Error");
        }
    }

    private void add_center(View v) {
        if (mClient == null) {
            return;
        }
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null){
            return;
        }

        String mail = currentUser.getEmail();
        // Create a new item
        final Local_help item = new Local_help();
        item.setText(
                name.getText().toString(),
                mob.getText().toString(),
                lat.getText().toString(),
                lng.getText().toString(),
                did,
                Integer.parseInt(maxcap.getText().toString()),
                Integer.parseInt(currcap.getText().toString()),
                mail
        );

        item.setComplete(false);

        // Insert the new item
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final Local_help entity = addInTable(item);

                } catch (final Exception e) {
                    createAndShowDialogFromTask(e, "Error");
                }
                return null;
            }
        };
        runAsyncTask(task);
        name.setText("");
        mob.setText("");
        lat.setText("");
        lng.setText("");
        currcap.setText("");
        maxcap.setText("");
        Toast.makeText(getApplicationContext(),"Your Request is made .",Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(),local_helper.class);
        i.putExtra("role","local");
        startActivity(i);
    }

    public Local_help addInTable(Local_help item) throws ExecutionException, InterruptedException {
        Local_help entity = ltable.insert(item).get();
        return entity;
    }

    private AsyncTask<Void, Void, Void> initLocalStore() throws MobileServiceLocalStoreException, ExecutionException, InterruptedException {

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    MobileServiceSyncContext syncContext = mClient.getSyncContext();

                    if (syncContext.isInitialized())
                        return null;

                    SQLiteLocalStore localStore = new SQLiteLocalStore(mClient.getContext(), "OfflineStore", null, 1);

                    Map<String, ColumnDataType> tableDefinition = new HashMap<String, ColumnDataType>();
                    tableDefinition.put("id", ColumnDataType.String);
                    tableDefinition.put("name", ColumnDataType.String);
                    tableDefinition.put("mobile",ColumnDataType.String);
                    tableDefinition.put("latitute",ColumnDataType.String);
                    tableDefinition.put("longitude",ColumnDataType.String);
                    tableDefinition.put("max_cap",ColumnDataType.Integer);
                    tableDefinition.put("curr_cap",ColumnDataType.Integer);
                    tableDefinition.put("disaster_id",ColumnDataType.String);
                    tableDefinition.put("complete", ColumnDataType.Boolean);

                    localStore.defineTable("Local_help", tableDefinition);

                    SimpleSyncHandler handler = new SimpleSyncHandler();

                    syncContext.initialize(localStore, handler).get();

                } catch (final Exception e) {
                    createAndShowDialogFromTask(e, "Error");
                }

                return null;
            }
        };

        return runAsyncTask(task);
    }

    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }

    private void createAndShowDialogFromTask(final Exception exception, String title) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createAndShowDialog(exception, "Error");
            }
        });
    }

    private void createAndShowDialog(Exception exception, String title) {
        Throwable ex = exception;
        if(exception.getCause() != null){
            ex = exception.getCause();
        }
        createAndShowDialog(ex.getMessage(), title);
    }

    private void createAndShowDialog(final String message, final String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
    }

    @Override
    public void onLocationChanged(Location location) {
        lat.setText(location.getLatitude()+"");
        lng.setText(location.getLongitude()+"");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
