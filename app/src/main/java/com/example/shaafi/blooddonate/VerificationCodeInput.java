package com.example.shaafi.blooddonate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class VerificationCodeInput extends AppCompatActivity {

    EditText verify;
    String phnNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_input);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setIcon(R.mipmap.ic_launcher);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        verify = (EditText) findViewById(R.id.editTextVerify);

        phnNum = getIntent().getStringExtra("uNumber");
    }

    public void showFullList(View view) {

        Intent intent = new Intent(this, SearchResultList.class);
        intent.putExtra("loggedin", true);
        startActivity(intent);

    }

    public void doneVerify(View view) {

        String ver = verify.getText().toString();

        if(ver.length() == 4){
            Intent intent = new Intent(this, RegisterClass.class);
            intent.putExtra("uNum", phnNum);
            startActivity(intent);
        }
        else{
            verify.setError("Wrong Verification Code");
            verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verify.setText("");
                }
            });
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
