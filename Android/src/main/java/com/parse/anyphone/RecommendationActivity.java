package com.parse.anyphone;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.eventplanner.models.RecommendationResults;
import com.eventplanner.models.Server;
import com.eventplanner.models.UserBookEvent;
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
import java.util.ArrayList;
import java.util.List;

public class RecommendationActivity extends Activity {
    private ListView mListView;
    private RecommendationAdapter mAdapter;
    private String userName;
    private String date;
    private ProgressDialog progress;
    private String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);
        mListView = (ListView) findViewById(R.id.my_list_view);

        Intent intentData = getIntent();
        json = intentData.getStringExtra("RECOS");
        userName = intentData.getStringExtra("USER_NAME");
        date = intentData.getStringExtra("DATE");
        progress = new ProgressDialog(this);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView

        // dummy data
        String[] vendorNames = {"Shashank", "Segu Shashank", "Shashank", "Shashank", "Shashank"};
        String[] price = {"25000", "88000", "25000", "125000", "2000"};
        int[] imgRes = {R.drawable.nobody_m, R.drawable.lock, R.drawable.logo, R.drawable.lock, R.drawable.logo};
    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<RecommendationResults> results = parseJSONtoPOJO(json);

        mAdapter = new RecommendationAdapter(results);
        mListView.setAdapter(mAdapter);
    }

    private ArrayList<RecommendationResults> parseJSONtoPOJO(String jsonStr) {
        JSONArray jsonarray = null;
        try {
            jsonarray = new JSONArray(jsonStr);
            ArrayList<RecommendationResults> results = new ArrayList<>(); //RecommendationResults[jsonarray.length()];
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                RecommendationResults result = new RecommendationResults();
                result.setUsername(jsonobject.getString("username"));
                result.setRating(jsonobject.getDouble("rating"));
                result.setPrice(jsonobject.getLong("price"));
                result.setCategory(jsonobject.getString("category"));
                results.add(result);
            }
            return results;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    class RecommendationAdapter extends BaseAdapter {
        private List<RecommendationResults> results = null;
        String[] Title, Detail;
        int[] imge;

        RecommendationAdapter() {
            Title = null;
            Detail = null;
            imge = null;
        }

        public RecommendationAdapter(String[] text, String[] text1, int[] text3) {
            Title = text;
            Detail = text1;
            imge = text3;

        }

        public RecommendationAdapter(ArrayList<RecommendationResults> results) {
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
            final TextView vendorName, price;
            ImageView catImage;
            vendorName = (TextView) row.findViewById(R.id.title);
            price = (TextView) row.findViewById(R.id.detail);
            TextView rating = (TextView) row.findViewById(R.id.rating);
            catImage = (ImageView) row.findViewById(R.id.imageView);
            final Button bookButton = (Button) row.findViewById(R.id.RegisterWeddingbutton);
//            vendorName.setText(Title[position]);
//            price.setText(Detail[position]);
//            catImage.setImageResource(imge[position]);

            if (results != null && results.get(position) != null) {
                vendorName.setText(results.get(position).getUsername());
                price.setText("Price: " + results.get(position).getPrice());
                String rate = String.valueOf(results.get(position).getRating());
                rating.setText("Rating: " + rate.substring(0, 3));
                if (results.get(position).getCategory().equalsIgnoreCase(getResources().getString(R.string.photographer)))
                    catImage.setImageResource(R.drawable.ic_photographer2);
                else if (results.get(position).getCategory().equalsIgnoreCase(getResources().getString(R.string.florist)))
                    catImage.setImageResource(R.drawable.ic_florist2);
                else if (results.get(position).getCategory().equalsIgnoreCase(getResources().getString(R.string.artist)))
                    catImage.setImageResource(R.drawable.ic_makeup);
            }

            bookButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progress.show();
                    bookButton.setClickable(false);
                    UserBookEvent bookEvent = new UserBookEvent();
                    long l = 1234;
                    bookEvent.setUsr(userName);
                    bookEvent.setVendor(vendorName.getText().toString());
                    bookEvent.setDate(date);
                    Address address = new Address();
                    address.setCity("New York");
                    address.setState("New York");
                    address.setStreet("Ocean PKWY");
                    address.setZip("11235");
                    bookEvent.setAddress(address);

                    Gson gson = new Gson();
                    String json = gson.toJson(bookEvent);
                    System.out.println("BOOK_EVENT");
                    System.out.println(json);
                    new BookAsyncTask().execute(json, null, null);
                }
            });

            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new GetProfileAsyncTask().execute(vendorName.getText().toString());
                }
            });

            return (row);
        }

        class BookAsyncTask extends AsyncTask<String, String, Boolean> {

            @Override
            protected Boolean doInBackground(String... params) {
                return requestServer("/rest/planner/userBookEvent", params[0]);
            }

            @Override
            protected void onPostExecute(final Boolean success) {
                if (success) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
                }
            }
        }

        class GetProfileAsyncTask extends AsyncTask<String, Void, JSONObject> {

            private String mEmail;

            @Override
            protected JSONObject doInBackground(String... params) {
                mEmail = params[0];
                return getProfile(params[0]);
            }

            @Override
            protected void onPostExecute(final JSONObject success) {
                Intent i;

                if (success != null) {
                    System.out.println("--------------------");
                    try {
                        i = new Intent(RecommendationActivity.this, VendorProfileActivity.class);

                        i.putExtra("BUDGET", success.getString("price"));
                        i.putExtra("FIRST_NAME", success.getString("firstname"));
                        i.putExtra("PHONE_NUMBER", success.getString("phoneNumber"));
                        i.putExtra("LAST_NAME", success.getString("lastname"));
                        i.putExtra("EMAIL", mEmail);
                        i.putExtra("IS_VISITOR", true);
                        startActivity(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        }

        private JSONObject getProfile(String email) {

            try {
                HttpURLConnection urlConnection = null;
                URL url = null;
                url = new URL(Server.ADDRESS + "rest/planner/vendorGetProfile/" + email);

                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "text/plain");

                urlConnection.connect();

                int HttpResult = urlConnection.getResponseCode();
                System.out.println(urlConnection.getResponseMessage());
                System.out.println(HttpResult);
                System.out.println(urlConnection.getRequestMethod());
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream(), "utf-8"));
                    String line = null;
                    StringBuffer sb = new StringBuffer();
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();

                    System.out.println("" + sb.toString());
                    try {
                        JSONObject jsonObj = new JSONObject(sb.toString());
                        return jsonObj;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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

    }
}