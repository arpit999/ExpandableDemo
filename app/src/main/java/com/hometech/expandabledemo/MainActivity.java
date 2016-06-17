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

        // preparing list data
        new DownloadJason().execute();


    }

    /*
     * Preparing the list data
     */

    private class DownloadJason extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

//            Showing Progress dialog

            mprocessingdialog.setTitle("Please Wait..");
            mprocessingdialog.setMessage("Loading");
            mprocessingdialog.setCancelable(false);
            mprocessingdialog.setIndeterminate(false);
            mprocessingdialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub

            JSONParser jp = new JSONParser();
            String jsonstr = jp.makeServiceCall(url);
            Log.d("Response = ", jsonstr);

            if (jsonstr != null) {
//            For Header title Arraylist
                listDataHeader = new ArrayList<String>();
//                Hashmap for child data key = header title and value = Arraylist (child data)
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
                        lease_offer.add("And Population is " + d.getString("population"));

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
//call constructor
            listAdapter = new ExpandableListAdapter(getApplicationContext(), listDataHeader, listDataChild);

            // setting list adapter
            expListView.setAdapter(listAdapter);

        }
    }

}