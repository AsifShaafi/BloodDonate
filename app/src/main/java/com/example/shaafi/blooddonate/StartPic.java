package com.example.shaafi.blooddonate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class StartPic extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_pic);

        Thread startingPic = new Thread(){
            @Override
            public void run() {
                try {

                    sleep(2000);

                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        startingPic.start();
    }
}
