package com.parse.anyphone;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.eventplanner.models.RecommendationResults;
import com.eventplanner.models.Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
public class UserProfileActivity extends AppCompatActivity {

    private static final String SERVER_ADDRESS = "http://sample-env-1.ngmrx3yxch.us-west-2.elasticbeanstalk.com";
    private TextView mUserNametextView;
    private TextView mPhoneNumbertextView;
    private Button mToBeConfirmedEventButton;
    private Button mRecommendationButton;

    private Button buttonChoose;
    private Button buttonUpload;

    private ImageView imageView;

    private EditText editTextName;
    private EditText budgetText;

    private Bitmap bitmap;
    private Uri filePath;
    private int PICK_IMAGE_REQUEST = 1;

    private String UPLOAD_URL ="http://simplifiedcoding.16mb.com/VolleyUpload/upload.php";

    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";
    private ArrayList<RecommendationResults> results;
    private CalendarView calendarView2;
	BasicAWSCredentials awsCreds = new BasicAWSCredentials("", "");
    private String bucketName = "eventplanner";
    private Button mconfirmedEventbutton;
    String firstName;
    String lastName;
    String phoneNumber;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("User Profile");

        imageView  = (ImageView) findViewById(R.id.imageView);

        new GetProfileImageAsyncTask().execute();

        imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                }

        });
//        buttonUpload.setOnClickListener(this);

        mUserNametextView = (TextView) findViewById(R.id.userNametextView);
        mPhoneNumbertextView = (TextView) findViewById(R.id.phoneNumbertextView);
        mToBeConfirmedEventButton = (Button) findViewById(R.id.ToBeConfirmedEventButton);
        mRecommendationButton = (Button) findViewById(R.id.recommendation_for_you_button);
        mconfirmedEventbutton = (Button) findViewById(R.id.confirmedEventbutton);

        Intent intent = getIntent();
        firstName = intent.getStringExtra("FIRST_NAME");
        lastName = intent.getStringExtra("LAST_NAME");
        phoneNumber = intent.getStringExtra("PHONE_NUMBER");
        username = intent.getStringExtra("EMAIL");

        mUserNametextView.setText("Name: " + firstName + " " + lastName);
        mPhoneNumbertextView.setText("PH: " + phoneNumber);

        mconfirmedEventbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserProfileActivity.this, ConfirmedActivity.class);
                i.putExtra("FIRST_NAME", firstName);
                i.putExtra("LAST_NAME", lastName);
                i.putExtra("PHONE_NUMBER", phoneNumber);
                i.putExtra("BUDGET", "0");
                i.putExtra("USERNAME", username);
                i.putExtra("USER_TYPE", "customer");
                startActivity(i);
            }
        });

        mToBeConfirmedEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserProfileActivity.this, PendingEventActivity.class);
                i.putExtra("FIRST_NAME", firstName);
                i.putExtra("LAST_NAME", lastName);
                i.putExtra("PHONE_NUMBER", phoneNumber);
                i.putExtra("USERNAME", username);
                i.putExtra("USER_TYPE", "customer");
                startActivity(i);
            }
        });

        mRecommendationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
                new ImageUploaderAsyncTask().execute();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    class ImageAsyncTask extends AsyncTask<String, String, Boolean>
    {

        @Override
        protected Boolean doInBackground(String... params) {
//            boolean isSuccess = false;
            return requestServer("rest/planner/userRegisterWedding", params[0]);
//            return requestServer("/rest/planner/userBookEvent", params[0]);

//            return isSuccess;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
//            showProgress(false);

            if (success) {
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(SignUpActivity.this, UserProfileActivity.class);
//                String firstName = mFirstName.getText().toString();
//                String lastName = mLastName.getText().toString();
//                String phoneNumber = mPhoneNumber.getText().toString();
//                i.putExtra("FIRST_NAME", firstName);
//                i.putExtra("LAST_NAME", lastName);
//                i.putExtra("PHONE_NUMBER", phoneNumber);
//                startActivity(i);
//                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
            }
        }
    }

    private void showPopup() {
        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.custom_dialog);
        dialog.setTitle("My Preference");

        budgetText = (EditText) dialog.findViewById(R.id.budgetText);
        Spinner preferenceSpinner = (Spinner) dialog.findViewById(R.id.preferenceSpinner);
        calendarView2 = (CalendarView) dialog.findViewById(R.id.calendarView);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.preference_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        preferenceSpinner.setAdapter(adapter);

        Button btnOk          = (Button) dialog.findViewById(R.id.ok);
        Button btnCancel        = (Button) dialog.findViewById(R.id.cancel);
        dialog.show();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BudgetAsyncTask().execute(budgetText.getText().toString());
                dialog.dismiss();
