package com.parse.anyphone;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.eventplanner.models.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class LoginActivity extends AppCompatActivity {


    private EditText emailView, passwordView;
    private Button loginView;
    private CheckBox customerView;

    public static String phoneNumber = null;
    private String token = null;
    private int code = 0;
    private int flag = 0; //If flag = 0 call the sendCode method, otherwise call the doLogin method.
    private String server = Server.ADDRESS;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailView           = (EditText) findViewById(R.id.email);
        passwordView        = (EditText) findViewById(R.id.password);
        customerView        = (CheckBox) findViewById(R.id.customer);
        progressBar         = (ProgressBar) findViewById(R.id.loadingSpinner);
        loginView           = (Button) findViewById(R.id.login);

        loginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loginView.setClickable(false);
                doLogin();
            }
        });
    }

    // Login page
    private void doLogin() {
        System.out.println(emailView.getText().toString());
        new LogInAsyncTask().execute(null, null, null);
    }

    class LogInAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            return requestServer();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            Intent i;
            if (success) {
                new GetProfileAsyncTask().execute();
            }else{
                passwordView.setError(getString(R.string.error_incorrect_password));
                passwordView.requestFocus();
            }
        }
    }

    class GetProfileAsyncTask extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Void... params) {
            return getProfile();
        }

        @Override
        protected void onPostExecute(final JSONObject success) {
            Intent i;

            if(success != null){
                System.out.println("--------------------");
                try {
                    if(customerView.isChecked()){
                        i = new Intent(LoginActivity.this, UserProfileActivity.class);
                    }else{
                        i = new Intent(LoginActivity.this, VendorProfileActivity.class);

                        i.putExtra("BUDGET", success.getString("price"));
                    }
                    i.putExtra("FIRST_NAME", success.getString("firstname"));
                    i.putExtra("PHONE_NUMBER", success.getString("phoneNumber"));
                    i.putExtra("LAST_NAME", success.getString("lastname"));
                    i.putExtra("EMAIL", emailView.getText().toString());
                    startActivity(i);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{

            }
        }
    }

    private JSONObject getProfile(){

        String email        = emailView.getText().toString();
        System.out.println(email);
        try {
            StringBuilder sb = new StringBuilder();
            HttpURLConnection urlConnection = null;
            URL url = null;
            if(customerView.isChecked()){
                url = new URL(server + "rest/planner/usergetProfile/" + email);
            }else {
                url = new URL(server + "rest/planner/vendorGetProfile/" + email);
            }

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type","text/plain");
//            urlConnection.setDoOutput (true);


            urlConnection.connect();

            int HttpResult =urlConnection.getResponseCode();
            System.out.println(urlConnection.getResponseMessage());
            System.out.println(HttpResult);
            System.out.println(urlConnection.getRequestMethod());
            if(HttpResult ==HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(),"utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();

                System.out.println(""+sb.toString());
                try {
                    JSONObject jsonObj = new JSONObject(sb.toString());
                    return jsonObj;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                if(sb.toString().equals("Success"))
//                {
//                    return true;
//                }
//                else
//                    return false;

            }else{
                System.out.println(urlConnection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean requestServer() {
        String email        = emailView.getText().toString();
        String password     = passwordView.getText().toString();
        try {
            StringBuilder sb = new StringBuilder();
            HttpURLConnection urlConnection = null;
            URL url = null;
            if(customerView.isChecked()){
                url = new URL(server + "rest/planner/userLogin/" + email + "/" + password);
            }else {
                url = new URL(server + "rest/planner/vendorLogin/" + email + "/" + password);
            }
            System.out.println(url);
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type","text/plain");

            urlConnection.connect();

            int HttpResult =urlConnection.getResponseCode();
            System.out.println(urlConnection.getResponseMessage());
            System.out.println(HttpResult);
            System.out.println(urlConnection.getRequestMethod());
            if(HttpResult ==HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(),"utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();

                System.out.println(""+sb.toString());

                if(sb.toString().equals("true"))
                {
                    System.out.println("It's working!");
                    return true;
                }
                else
                    return false;

            }else{
                System.out.println(urlConnection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
