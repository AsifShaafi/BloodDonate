package com.example.shaafi.blooddonate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Shaafi on 06-Jan-16, 2016.
 */

public class BackgroundTask extends AsyncTask<String, Void, String> {

    private static final String FORMAT = "UTF-8";
    private static String callingMethod;
    Context ctx;


    String reg_url;
    String first_name;
    String last_name;
    String mobile_number;
    String age_user;
    String blood_type_user;
    String location_user;
    String active_user;
    String pass;

    ProgressDialog progressDialog;

    public BackgroundTask(Context ctx) {
        this.ctx = ctx;

    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(ctx);
        reg_url = "http://www.androidtesting.tk/connectivity-sign-up_2.php";
    }

    @Override
    protected String doInBackground(String... params) {

        //String reg_url = "http://10.0.2.2/webapp/register.php";
        //String reg_url = "http://192.168.0.101/webapp/register.php";
        //String reg_url = "http://192.168.0.101/blood/connect_blood_donation.php";


        //String login_url = "http://10.0.2.2/webapp/login.php";


        //String login_url = "http://192.168.0.101/blood/login.php";


        String method = params[0];
        callingMethod = params[0];
        if (method.equals("register")) {


             first_name = params[1];
             last_name = params[2];
             mobile_number = params[3];
             age_user = params[4];
             blood_type_user = params[5];
             location_user = params[6].toLowerCase();
             active_user = params[7];
             pass = params[8];

            try {
                URL url = new URL(reg_url);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setConnectTimeout(5000);


                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, FORMAT));
                String data = URLEncoder.encode("firstname", FORMAT) + "=" + URLEncoder.encode(first_name, FORMAT) + "&"
                        + URLEncoder.encode("lastname", FORMAT) + "=" + URLEncoder.encode(last_name, FORMAT) + "&"
                        + URLEncoder.encode("mobile_no", FORMAT) + "=" + URLEncoder.encode(mobile_number, FORMAT) + "&"
                        + URLEncoder.encode("password", FORMAT) + "=" + URLEncoder.encode(pass, FORMAT) + "&"
                        + URLEncoder.encode("age", FORMAT) + "=" + URLEncoder.encode(age_user, FORMAT) + "&"
                        + URLEncoder.encode("blood_type", FORMAT) + "=" + URLEncoder.encode(blood_type_user, FORMAT) + "&"
                        + URLEncoder.encode("location", FORMAT) + "=" + URLEncoder.encode(location_user, FORMAT) + "&"
                        + URLEncoder.encode("active", FORMAT) + "=" + URLEncoder.encode(active_user, FORMAT);

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                is.close();

                httpURLConnection.disconnect();

                return "Registration complete!";


            } catch (java.net.SocketTimeoutException e) {
                return "Connection time out";
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(ctx, "Prb is: " + e, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return "Sorry..could not register";
        }

        return "Sorry..could not Connect";
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        if (callingMethod.equals("register")) {
            Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
            Intent i = new Intent(ctx, MemberInfoDisplay.class);
            i.putExtra("fname", first_name);
            i.putExtra("lname", last_name);
            i.putExtra("location", location_user);
            i.putExtra("blood", blood_type_user);
            i.putExtra("age", age_user);
            i.putExtra("active", active_user);

            ctx.startActivity(i);
        }
        else{

            Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
        }

    }
}
