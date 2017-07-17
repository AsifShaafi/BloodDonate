package com.example.shaafi.blooddonate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.net.URL;
import java.net.URLEncoder;

public class PhoneVerify extends AppCompatActivity {
    public static String userMobile;
    EditText phoneNumber;
    Spinner phnOperator;
    private ConnectivityManager connectivityManager;
    private int NETWORK_STATUS;
    private String phnNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verify);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setIcon(R.mipmap.ic_launcher);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        phoneNumber = (EditText) findViewById(R.id.editTextPhoneNumber);
        phnOperator = (Spinner) findViewById(R.id.phn_spinner);
        //array adapter for phone operators
        String[] phnArray = getResources().getStringArray(R.array.phone_numbers);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, phnArray);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        phnOperator.setAdapter(adapter);
        phnOperator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userMobile = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void checkNumber() {

        BackgroundTaskCheckPhone backgroundTaskCheckPhone = new BackgroundTaskCheckPhone(this);
        backgroundTaskCheckPhone.execute(phnNum);

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
        if (item.getItemId() == R.id.about) {
            startActivity(new Intent(getBaseContext(), AboutUs.class));
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void phnETclick() {
        phoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber.setText("");
            }
        });
    }

    public void phoneRegister(View view) {

        checkNetwork();

        phnNum = phoneNumber.getText().toString();

        if (phnNum.length() == 0) {
            phoneNumber.setError("Please enter a phone number!");
            phnETclick();

        } else if (phnNum.length() < 8 || phnNum.length() > 8) {
            phoneNumber.setError("Please enter a valid number");
            phnETclick();
        } else if (phnNum.length() == 8) {

            //int phoneNumberNum = Integer.parseInt(userMobile);

            phnNum = userMobile + phnNum;

            //Toast.makeText(this, "Number is: " + phnNum, Toast.LENGTH_SHORT).show();
            if (NETWORK_STATUS == 0) {
                Toast.makeText(this, "Please connect your internet first", Toast.LENGTH_SHORT).show();
            } else {
                checkNumber();
            }

        }
    }


    private class BackgroundTaskCheckPhone extends AsyncTask<String, Void, String> {
        private static final String FORMAT = "UTF-8";
        Context ctx;

        String login_url;

        ProgressDialog progressDialog;

        BackgroundTaskCheckPhone(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ctx);

            progressDialog.setTitle("Checking Number....");
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();

            login_url = "http://www.androidtesting.tk/connect_blood_donation_check_phone_2.php";
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

                    String data = URLEncoder.encode("Number", FORMAT) + "=" + URLEncoder.encode(phnNum, FORMAT);

                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();

                    outputStream.close();


                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    StringBuilder stringBuilder = new StringBuilder();
                    String response;

                    while ((response = bufferedReader.readLine()) != null) {

                        stringBuilder.append(response).append("\n");
                    }

                    bufferedReader.close();
                    inputStream.close();


                    httpURLConnection.disconnect();

                    return stringBuilder.toString().trim();

                } catch (java.net.SocketTimeoutException e) {
                    return "Connection time out";
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

            if (s.equals("Connection time out") || s.equals("Sorry..could not Connect")) {
                Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
            } else if (s.contains("found")) {

                Toast.makeText(getBaseContext(), "This Number is already Registered\n" + s, Toast.LENGTH_SHORT).show();

            } else {

                //Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
                Intent goToRegister = new Intent(getBaseContext(), VerificationCodeInput.class);
                goToRegister.putExtra("uNumber", phnNum);
                startActivity(goToRegister);

            }
        }

    }
}
