package com.hometech.expandabledemo;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends Activity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    private ProgressDialog mprocessingdialog;
    private static String url = "http://www.androidbegin.com/tutorial/jsonparsetutorial.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mprocessingdialog = new ProgressDialog(this);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);


        new DownloadJason().execute();

        // preparing list data
//        prepareListData();


    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }


    private class DownloadJason extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mprocessingdialog.setTitle("Please Wait..");
            mprocessingdialog.setMessage("Loading");
            mprocessingdialog.setIndeterminate(false);
            mprocessingdialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            listDataHeader = new ArrayList<String>();
            JSONParser jp = new JSONParser();
            String jsonstr = jp.makeServiceCall(url);
            Log.d("Json url view", jsonstr);

            if (jsonstr != null) {

                listDataHeader = new ArrayList<String>();
                listDataChild = new HashMap<String, List<String>>();

                try {
                    JSONObject jobj = new JSONObject(jsonstr);
                    JSONArray jarray = jobj.getJSONArray("worldpopulation");
                    for (int hk = 0; hk < jarray.length(); hk++) {
                        JSONObject d = jarray.getJSONObject(hk);
                        // Adding Header data

                        listDataHeader.add(d.getString("country"));
                        // Adding child data for lease offer
                        List<String> lease_offer = new ArrayList<String>();

                        lease_offer.add(d.getString("country") + "'s Rank is : "
                                + d.getString("rank"));
                        lease_offer.add("And Population is "+d.getString("population"));
                        // Header into Child data
                        listDataChild.put(listDataHeader.get(hk), lease_offer);

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(),
                        "Please Check internet Connection", Toast.LENGTH_SHORT)
                        .show();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            mprocessingdialog.dismiss();

            listAdapter = new ExpandableListAdapter(getApplicationContext(), listDataHeader, listDataChild);

            // setting list adapter
            expListView.setAdapter(listAdapter);

        }
    }

   /*  public class GetData extends AsyncTask<Void,Void,Void> {

             @Override
             protected void onPreExecute() {
                 super.onPreExecute();

             }

             @Override
             protected Void doInBackground(Void... params) {

                 OkHttpClient client = new OkHttpClient();
                 RequestBody formBody = new FormBody.Builder()
                         .add("subcategoryid", "1")
                         .build();
                 Request request = new Request.Builder()
                         .url("http://skmuthaskinclinic.com/JobscheduleApp/API/view_questionbysubcate_id.php")
                         .post(formBody)
                         .build();
                 try {
                     Response response = client.newCall(request).execute();
                     String responseString = response.body().string();
                     response.body().close();

                     Log.i("Response", "doInBackground: ");
                 } catch (Exception e) {
                 }

                 return null;
             }

             @Override
             protected void onPostExecute(Void aVoid) {
                 super.onPostExecute(aVoid);

             }

         }*/


}