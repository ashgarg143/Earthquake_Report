
package com.example.ashis.earthquake;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.earthquake.R;

import java.util.ArrayList;
import java.util.List;


public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    private static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&minmagnitude=3.1&orderby=time&limit=15";


    private static EarthquakeAdapter adapter;
    private TextView textview;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "TEST : onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null) {
            progressDialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Connection Unavailable.");
            builder.setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    startActivity(intent);

                }
            });
            builder.create();
            builder.show();

        }



        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = findViewById(R.id.list_view);
        textview = findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(textview);
        // Create a new {@link ArrayAdapter} of earthquakes
        adapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Earthquake currentEarthquake = adapter.getItem(position);
                String url = currentEarthquake.getUrl();
                Uri earthquakeUri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
                startActivity(intent);
            }
        });
        Log.i(LOG_TAG, "TEST : initLoader called: ");


        getLoaderManager().initLoader(1, null, this).forceLoad();

        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLoaderManager().initLoader(1, null, EarthquakeActivity.this).forceLoad();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "TEST : onCreateLoader called: ");

        return new EarthquakeLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquake) {
        Log.i(LOG_TAG, "TEST : onLoaderFinished: ");
        textview.setText("No earthquake Found !!");
        // adapter.clear();

        if (earthquake != null && !earthquake.isEmpty() && adapter.isEmpty()) {
            progressDialog.dismiss();
            adapter.addAll(earthquake);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        Log.i(LOG_TAG, "TEST : onLoaderFinished: ");

        adapter.clear();
    }

    private static class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

        private String URL;

        EarthquakeLoader(Context context, String url) {
            super(context);
            URL = url;
        }


        @Override
        protected void onStartLoading() {
            Log.i(LOG_TAG, "TEST : onStartLoading: ");

            forceLoad();
        }

        @Override
        public List<Earthquake> loadInBackground() {
            Log.i(LOG_TAG, "TEST : loadInBackground: ");

            if (URL == null)
                return null;

            return QueryUtils.fetchEarthquakeData(URL);
        }
    }


}
