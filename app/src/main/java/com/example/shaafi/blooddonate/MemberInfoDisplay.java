package com.example.shaafi.blooddonate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class MemberInfoDisplay extends AppCompatActivity {

    Switch aSwitch;
    TextView f, l, a, lo, bl;

    String fname, lname, active, location, age, blood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_info_display);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setIcon(R.mipmap.ic_launcher);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        gettingInfo();

        setLayouts();

    }

    private void gettingInfo() {
        fname = getIntent().getStringExtra("fname");
        lname = getIntent().getStringExtra("lname");
        location = getIntent().getStringExtra("location");
        age = getIntent().getStringExtra("age");
        active = getIntent().getStringExtra("active");
        blood = getIntent().getStringExtra("blood");
    }

    private void setLayouts() {

        aSwitch = (Switch) findViewById(R.id.activity_switch);
        if (active.equals("yes")) {
            aSwitch.setChecked(true);
        }
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (aSwitch.isChecked()) {
                    aSwitch.setText("Active");
                } else {
                    aSwitch.setText("Not Active");
                }
            }
        });

        f = (TextView) findViewById(R.id.tv_first_display);
        f.setText(fname);

        l = (TextView) findViewById(R.id.tv_last_display);
        l.setText(lname);

        a = (TextView) findViewById(R.id.tv_age_display);
        a.setText(age);

        lo = (TextView) findViewById(R.id.tv_location_display);
        lo.setText(location);

        bl = (TextView) findViewById(R.id.tv_blood_display);
        bl.setText(blood);
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

    public void homePage(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