//                budgetText.getText().toString();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                budgetText.getText().toString();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        return true;
    }

    class ServerAsyncTask extends AsyncTask<String, String, Boolean>
    {

        @Override
        protected Boolean doInBackground(String... params) {
//            boolean isSuccess = false;
            return requestServer("rest/planner/userRegisterWedding", params[0]);
//            return requestServer("/rest/planner/userBookEvent", params[0]);

//            return isSuccess;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
//            showProgress(false);

            if (success) {
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(SignUpActivity.this, UserProfileActivity.class);
//                String firstName = mFirstName.getText().toString();
//                String lastName = mLastName.getText().toString();
//                String phoneNumber = mPhoneNumber.getText().toString();
//                i.putExtra("FIRST_NAME", firstName);
//                i.putExtra("LAST_NAME", lastName);
//                i.putExtra("PHONE_NUMBER", phoneNumber);
//                startActivity(i);
//                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
            }
        }
    }

    class BudgetAsyncTask extends AsyncTask<String, String, String>
    {

        @Override
        protected String doInBackground(String... params) {
            return requestServerGET("rest/planner/recommend/" + params[0]);
        }

        @Override
        protected void onPostExecute(final String json) {

            if (json != null) {
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(UserProfileActivity.this, RecommendationActivity.class);
                i.putExtra("RECOS", json);
                i.putExtra("USER_NAME", username);
                i.putExtra("DATE", new Long(calendarView2.getDate()).toString());
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean requestServer(String relativeUrl, String json) {

        try {
            StringBuilder sb = new StringBuilder();
            HttpURLConnection urlConnection = null;

            URL url = new URL(Server.ADDRESS + relativeUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            urlConnection.setDoOutput (true);

            urlConnection.connect();

            System.out.println("Json ");
            System.out.println(json);
            Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            writer.write(json);
            writer.close();


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
                    return true;
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

    private String requestServerGET(String relativeUrl) {

        try {
            StringBuilder sb = new StringBuilder();
            HttpURLConnection urlConnection = null;

            System.out.println(Server.ADDRESS + relativeUrl);
            URL url = new URL(Server.ADDRESS + relativeUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
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
                    sb.append(line + "\n");
                }
                br.close();

                if(sb.length() > 0)
                    return sb.toString();
                else
                    return null;

            }else{
                System.out.println(urlConnection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
	
	class GetProfileImageAsyncTask extends AsyncTask<String, String, Bitmap>
    {

        @Override
        protected Bitmap doInBackground(String... params) {
            AmazonS3 s3client = new AmazonS3Client(awsCreds);
            try {
                S3ObjectInputStream content = s3client.getObject(bucketName, "usr/profile/" + username + "/profile.jpeg").getObjectContent();
                byte[] bytes = IOUtils.toByteArray(content);
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }catch(Exception e){
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Bitmap success) {

            if (success != null) {
                imageView.setImageBitmap(success);
            }else{
                Toast.makeText(getApplicationContext(), "Please Choose Profile Picture", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class ImageUploaderAsyncTask extends AsyncTask<String, String, Boolean>
    {

        @Override
        protected Boolean doInBackground(String... params) {
            AmazonS3 s3client = new AmazonS3Client(awsCreds);
            s3client.putObject(new PutObjectRequest(bucketName, "usr/profile/" + username + "/profile.jpeg", new File(getPath(filePath))));
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getPath(Uri uri){
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }

}
