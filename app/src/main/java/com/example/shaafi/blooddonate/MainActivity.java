package com.example.shaafi.blooddonate;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setIcon(R.mipmap.ic_launcher);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    public void resisterNewUser(View view) {

        Intent goToRegister = new Intent(this, PhoneVerify.class);
        startActivity(goToRegister);

    }

    public void searchForBlood(View view) {
        Intent goToSearchPage = new Intent(this, SearchPage.class);
        startActivity(goToSearchPage);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.about) {
            startActivity(new Intent(getBaseContext(), AboutUs.class));
        } else if (item.getItemId() == R.id.user_info) {
            startActivity(new Intent(this, UserPhoneNumerInput.class));
        } else if (item.getItemId() == R.id.color_change) {
            this.setTheme(R.style.blue_bg);
            Toast.makeText(this, "changed", Toast.LENGTH_SHORT).show();
        }
        else if(item.getItemId() == R.id.web_page){
            Intent i = new Intent(Intent.ACTION_VIEW);
            PackageManager packageManager = getPackageManager();
            List activities = packageManager.queryIntentActivities(i,
                    PackageManager.MATCH_DEFAULT_ONLY);
            boolean isIntentSafe = activities.size() > 0;
            if(isIntentSafe) {
                i.setData(Uri.parse("http://blooddonorsbd.tk"));
                startActivity(i);
            }
            else{
                Toast.makeText(this,"No browser found",Toast.LENGTH_SHORT).show();
            }

        }

        return true;
    }

}
