package com.example.shaafi.blooddonate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserInfo extends AppCompatActivity {

    String jsonString;
    JSONObject jsonObject;
    JSONArray jsonArray;
    MemberInfo memberInfo;

    private TextView first, last, blood, age, location;
    private Switch active;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setIcon(R.mipmap.ic_launcher);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        first = (TextView) findViewById(R.id.tv_first_display_userInfo);
        last = (TextView) findViewById(R.id.tv_last_display_userInfo);
        age = (TextView) findViewById(R.id.tv_age_display_userInfo);
        blood = (TextView) findViewById(R.id.tv_blood_display_userInfo);
        location = (TextView) findViewById(R.id.tv_location_display_userInfo);
        active = (Switch) findViewById(R.id.activity_switch_userInfo);

        parseUserData();
    }

    private void parseUserData(){

        //Parsing user data
        jsonString = getIntent().getStringExtra("userData");
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

                //Toast.makeText(this,"hi ", Toast.LENGTH_SHORT).show();
                memberInfo = new MemberInfo(uFirstName, uLastName, uPhone, uAge, uBlood, uLocation, uActive);
//                Toast.makeText(this, uFirstName + " " +
//                        uLastName + " " +
//                        uPhone + " " +
//                        uAge + " " +
//                        uBlood + " " +
//                        uLocation + " " +
//                        uActive, Toast.LENGTH_SHORT).show();

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        first.setText(memberInfo.getUserFirstName());
        last.setText(memberInfo.getUserLastName());
        age.setText(memberInfo.getUserAge());
        location.setText(memberInfo.getUserLocation());
        blood.setText(memberInfo.getUserBlood());
        if(memberInfo.getUserActive().equals("yes")){
            active.setChecked(true);
            active.setText("Active");
        }
        else{
            active.setChecked(false);
            active.setText("Not Active");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
        }

        return true;
    }

    public void homePage(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
