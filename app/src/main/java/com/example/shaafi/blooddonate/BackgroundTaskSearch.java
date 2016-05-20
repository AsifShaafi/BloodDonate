package com.example.shaafi.blooddonate;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
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

/**
 * Created by Shaafi on 06-Jan-16, 2016.
 */

public class BackgroundTaskSearch extends AsyncTask<String, Void, String> {

    private static final String FORMAT = "UTF-8";
    private static String callingMethod;
    Context ctx;

    String login_url;

    ProgressDialog progressDialog;

    public BackgroundTaskSearch(Context ctx) {
        this.ctx = ctx;

    }

    @Override
    protected void onPreExecute() {
        progressDialog  = new ProgressDialog(ctx);

        progressDialog.setTitle("Connecting....");
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        login_url = "http://www.androidtesting.tk/connect_blood_donation_fetch_data_2.php";
        //login_url = "http://10.0.2.3/connect_blood_donation_fetch_data.php";
    }

    @Override
    protected String doInBackground(String... params) {

        String method = params[0];
        callingMethod = params[0];
        if(method.equals("search"))
        {
            //progressDialog.show();

            String bloodGrp = params[1];
            String location = params[2].toLowerCase();

            try {
                URL url = new URL(login_url);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, FORMAT));

                String data = URLEncoder.encode("bloodType", FORMAT) + "=" + URLEncoder.encode(bloodGrp, FORMAT) + "&"
                        +URLEncoder.encode("location", FORMAT) + "=" + URLEncoder.encode(location, FORMAT);

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();

                outputStream.close();


                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder stringBuilder = new StringBuilder();
                String response;

                while ((response = bufferedReader.readLine()) != null){

                    stringBuilder.append(response+"\n");
                }

                bufferedReader.close();
                inputStream.close();


                httpURLConnection.disconnect();

                return stringBuilder.toString().trim();

            }catch (java.net.SocketTimeoutException e) {
                return "Connection time out";
            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




        return "Sorry..could not Connect";

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {

        if(progressDialog.isShowing()){
            progressDialog.cancel();
        }

        //Toast.makeText(ctx, result + " " + result.charAt(12), Toast.LENGTH_LONG).show();
        if(callingMethod.equals("search")){

            if(result.equals("Sorry..could not Connect")){
                Toast.makeText(ctx, result + "\nPlease check your internet connection and try again", Toast.LENGTH_LONG).show();
                SearchPage.jsonString = null;
            }

            else if(!result.equals("Connection time out") && result.charAt(12) != ']'){

                Toast.makeText(ctx, "Member found!\nPlease Click Show result for details", Toast.LENGTH_LONG).show();
                SearchPage.jsonString = result;
            }
            else if(result.charAt(12) == ']'){
                Toast.makeText(ctx, "Sorry no member found", Toast.LENGTH_LONG).show();
                SearchPage.jsonString = null;
            }
            else if(result.equals("Connection time out")){
                Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
                SearchPage.jsonString = null;
            }
        }
    }
}
