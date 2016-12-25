package com.parse.anyphone;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.eventplanner.models.Address;
import com.eventplanner.models.ConfirmedEvent;
import com.eventplanner.models.PendingEventsSearch;
import com.eventplanner.models.Server;
import com.google.gson.Gson;

import org.json.JSONArray;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PendingEventActivity extends AppCompatActivity {

    private TextView mUserNametextView;
    private TextView mPhoneNumbertextView;
    private TextView mBudgettextView;
    private ImageView imageView;
    private ListView listView;
    private String user_type;

    private String username;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_be_confirmed_event);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Pending Events");

        mUserNametextView = (TextView) findViewById(R.id.userNametextView);
        mPhoneNumbertextView = (TextView) findViewById(R.id.phoneNumbertextView);
        mBudgettextView = (TextView) findViewById(R.id.budgettextView);
        imageView = (ImageView) findViewById(R.id.imageView);
        listView = (ListView) findViewById(R.id.listView);
        progress = new ProgressDialog(this);

        Intent intent = getIntent();
        String firstName = intent.getStringExtra("FIRST_NAME");
        String lastName = intent.getStringExtra("LAST_NAME");
        String phoneNumber = intent.getStringExtra("PHONE_NUMBER");
        String budget = null;
        username = intent.getStringExtra("USERNAME");
        user_type = intent.getStringExtra("USER_TYPE");
//        Bitmap _bitmap = BitmapFactory.decodeByteArray(
//                getIntent().getByteArrayExtra("byteArray"), 0, getIntent().getByteArrayExtra("byteArray").length);
        try {
            budget = intent.getStringExtra("BUDGET");
        } catch (Exception e) {
            System.out.println(e);
        }

        mUserNametextView.setText("Name: " + firstName + " " + lastName);
        mPhoneNumbertextView.setText("PH: " + phoneNumber);
        mBudgettextView.setText(budget);
