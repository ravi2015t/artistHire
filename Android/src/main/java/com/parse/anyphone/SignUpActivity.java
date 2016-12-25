package com.parse.anyphone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.eventplanner.models.Address;
import com.eventplanner.models.Server;
import com.eventplanner.models.User;
import com.eventplanner.models.VendorSignup;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A login screen that offers login via email/password.
 */
public class SignUpActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private AutoCompleteTextView mBudgetView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private AutoCompleteTextView mFirstName;
    private AutoCompleteTextView mLastName;
    private AutoCompleteTextView mPhoneNumber;
    private TextView mLoginValidationText;
    private String userType;
    private String server = Server.ADDRESS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Intent intent = getIntent();
        userType = intent.getStringExtra("USER_TYPE");

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mBudgetView = (AutoCompleteTextView) findViewById(R.id.budget);
        mFirstName = (AutoCompleteTextView) findViewById(R.id.firstName);
        mLastName = (AutoCompleteTextView) findViewById(R.id.lastName);
        mPhoneNumber = (AutoCompleteTextView) findViewById(R.id.phoneNumber);
        mLoginValidationText = (TextView) findViewById(R.id.loginValidationText);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        if(userType.equals("Customer"))
            mBudgetView.setVisibility(View.GONE);
        else
            mBudgetView.setVisibility(View.VISIBLE);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

//        if(userType.equals("Customer")){
//            User user = new User();
//        }else{
//            Vendor user = new Vendor();
//        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String firstName = mFirstName.getText().toString();
        String lastName = mLastName.getText().toString();
        String budget = mBudgetView.getText().toString();
        String phoneNumber = mPhoneNumber.getText().toString();

        boolean cancel = false;
        View focusView = null;

//        // Check for a valid password, if the user entered one.
//        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
//            mPasswordView.setError(getString(R.string.error_invalid_password));
//            focusView = mPasswordView;
//            cancel = true;
//        }
//
//        // Check for a valid email address.
//        if (TextUtils.isEmpty(email)) {
//            mEmailView.setError(getString(R.string.error_field_required));
//            focusView = mEmailView;
//            cancel = true;
//        } else if (!isEmailValid(email)) {
//            mEmailView.setError(getString(R.string.error_invalid_email));
//            focusView = mEmailView;
//            cancel = true;
//        }
        if(userType.equals("Customer")){
            if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(phoneNumber)) {
                mLoginValidationText.setError(getString(R.string.error_all_field_required));
                focusView = mEmailView;
                cancel = true;
            }
            else
            {
                User user = new User();
                user.setFirstname(firstName);
                user.setLastname(lastName);
                user.setUsername(email);
                user.setPhoneNumber(phoneNumber);
                user.setPassword(password);
                Address address = new Address();
                address.setCity("New York");
                address.setState("New York");
                address.setStreet("Ocean PKWY");
                address.setZip("11235");
                user.setAddress(address);

                Gson gson = new Gson();
                String json = gson.toJson(user);
                System.out.println("SIGNUP_JSON");
                System.out.println(json);

                new ServerAsyncTask().execute(json, null, null);
            }
        }else{
            if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) ||
                TextUtils.isEmpty(budget) || TextUtils.isEmpty(phoneNumber)) {
                mLoginValidationText.setError(getString(R.string.error_all_field_required));
                focusView = mEmailView;
                cancel = true;
            }
            else
            {
                VendorSignup vendor = new VendorSignup();
                vendor.setFirstname(firstName);
                vendor.setLastname(lastName);
                vendor.setUsername(email);
                vendor.setPrice(Long.parseLong(budget));
                vendor.setPhoneNumber(phoneNumber);
                vendor.setPassword(password);
                vendor.setCategoryNumber(userType.toLowerCase());
                Address address = new Address();
                address.setCity("New York");
                address.setState("New York");
                address.setStreet("Ocean PKWY");
                address.setZip("11235");
                vendor.setAddress(address);

                Gson gson = new Gson();
                String json = gson.toJson(vendor);
                System.out.println("SIGNUP_JSON");
                System.out.println(json);

                new ServerAsyncTask().execute(json, null, null);
            }
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
//            mAuthTask = new UserLoginTask(email, password);
//            mAuthTask.execute((Void) null);
        }
    }

    class ServerAsyncTask extends AsyncTask<String, Void, Boolean>
    {

        @Override
        protected Boolean doInBackground(String... params) {
//            boolean isSuccess = false;
            return requestServer(params[0]);
//            return isSuccess;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            Intent i;
            if (success){
//            if (true) {
                if(userType.equals("Customer")){
                    i = new Intent(SignUpActivity.this, UserProfileActivity.class);
                }else{
                    i = new Intent(SignUpActivity.this, VendorProfileActivity.class);
                    String budget = mBudgetView.getText().toString();
                    i.putExtra("BUDGET", budget);
                }
                String firstName = mFirstName.getText().toString();
                String lastName = mLastName.getText().toString();
                String phoneNumber = mPhoneNumber.getText().toString();
                i.putExtra("FIRST_NAME", firstName);
                i.putExtra("LAST_NAME", lastName);
                i.putExtra("PHONE_NUMBER", phoneNumber);
                i.putExtra("EMAIL", mEmailView.getText().toString());
                startActivity(i);
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }
    }

    private boolean requestServer(String json) {

        try {
            StringBuilder sb = new StringBuilder();
            HttpURLConnection urlConnection = null;
            URL url = null;
            if(userType.equals("Customer")){
                url = new URL(server + "rest/planner/userSignup");
            }else if (userType.equals("Photographer")){
                url = new URL(server + "rest/planner/vendorPhotographerSignup");
            }else if (userType.equals("Florist")){
                url = new URL(server + "rest/planner/vendorFloristSignup");
            }else if (userType.equals("MakeupArtist")){
                url = new URL(server + "rest/planner/vendorMakeUpartistSignup");
            }

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("POST");
//        urlConnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            if(userType.equals("Customer")){
                urlConnection.setRequestProperty("Content-Type","text/plain");
            }else
            {
                urlConnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            }
//        urlConnection.setReadTimeout(10000 /* milliseconds */);
//        urlConnection.setConnectTimeout(15000 /* milliseconds */);
//        urlConnection.setDoInput (true);
            urlConnection.setDoOutput (true);
//        urlConnection.setUseCaches (false);


            urlConnection.connect();

            JSONObject jsonParam = new JSONObject(json);

            System.out.println("Json ");
            System.out.println(json);
            Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            writer.write(json);
// json data
            writer.close();

            // Send POST output.
//        DataOutputStream printout = new DataOutputStream(urlConnection.getOutputStream());
//        printout.writeBytes(URLEncoder.encode(json,"UTF-8"));
//        printout.flush ();
//        printout.close ();

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

                if(sb.toString().equals("Success"))
                {
                    return true;
                }
                else
                    return false;

            }else{
                System.out.println(urlConnection.getResponseMessage());
            }

//        jsonString = sb.toString();

//        System.out.println("JSON: " + jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;

    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(SignUpActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Intent i = new Intent(SignUpActivity.this, UserProfileActivity.class);
                String firstName = mFirstName.getText().toString();
                String lastName = mLastName.getText().toString();
                String phoneNumber = mPhoneNumber.getText().toString();
                i.putExtra("FIRST_NAME", firstName);
                i.putExtra("LAST_NAME", lastName);
                i.putExtra("PHONE_NUMBER", phoneNumber);
                startActivity(i);
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

