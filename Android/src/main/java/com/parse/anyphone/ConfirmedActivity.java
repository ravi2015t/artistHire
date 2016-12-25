package com.parse.anyphone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.eventplanner.models.MapConfirmedEvent;
import com.eventplanner.models.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConfirmedActivity extends AppCompatActivity {

    BasicAWSCredentials awsCreds = new BasicAWSCredentials("", "");
    private String bucketName = "eventplanner";

    private TextView mUserNametextView;
    private TextView mPhoneNumbertextView;
    private TextView mBudgettextView;
    private ImageView imageView;
    private ListView listView;
    private String user_type;

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String budget;

    private ConfirmationAdapter mAdapter;
    ArrayList<MapConfirmedEvent> results;

    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Confirmed Events");

        mUserNametextView = (TextView) findViewById(R.id.userNametextView);
        mPhoneNumbertextView = (TextView) findViewById(R.id.phoneNumbertextView);
        mBudgettextView = (TextView) findViewById(R.id.budgettextView);
        imageView = (ImageView) findViewById(R.id.imageView);
        listView = (ListView) findViewById(R.id.listView);


        new GetProfileImageAsyncTask().execute();

        Intent intent = getIntent();
        firstName = intent.getStringExtra("FIRST_NAME");
        lastName = intent.getStringExtra("LAST_NAME");
        phoneNumber = intent.getStringExtra("PHONE_NUMBER");
        budget = null;
        username = intent.getStringExtra("USERNAME");
        user_type = intent.getStringExtra("USER_TYPE");
        try {
            budget = intent.getStringExtra("BUDGET");
        } catch (Exception e) {
            System.out.println(e);
        }
        if(user_type.equalsIgnoreCase("customer"))
            mBudgettextView.setVisibility(View.GONE);
        else
            mBudgettextView.setVisibility(View.VISIBLE);

        mUserNametextView.setText("Name: " + firstName + " " + lastName);
        mPhoneNumbertextView.setText("PH: " + phoneNumber);
        mBudgettextView.setText(budget);

        new ConfirmEventAsyncTask().execute();
    }


    class ConfirmationAdapter extends BaseAdapter {
        private List<MapConfirmedEvent> results = null;
        String[] Title, Detail;
        int[] imge;

        ConfirmationAdapter() {
            Title = null;
            Detail = null;
            imge = null;
        }



        public ConfirmationAdapter(ArrayList<MapConfirmedEvent> results) {
            this.results = results;

        }

        public int getCount() {
            // TODO Auto-generated method stub
            return results.size();
        }

        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

//        result.setUser(jsonobject.getString("usr"));
//        result.setVendor(jsonobject.getString("artist"));
//        result.setDate(jsonobject.getString("date"));
//        result.setCategory(jsonobject.getString("category"));

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View row;
            row = inflater.inflate(R.layout.reco_list_item, parent, false);
            final TextView vendorName, date;
            ImageView catImage;
            vendorName = (TextView) row.findViewById(R.id.title);
            date = (TextView) row.findViewById(R.id.detail);
            TextView rating = (TextView) row.findViewById(R.id.rating);
            catImage = (ImageView) row.findViewById(R.id.imageView);
            final Button bookButton = (Button) row.findViewById(R.id.RegisterWeddingbutton);

            rating.setVisibility(View.GONE);
            bookButton.setVisibility(View.GONE);
            Date readableDate = new Date(Long.parseLong(results.get(position).getDate()));
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            date.setText(df.format(readableDate));

            if (results != null && results.get(position) != null) {
                if(user_type.equals("customer")) {
                    vendorName.setText(results.get(position).getArtist());
                    if (results.get(position).getCategory().equalsIgnoreCase(getResources().getString(R.string.photographer)))
                        catImage.setImageResource(R.drawable.ic_photographer2);
                    else if (results.get(position).getCategory().equalsIgnoreCase(getResources().getString(R.string.florist)))
                        catImage.setImageResource(R.drawable.ic_florist2);
                    else if (results.get(position).getCategory().equalsIgnoreCase(getResources().getString(R.string.artist)))
                        catImage.setImageResource(R.drawable.ic_makeup);
                }
                else {
                    vendorName.setText(results.get(position).getUsr());
                    catImage.setVisibility(View.GONE);
                }
            }
            return (row);
        }
    }

    class ConfirmEventAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... voids) {
            return requestServer();
        }

        @Override
        protected void onPostExecute(final String json) {

            if (json != null) {
                System.out.println(json);
                results = parseJSONtoPOJO(json);
                mAdapter = new ConfirmationAdapter(results);
                listView.setAdapter(mAdapter);
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private ArrayList<MapConfirmedEvent> parseJSONtoPOJO(String jsonStr) {
        JSONArray jsonarray = null;
        try {
            jsonarray = new JSONArray(jsonStr);
            ArrayList<MapConfirmedEvent> results = new ArrayList<>(); //RecommendationResults[jsonarray.length()];
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                MapConfirmedEvent result = new MapConfirmedEvent();
                result.setUser(jsonobject.getString("usr"));
                result.setVendor(jsonobject.getString("artist"));
                result.setDate(jsonobject.getString("date"));
                result.setCategory(jsonobject.getString("category"));
                results.add(result);
            }
            return results;
        } catch (JSONException e) {
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
                S3ObjectInputStream content = null;
                if(user_type.equals("costumer")) {
                    content = s3client.getObject(bucketName, "usr/profile/" + username + "/profile.jpeg").getObjectContent();
                }else{
                    content = s3client.getObject(bucketName, "vendor/profile/" + username + "/profile.jpeg").getObjectContent();
                }
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
            }
        }
    }

    private String requestServer() {

        try {
            StringBuilder sb = new StringBuilder();
            HttpURLConnection urlConnection = null;
            URL url = null;
            if(user_type.equalsIgnoreCase("customer")){
                url = new URL(Server.ADDRESS + "rest/planner/userConfirmedEvents/" + username);
            }else{
                url = new URL(Server.ADDRESS + "rest/planner/vendorConfirmedEvents/" + username);
            }

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            urlConnection.connect();


            int HttpResult = urlConnection.getResponseCode();
            System.out.println(url);
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
}