//        imageView.setImageBitmap(_bitmap);

        new ConfirmEventAsyncTask().execute();
    }

    class ConfirmEventAsyncTask extends AsyncTask<String, String, String> {

        private ToBeConfirmedAdapter mAdapter;

        @Override
        protected String doInBackground(String... voids) {
            return requestServer();
        }

        @Override
        protected void onPostExecute(final String json) {

            if (json != null) {
                System.out.println(json);
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                ArrayList<PendingEventsSearch> results = parseJSONtoPOJO(json);

                mAdapter = new ToBeConfirmedAdapter(results);
                listView.setAdapter(mAdapter);
            } else {
                Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
            }
        }

        private ArrayList<PendingEventsSearch> parseJSONtoPOJO(String jsonStr) {
            JSONArray jsonarray = null;
            try {
                jsonarray = new JSONArray(jsonStr);
                ArrayList<PendingEventsSearch> results = new ArrayList<>(); //RecommendationResults[jsonarray.length()];
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    PendingEventsSearch result = new PendingEventsSearch();
                    result.setUser(jsonobject.getString("user"));
                    result.setVendor(jsonobject.getString("vendor"));
//                    result.setAddress(jsonobject.getJSONObject("price"));
                    result.setDate(jsonobject.getString("date"));
                    results.add(result);
                }
                return results;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    class BookConfirmAsyncTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
//            boolean isSuccess = false;
//                return requestServer("rest/planner/userRegisterWedding", params[0]);
            return requestServer("rest/planner/confirmEventVendor", params[0]);

//            return isSuccess;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
//            showProgress(false);
            progress.dismiss();
            if (success) {
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            } else {
                Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String requestServer() {

        try {
            StringBuilder sb = new StringBuilder();
            HttpURLConnection urlConnection = null;
            URL url = null;
            if (user_type.equals("vendor")) {
                url = new URL(Server.ADDRESS + "rest/planner/vendorPendingRequest/" + username);
            } else {
                url = new URL(Server.ADDRESS + "rest/planner/userAwaitingApproval/" + username);
            }

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            urlConnection.connect();


            int HttpResult = urlConnection.getResponseCode();
            System.out.println(urlConnection.getResponseMessage());
            System.out.println(HttpResult);
            System.out.println(urlConnection.getRequestMethod());
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();

                if (sb.length() > 0)
                    return sb.toString();
                else
                    return null;

            } else {
                System.out.println(urlConnection.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean requestServer(String relativeUrl, String json) {

        try {
            StringBuilder sb = new StringBuilder();
            HttpURLConnection urlConnection = null;

            URL url = new URL(Server.ADDRESS + relativeUrl);

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

    class ToBeConfirmedAdapter extends BaseAdapter {
        private List<PendingEventsSearch> results = null;
        String[] Title, Detail;
        int[] imge;
        private String artist;
        private String eventDate;

        public ToBeConfirmedAdapter(ArrayList<PendingEventsSearch> results) {
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

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View row;
            row = inflater.inflate(R.layout.reco_list_item, parent, false);
            final TextView name, date;
            ImageView catImage;
            name = (TextView) row.findViewById(R.id.title);
            date = (TextView) row.findViewById(R.id.detail);
            TextView rating = (TextView) row.findViewById(R.id.rating);
            catImage = (ImageView) row.findViewById(R.id.imageView);
            final Button bookButton = (Button) row.findViewById(R.id.RegisterWeddingbutton);
//            name.setText(Title[position]);
//            date.setText(Detail[position]);
//            catImage.setImageResource(imge[position]);
            rating.setVisibility(View.GONE);
//            catImage.setVisibility(View.GONE);

            if (results != null && results.get(position) != null) {
                artist = results.get(position).getVendor();
                eventDate = results.get(position).getDate();
                Date readableDate = new Date(Long.parseLong(results.get(position).getDate()));
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                date.setText(df.format(readableDate));

                if(user_type.equalsIgnoreCase("customer")) {
                    bookButton.setVisibility(View.GONE);
                    name.setText(artist);
                    if (user_type.equalsIgnoreCase(getResources().getString(R.string.photographer)))
                        catImage.setImageResource(R.drawable.ic_photographer2);
                    else if (user_type.equalsIgnoreCase(getResources().getString(R.string.florist)))
                        catImage.setImageResource(R.drawable.ic_florist2);
                    else if (user_type.equalsIgnoreCase(getResources().getString(R.string.artist)))
                        catImage.setImageResource(R.drawable.ic_makeup);
                    else
                        catImage.setImageResource(R.drawable.ic_photographer2);
                }
                else {
                    name.setText(results.get(position).getUser());
                    bookButton.setText("Confirm");
                    catImage.setVisibility(View.GONE);
                }
//                rating.setText("Rating: " + results.get(position).getRating());
//                if (results.get(position).getCategory().equalsIgnoreCase(getResources().getString(R.string.photographer)))
//                    catImage.setImageResource(R.drawable.nobody_m);
//                else if (results.get(position).getCategory().equalsIgnoreCase(getResources().getString(R.string.florist)))
//                    catImage.setImageResource(R.drawable.lock);
//                else if (results.get(position).getCategory().equalsIgnoreCase(getResources().getString(R.string.artist)))
//                    catImage.setImageResource(R.drawable.logo);
            }

            bookButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progress.show();
                    bookButton.setClickable(false);
                    ConfirmedEvent confimredEvent = new ConfirmedEvent();
                  confimredEvent.setUsr(name.getText().toString());//Usr(artist);
                    confimredEvent.setArtist(username);
                    if(user_type.equalsIgnoreCase("vendor"))
                        confimredEvent.setCategory("photographer");
                    else
                        confimredEvent.setCategory(user_type.toLowerCase());
                    confimredEvent.setDate(eventDate);
                    Address address = new Address();
                    address.setCity("New York");
                    address.setState("New York");
                    address.setStreet("Ocean PKWY");
                    address.setZip("11235");
                    confimredEvent.setVenue(address);

                    Gson gson = new Gson();
                    String json = gson.toJson(confimredEvent);
                    System.out.println("BOOK_EVENT");
                    System.out.println(json);
                    new BookConfirmAsyncTask().execute(json, null, null);
                }
            });

            return (row);
        }
    }
}
