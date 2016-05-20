package com.example.shaafi.blooddonate;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class RegisterClass extends AppCompatActivity {
    ConnectivityManager connectivityManager;
    private int NETWORK_STATUS = 1;

    //user information
    public static String userFirstName;
    public static String userLastName;
    public static String userAge;
    public static String userLocation;
    public static String userBloodType;
    public static String userPass;
    public static String userConfirmPass;
    public static String userPhnNumber;
    public static String userActive = "no";

    public static int done = 0;


    //check variables for checking the fields
    private static boolean AGELIMIT;
    private static boolean FIRSTNAME;
    private static boolean LASTNAME;
    private static boolean AGE;
    private static boolean LOCATION;
    private static boolean FITNESS;
    private static boolean PASS;

    EditText etFirstName;
    EditText etLastName;
    EditText etAge;
    EditText etPass;
    EditText etConfirmPass;
    Spinner etBloodGrp;
    AutoCompleteTextView etLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_class);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setIcon(R.mipmap.ic_launcher);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        setLayoutObjects();
        focusChange();

        userPhnNumber = getIntent().getStringExtra("uNum");
    }

    private void setLayoutObjects() {

        //autocomplete blood grp setting
        etBloodGrp = (Spinner) findViewById(R.id.editTextBloodGrp);
        String[] bloods = getResources().getStringArray(R.array.bloodGrpArray);

        ArrayAdapter<String> bloodGrpArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bloods);
        bloodGrpArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        etBloodGrp.setAdapter(bloodGrpArrayAdapter);
        //etBloodGrp.setSelection(0, false);
        etBloodGrp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userBloodType = parent.getItemAtPosition(position).toString();
                //Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " selected", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //edit text name
        etFirstName = (EditText) findViewById(R.id.editTextFirstName);
        etLastName = (EditText) findViewById(R.id.editTextLastName);

        //edit text age
        etAge = (EditText) findViewById(R.id.editTextAge);

        //edit text location
        etLocation = (AutoCompleteTextView) findViewById(R.id.editTextLocation);
        String[] locations = getResources().getStringArray(R.array.locaionArray);
        etLocation.setThreshold(1);

        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, locations);
        etLocation.setAdapter(locationAdapter);

        etPass = (EditText) findViewById(R.id.editTextLastPass);
        etConfirmPass = (EditText) findViewById(R.id.editTextLastConfirmPass);

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
        if (item.getItemId() == R.id.about) {
            startActivity(new Intent(getBaseContext(), AboutUs.class));
        }

        return true;
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


    private void focusChange() {
        //checking first name
        etFirstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    storeData();
                    if (userFirstName.isEmpty()) {
                        FIRSTNAME = false;
                        etFirstName.setError("Please enter your First name");
                        //Toast.makeText(getBaseContext(), FIRSTNAME + "", Toast.LENGTH_SHORT).show();
                    } else {
                        FIRSTNAME = true;
                        //Toast.makeText(getBaseContext(), FIRSTNAME + "", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

        //checking last name
        etLastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    storeData();
                    if (userLastName.length() == 0) {
                        LASTNAME = false;
                        etLastName.setError("Please enter your Last name");
                    } else {
                        LASTNAME = true;
                        //Toast.makeText(getBaseContext(), LASTNAME + "", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //checking age
        etAge.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    storeData();
                    if (userAge.isEmpty()) {
                        etAge.setError("Please enter your age");
                        AGE = false;
                    } else {
                        int ageInt = Integer.parseInt(userAge);
                        if (ageInt >= 18) {
                            AGE = true;
                            //Toast.makeText(getBaseContext(), AGE + "", Toast.LENGTH_SHORT).show();
                            //Toast.makeText(getBaseContext(), AGELIMIT + "", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getBaseContext(), "SORRY, you can not register for this program", Toast.LENGTH_SHORT).show();
                            AGELIMIT = true;
                            AGE = false;
                        }
                    }
                }
            }
        });

        //checking location
        etLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    storeData();
                    if (userLocation.isEmpty()) {
                        etLocation.setError("Please enter your location");
                        LOCATION = false;
                    } else {
                        LOCATION = true;
                        //Toast.makeText(getBaseContext(), LOCATION + "", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //pass check
        etPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    storeData();
                    if(userPass.isEmpty()){
                        etPass.setError("Enter a password");
                    }
                }
            }
        });

        etConfirmPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    storeData();
                    if(!userPass.isEmpty() && !userConfirmPass.isEmpty()){
                        if(userConfirmPass.equals(userPass)){
                            PASS = true;
                        }
                        else{
                            etConfirmPass.setError("Password didn't match");
                            PASS = false;
                        }
                    }
                    else if(userConfirmPass.isEmpty()){
                        etConfirmPass.setError("Re-enter your password");
                        PASS = false;
                    }
                }
            }
        });
    }


    //storing user data
    void storeData() {

        userFirstName = etFirstName.getText().toString();
        userLastName = etLastName.getText().toString();
        userAge = etAge.getText().toString();
        userLocation = etLocation.getText().toString();
        userPass = etPass.getText().toString();
        userConfirmPass = etConfirmPass.getText().toString();

    }


    //handling the button event
    public void doneRegister(View view) {
        checkNetwork();

        storeData();
        if (userLocation.isEmpty()) {
            etLocation.setError("Please enter your location");
            LOCATION = false;
        } else {
            LOCATION = true;
            //Toast.makeText(getBaseContext(), LOCATION + "", Toast.LENGTH_SHORT).show();
        }

        if (!FITNESS) {
            Toast.makeText(this, "Please check all the Data carefully", Toast.LENGTH_LONG).show();
        }

        //checking if all fields are correctly filled
        if (FIRSTNAME && LASTNAME && AGE && LOCATION && FITNESS && PASS) {

            String method = "register";
            //Toast.makeText(this, userFirstName + " " + userLastName + " " + userPhnNumber + " " + userAge + " " + userBloodType + " " + userLocation + " " + userActive, Toast.LENGTH_SHORT).show();
            if (NETWORK_STATUS == 0) {
                Toast.makeText(this, "Please connect your internet first", Toast.LENGTH_SHORT).show();
            }else {
                BackgroundTask backgroundTask = new BackgroundTask(this);
                backgroundTask.execute(method, userFirstName, userLastName, userPhnNumber, userAge, userBloodType, userLocation, userActive, userPass);
            }

//            else {
//
//                Intent i = new Intent(this, MemberInfoDisplay.class);
//                i.putExtra("fname", userFirstName);
//                i.putExtra("lname", userLastName);
//                i.putExtra("location", userLocation);
//                i.putExtra("blood", userBloodType);
//                i.putExtra("age", userAge);
//                i.putExtra("active", userActive);
//
//                startActivity(i);
//            }


        }
    }

    public void activeCheck(View view) {
        FITNESS = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.radio_button_Y:
                if (FITNESS) {
                    userActive = "yes";
                }
                break;
            case R.id.radio_button_N:
                if (FITNESS) {
                    userActive = "no";
                }
                break;
        }
    }
}
