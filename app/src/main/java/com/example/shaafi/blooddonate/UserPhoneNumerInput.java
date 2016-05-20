package com.example.shaafi.blooddonate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class UserPhoneNumerInput extends AppCompatActivity {
    ConnectivityManager connectivityManager;
    private int NETWORK_STATUS = 1;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    EditText userNumber;
    EditText userPass;
    Spinner userNumberOperator;
    CheckBox remember;

    String userPhoneNumber, userInfos, userOperator, userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_phone_numer_input);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setIcon(R.mipmap.ic_launcher);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE);

        setLayoutDisplay();


    }

    private void setLayoutDisplay() {

        boolean saveLogin = sharedPreferences.getBoolean("remember", false);

        userNumber = (EditText) findViewById(R.id.editTextPhoneNumber_userIfo);
        userNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNumber.setError(null);
            }
        });
        userNumberOperator = (Spinner) findViewById(R.id.phn_spinner_userIfo);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.phone_numbers));
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        userNumberOperator.setAdapter(adapter);
        userNumberOperator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userOperator = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        userPass = (EditText) findViewById(R.id.editTextPass_userInfo);
        userPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPass.setError(null);
            }
        });

        remember = (CheckBox) findViewById(R.id.checkBox_remember);


        if (saveLogin) {
            userPass.setText(sharedPreferences.getString("uPass", ""));
            userNumber.setText(sharedPreferences.getString("uNumber", ""));
            remember.setChecked(true);
        }
        else{
            userPass.setText(null);
            userNumber.setText(null);
            remember.setChecked(false);
        }

    }

    public void checkNetwork() {
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                NETWORK_STATUS = 1;
                //Toast.makeText(this, "wifi connected", Toast.LENGTH_SHORT).show();
            }

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                NETWORK_STATUS = 2;
                //Toast.makeText(this, "mobile data connected", Toast.LENGTH_SHORT).show();
            }
        } else
            NETWORK_STATUS = 0;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }

    public void userInfoClass(View view) {
        checkNetwork();

        if (remember.isChecked()) {
            saveData();
        } else{
            editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
        }

        userPhoneNumber = null;
        userInfos = null;
        userPassword = null;

        userPhoneNumber = userOperator + userNumber.getText().toString();
        userPassword = userPass.getText().toString();

        //Toast.makeText(this, userPhoneNumber, Toast.LENGTH_SHORT).show();

        if (userPhoneNumber.length() == 14 && !userPassword.isEmpty()) {
            if (NETWORK_STATUS == 0) {
                Toast.makeText(this, "Please connect your internet first", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(this, userPhoneNumber + " " + userPassword, Toast.LENGTH_SHORT).show();
                BackgroundTaskUserInfo backgroundTaskUserInfo = new BackgroundTaskUserInfo(this);
                backgroundTaskUserInfo.execute(userPhoneNumber, userPassword);
            }
        } else if (userPassword.isEmpty()) {
            userPass.setError("Enter Password");
        } else {
            userNumber.setError("Please Enter Your Phone Number");
        }
    }

    public void saveData() {
        editor = sharedPreferences.edit();
        editor.putString("uNumber", userNumber.getText().toString());
        editor.putString("uPass", userPass.getText().toString());
        editor.putBoolean("remember", true);
        editor.apply();
    }


    //background search for user info in database

    class BackgroundTaskUserInfo extends AsyncTask<String, Void, String> {
        private static final String FORMAT = "UTF-8";
        Context ctx;

        String login_url;

        ProgressDialog progressDialog;

        public BackgroundTaskUserInfo(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ctx);

            progressDialog.setTitle("Getting Data....");
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();

            login_url = "http://www.androidtesting.tk/connect_blood_donation_get_user_info_2.php";
        }

        @Override
        protected String doInBackground(String... params) {
            {
                try {
                    URL url = new URL(login_url);

                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                    httpURLConnection.setRequestMethod("POST");

                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();

                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, FORMAT));

                    String data = URLEncoder.encode("userNumber", FORMAT) + "=" + URLEncoder.encode(params[0], FORMAT) + "&"
                            + URLEncoder.encode("password", FORMAT) + "=" + URLEncoder.encode(params[1], FORMAT);

                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();

                    outputStream.close();


                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    StringBuilder stringBuilder = new StringBuilder();
                    String response;

                    while ((response = bufferedReader.readLine()) != null) {

                        stringBuilder.append(response + "\n");
                    }

                    bufferedReader.close();
                    inputStream.close();


                    httpURLConnection.disconnect();

                    return stringBuilder.toString().trim();

                } catch (java.net.SocketTimeoutException e) {
                    return "Connection time out";
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            return "Sorry..could not Connect";

        }

        @Override
        protected void onPostExecute(String s) {

            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }


            //Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();

            if (s.charAt(0) == '{') {
                userInfos = s;

                Intent i = new Intent(getBaseContext(), UserInfo.class);

                i.putExtra("userData", userInfos);

                startActivity(i);
                finish();
            } else {
                Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
            }
        }

    }
}
