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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import com.eventplanner.models.Rating;
import com.eventplanner.models.Server;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class VendorProfileActivity extends AppCompatActivity {

    private static final String SERVER_ADDRESS = "http://sample-env-1.ngmrx3yxch.us-west-2.elasticbeanstalk.com";
    private TextView mUserNametextView;
    private TextView mPhoneNumbertextView;
    private TextView mBudgettextView;
    private Button mconfirmedEventbutton;
    private Button mPendingEventButton;
    private Button mUploadPicture;
    private ImageView imageView;
    private int PICK_IMAGE_REQUEST = 1;
    private int PICK_IMAGE_UPLOAD_ALBUM_REQUEST = 2;
    private Uri filePath;
    private Bitmap bitmap;

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String budget;
    BasicAWSCredentials awsCreds = new BasicAWSCredentials("","");
    private String bucketName = "eventplanner";
    private GridView gridView;
    private String username;

    private ArrayList<Item> itemArray = new ArrayList<Item>();
    private boolean mVisitorMode = false;
    private Button submitRateButton;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Vendor Profile");

        gridView = (GridView) findViewById(R.id.gridview);

        mUserNametextView = (TextView) findViewById(R.id.userNametextView);
        mPhoneNumbertextView = (TextView) findViewById(R.id.phoneNumbertextView);
        mBudgettextView = (TextView) findViewById(R.id.budgettextView);
        submitRateButton = (Button) findViewById(R.id.ratingSubmitButton);

        mconfirmedEventbutton = (Button) findViewById(R.id.confirmedEventbutton);
        mPendingEventButton = (Button) findViewById(R.id.ToBeConfirmedEventButton);
        mUploadPicture = (Button) findViewById(R.id.UploadPicture);

        imageView = (ImageView) findViewById(R.id.imageView);

        Intent intent = getIntent();
        firstName = intent.getStringExtra("FIRST_NAME");
        lastName = intent.getStringExtra("LAST_NAME");
        phoneNumber = intent.getStringExtra("PHONE_NUMBER");
        budget = intent.getStringExtra("BUDGET");
        username = intent.getStringExtra("EMAIL");
        if (intent.getBooleanExtra("IS_VISITOR", false))
            mVisitorMode = true;

        if (mVisitorMode) {
            submitRateButton.setVisibility(View.VISIBLE);
            mUploadPicture.setVisibility(View.GONE);
            mconfirmedEventbutton.setVisibility(View.GONE);
            mPendingEventButton.setVisibility(View.GONE);
            imageView.setClickable(false);
        }

        mUserNametextView.setText("Name: " + firstName + " " + lastName);
        mPhoneNumbertextView.setText("PH: " + phoneNumber);
        mBudgettextView.setText("Price: " + budget);

        System.out.println("Before Get Profile Image");
        new GetProfileImageAsyncTask().execute();

        new GetAlbumAsyncTask().execute();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }

        });

        mPendingEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(VendorProfileActivity.this, PendingEventActivity.class);
                i.putExtra("FIRST_NAME", firstName);
                i.putExtra("LAST_NAME", lastName);
                i.putExtra("PHONE_NUMBER", phoneNumber);
                i.putExtra("BUDGET", budget);
                i.putExtra("USERNAME", username);
                i.putExtra("USER_TYPE", "vendor");
                startActivity(i);
            }
        });

        try {
            budget = intent.getStringExtra("BUDGET");
        } catch (Exception e) {

        }

        mconfirmedEventbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VendorProfileActivity.this, ConfirmedActivity.class);
                i.putExtra("FIRST_NAME", firstName);
                i.putExtra("LAST_NAME", lastName);
                i.putExtra("USERNAME", username);
                i.putExtra("PHONE_NUMBER", phoneNumber);
                i.putExtra("BUDGET", budget);
                i.putExtra("USER_TYPE", "vendor");
                startActivity(i);
            }
        });

        mUploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Upload Picture"), PICK_IMAGE_UPLOAD_ALBUM_REQUEST);
            }
        });
    }

    public void showRatingPopup(View view) {
        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.rate_dialog);
        dialog.setTitle("My Preference");

        ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar);

        Button btnOk = (Button) dialog.findViewById(R.id.ok);
        Button btnCancel = (Button) dialog.findViewById(R.id.cancel);
        dialog.show();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ratingValue = String.valueOf(ratingBar.getRating());
                Toast.makeText(getApplicationContext(), "Rate: " + ratingValue, Toast.LENGTH_LONG).show();
                dialog.dismiss();
                new RateVendorAsyncTask().execute(ratingBar.getRating());
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
        } else if (requestCode == PICK_IMAGE_UPLOAD_ALBUM_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
//                imageView.setImageBitmap(bitmap);
//                new ImageUploaderAsyncTask().execute();
                itemArray.add(new Item("", bitmap));
                gridView.setAdapter(new MyAdapter(this.getApplicationContext(), itemArray));

                new PutAlbumAsyncTask().execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public String getPath(Uri uri) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        return true;
    }

    class ImageUploaderAsyncTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            AmazonS3 s3client = new AmazonS3Client(awsCreds);
            s3client.putObject(new PutObjectRequest(bucketName, "vendor/profile/" + username + "/profile.jpeg", new File(getPath(filePath))));
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

    class PutAlbumAsyncTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            AmazonS3 s3client = new AmazonS3Client(awsCreds);
            s3client.putObject(new PutObjectRequest(bucketName, "vendor/album/" + username + "/pic" + System.currentTimeMillis() + ".jpeg", new File(getPath(filePath))));
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

    private boolean requestServer(String relativeUrl, String json) {

        try {
            StringBuilder sb = new StringBuilder();
            HttpURLConnection urlConnection = null;

            URL url = new URL(SERVER_ADDRESS + relativeUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setDoOutput(true);

            urlConnection.connect();

            System.out.println("Json ");
            System.out.println(json);
            Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            writer.write(json);
            writer.close();


            int HttpResult = urlConnection.getResponseCode();
            System.out.println(urlConnection.getResponseMessage());
            System.out.println(HttpResult);
            System.out.println(urlConnection.getRequestMethod());
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();

                System.out.println("" + sb.toString());

                if (sb.toString().equals("Success")) {
                    return true;
                } else
                    return false;

            } else {
                System.out.println(urlConnection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }

    class GetProfileImageAsyncTask extends AsyncTask<String, String, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            AmazonS3 s3client = new AmazonS3Client(awsCreds);
            try {
                S3ObjectInputStream content = s3client.getObject(bucketName, "vendor/profile/" + username + "/profile.jpeg").getObjectContent();
                byte[] bytes = IOUtils.toByteArray(content);
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Bitmap success) {

            if (success != null) {
                imageView.setImageBitmap(success);
            } else {
                Toast.makeText(getApplicationContext(), "Please Choose Profile Picture", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class GetAlbumAsyncTask extends AsyncTask<String, String, ArrayList<Bitmap>> {

        @Override
        protected ArrayList<Bitmap> doInBackground(String... params) {
            AmazonS3 s3client = new AmazonS3Client(awsCreds);
            ArrayList<Bitmap> bitArray = new ArrayList<Bitmap>();
            try {
                ObjectListing listing = s3client.listObjects(bucketName, "vendor/album/" + username);
                GetObjectRequest request;
                for (S3ObjectSummary objectSummary : listing.getObjectSummaries()) {
                    System.out.println(objectSummary.getKey());
                    request = new GetObjectRequest(bucketName, objectSummary.getKey());
                    S3ObjectInputStream content = s3client.getObject(request).getObjectContent();
                    byte[] bytes = IOUtils.toByteArray(content);
                    bitArray.add(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                }
                return bitArray;
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Please Upload Your Work", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final ArrayList<Bitmap> success) {

            if (success != null) {
                for (int i = 0; i < success.size(); i++) {
                    itemArray.add(new Item("", success.get(i)));
                }
                gridView.setAdapter(new MyAdapter(getApplicationContext(), itemArray));

            }
        }
    }

    class RateVendorAsyncTask extends AsyncTask<Float, String, Boolean> {

        @Override
        protected Boolean doInBackground(Float... params) {
            try {
                StringBuilder sb = new StringBuilder();
                HttpURLConnection urlConnection = null;

                URL url = new URL(Server.ADDRESS + "rest/planner/rateVendor");

                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection.setDoOutput(true);

                urlConnection.connect();

                Rating rating = new Rating();
                rating.setUsername(username);
                rating.setRating(params[0]);
                Gson gson = new Gson();
                String json = gson.toJson(rating);
                System.out.println("RATING");
                System.out.println(json);

                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(json);
                writer.close();

                int HttpResult = urlConnection.getResponseCode();
                System.out.println(urlConnection.getResponseMessage());
                System.out.println(HttpResult);
                System.out.println(urlConnection.getRequestMethod());
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();

                    System.out.println("" + sb.toString());

                    if (sb.toString().equals("Success"))
                        return true;
                    else
                        return false;

                } else {
                    System.out.println(urlConnection.getResponseMessage());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                Toast.makeText(getApplicationContext(), "Rating posted", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
