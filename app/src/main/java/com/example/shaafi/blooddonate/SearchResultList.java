package com.example.shaafi.blooddonate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchResultList extends AppCompatActivity {

    String jsonString;
    JSONObject jsonObject;
    JSONArray jsonArray;


    ListAdapter listAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_list);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setIcon(R.mipmap.ic_launcher);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        listAdapter = new ListAdapter(this, R.layout.row_display);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.phone_numbers));

        listView = (ListView) findViewById(R.id.SearchlistView);
        listView.setAdapter(listAdapter);

        jsonString = getIntent().getStringExtra("jsonData");
        //Toast.makeText(this, jsonString, Toast.LENGTH_LONG).show();

        try {
            jsonObject = new JSONObject(jsonString);

            jsonArray = jsonObject.getJSONArray("members");

            String uFirstName, uLastName, uAge, uBlood, uPhone, uLocation, uActive;

            int count = 0;

            while (count < jsonArray.length()){


                JSONObject JO = jsonArray.getJSONObject(count++);

                //Toast.makeText(this,"hi ", Toast.LENGTH_SHORT).show();

                uFirstName = JO.getString("firstname");
                uLastName = JO.getString("lastname");
                uAge = JO.getString("age");
                uBlood = JO.getString("blood_type");
                uPhone = JO.getString("mobile_no");
                uLocation = JO.getString("location");
                uActive = JO.getString("active");
//                uLocation = "location";
//                uActive = "active";

                //Toast.makeText(this,"hi ", Toast.LENGTH_SHORT).show();
                MemberInfo memberInfo = new MemberInfo(uFirstName, uLastName, uPhone, uAge, uBlood, uLocation, uActive);
//                Toast.makeText(this, uFirstName + " " +
//                        uLastName + " " +
//                        uPhone + " " +
//                        uAge + " " +
//                        uBlood + " " +
//                        uLocation + " " +
//                        uActive, Toast.LENGTH_SHORT).show();

                listAdapter.add(memberInfo);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        if(item.getItemId() == R.id.about){
            startActivity(new Intent(getBaseContext(), AboutUs.class));
        }

        return true;
    }
}
