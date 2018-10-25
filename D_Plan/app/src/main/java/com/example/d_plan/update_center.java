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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.sync.MobileServiceSyncContext;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.ColumnDataType;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.MobileServiceLocalStoreException;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.SQLiteLocalStore;
import com.microsoft.windowsazure.mobileservices.table.sync.synchandler.SimpleSyncHandler;
import com.squareup.okhttp.OkHttpClient;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.val;

public class update_center extends AppCompatActivity implements LocationListener{

    private MobileServiceClient mClient;
    private MobileServiceTable<Local_help> ltable;
    EditText mob, name,currcap,maxcap,gplace;
    TextView lat,lng;
    Button makecenter;
    String did,gid;
    private FirebaseAuth mAuth;
    private  Local_help workon_item = null;
    private ProgressBar mProgressBar;

    protected LocationManager locationManager;
    protected LocationListener locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_center);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        assert toolbar != null;
        toolbar.setTitle("Update Center");
        toolbar.getOverflowIcon().setColorFilter(ContextCompat.getColor(this, R.color.common_google_signin_btn_text_light_default), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        mProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);
        // Initialize the progress bar
        mProgressBar.setVisibility(ProgressBar.GONE);
        did = getIntent().getStringExtra("did");
        gid = getIntent().getStringExtra("gid");
        try {
            mClient = new MobileServiceClient("https://d-plan.azurewebsites.net", this).withFilter(new update_center.ProgressFilter());

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
            gplace = (EditText)findViewById(R.id.gplace);
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
                    if(lat.getText().toString().length()!=0 && lng.getText().toString().length()!=0) {
                        if(workon_item!=null)
                            update_center(workon_item);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Please wait while we get Your location",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            refreshItemsFromTable();

        } catch (MalformedURLException e) {
            createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        } catch (Exception e){
            createAndShowDialog(e, "Error");
        }
    }

    public void update_center(final Local_help item) {
        if (mClient == null) {
            return;
        }

        // Set the item as completed and update it in the table
        workon_item.updatecurrent(Integer.parseInt(currcap.getText().toString()));
        workon_item.updatelatitude(lat.getText().toString());
        workon_item.updatelongitude(lng.getText().toString());
        workon_item.updatename(name.getText().toString());
        workon_item.updatenumber(mob.getText().toString());
        workon_item.updatemax(Integer.parseInt(maxcap.getText().toString()));
        workon_item.updateplace(gplace.getText().toString());
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    checkItemInTable(workon_item);
                } catch (final Exception e) {
                    createAndShowDialogFromTask(e, "Error");
                }

                return null;
            }
        };
        runAsyncTask(task);
        Toast.makeText(getApplicationContext(),"Fields are updated",Toast.LENGTH_SHORT).show();
        Intent i  = new Intent(getApplicationContext(),local_helper.class);
        i.putExtra("role","local");
        startActivity(i);
    }

    public void checkItemInTable(Local_help item) throws ExecutionException, InterruptedException {
        ltable.update(item).get();
    }


    private void refreshItemsFromTable() {

        // Get the items that weren't marked as completed and add them in the
        // adapter

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    final List<Local_help> results = refreshItemsFromMobileServiceTable();


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (Local_help item : results) {
                                if (item.getText_did().equals(did) && item.isComplete() == false && item.getId().equals(gid)) {
                                    workon_item = item;
                                    name.setText(workon_item.getText_name());
                                    mob.setText(workon_item.getText_mob());
                                    currcap.setText(workon_item.getText_currentcapacity()+"");
                                    maxcap.setText(workon_item.getText_maxcapacity()+"");
                                    gplace.setText(workon_item.getPlace());
                                    break;
                                }
                            }
                        }
                    });


                }catch (final Exception e){
                    createAndShowDialogFromTask(e, "Error");
                }

                return null;
            }
        };
        runAsyncTask(task);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),local_helper.class);
        i.putExtra("role","local");
        startActivity(i);
        super.onBackPressed();
    }

    private List<Local_help> refreshItemsFromMobileServiceTable() throws ExecutionException, InterruptedException {
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null){
            return null;
        }
        String mail = currentUser.getEmail();
        return  ltable.where().field("user_email").eq(val(mail)).execute().get();
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

    private class ProgressFilter implements ServiceFilter {

        @Override
        public ListenableFuture<ServiceFilterResponse> handleRequest(ServiceFilterRequest request, NextServiceFilterCallback nextServiceFilterCallback) {

            final SettableFuture<ServiceFilterResponse> resultFuture = SettableFuture.create();


            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.VISIBLE);
                }
            });

            ListenableFuture<ServiceFilterResponse> future = nextServiceFilterCallback.onNext(request);

            Futures.addCallback(future, new FutureCallback<ServiceFilterResponse>() {
                @Override
                public void onFailure(Throwable e) {
                    resultFuture.setException(e);
                }

                @Override
                public void onSuccess(ServiceFilterResponse response) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.GONE);
                        }
                    });

                    resultFuture.set(response);
                }
            });

            return resultFuture;
        }
    }
}

