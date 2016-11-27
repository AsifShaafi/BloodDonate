package com.example.shaafi.blooddonate;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

public class SearchPage extends AppCompatActivity {

    public static String jsonString;
    public static String bloodType, location;
    AutoCompleteTextView etLocation;
    Spinner bloodSpinner;
    private ConnectivityManager connectivityManager;
    private int NETWORK_STATUS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setIcon(R.mipmap.ic_launcher);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        setLayouts();

    }

    private void setLayouts(){

        String[] bloods = getResources().getStringArray(R.array.bloodGrpArray);

        ArrayAdapter<String> bloodGrpArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bloods);

        bloodSpinner = (Spinner) findViewById(R.id.bloodGrp_spinner);
        bloodGrpArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodSpinner.setAdapter(bloodGrpArrayAdapter);

        bloodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bloodType = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //edit text location
        etLocation = (AutoCompleteTextView) findViewById(R.id.editTextSLocation);
        String[] locations = getResources().getStringArray(R.array.locaionArray);

        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, locations);
        etLocation.setAdapter(locationAdapter);
        etLocation.setThreshold(1);
        etLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etLocation.setText(null);
                etLocation.setError(null);
            }
        });
    }

    public void checkNetwork() {
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                NETWORK_STATUS = 1;
                //Toast.makeText(this, "wifi connected", Toast.LENGTH_SHORT).show();
            }

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                NETWORK_STATUS = 2;
                //Toast.makeText(this, "mobile data connected", Toast.LENGTH_SHORT).show();
            }
        }
        else
        NETWORK_STATUS = 0;
    }

    public void SearchBlood(View view) {

        location = etLocation.getText().toString();

        checkNetwork();

        if(!location.isEmpty()) {

            //etLocation.setError(null);
            if(NETWORK_STATUS == 1 || NETWORK_STATUS == 2) {

                String method = "search";

                //searching and getting the data from the web as json
                BackgroundTaskSearch backgroundTaskSearch = new BackgroundTaskSearch(this);
                backgroundTaskSearch.execute(method, bloodType, location);

//                OkHttpClient client = new OkHttpClient();
//                RequestBody formBody = new FormBody.Builder()
//                        .add("bloodType", bloodType)
//                        .add("location", location)
//                        .build();
//
//                Request request = new Request.Builder()
//                        .url("http://www.androidtesting.tk/connect_blood_donation_fetch_data_2.php")
//                        .post(formBody)
//                        .build();
//
//                Call call = client.newCall(request);
//
//                call.enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        final String json = response.body().string();
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(SearchPage.this, "json:" + json, Toast.LENGTH_SHORT).show();
//                                SearchPage.jsonString = json;
//                            }
//                        });
//                    }
//                });

            }
            else{
                Toast.makeText(this, "Please connect to internet first", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            etLocation.setError("Please Enter Area/Location");
        }

    }

    public void showSearchResult(View view) {
        //sending the data to a list view
        if(jsonString == null){
            Toast.makeText(this, "Please Click Search First", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(this, SearchResultList.class);
            intent.putExtra("jsonData", jsonString);
            startActivity(intent);
            jsonString = null;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        else if(item.getItemId() == R.id.about){
            startActivity(new Intent(getBaseContext(), AboutUs.class));
        }
        else if(item.getItemId() == R.id.user_info){
            startActivity(new Intent(this, UserPhoneNumerInput.class));
        }

        return true;
    }
}
