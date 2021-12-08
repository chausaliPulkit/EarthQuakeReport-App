
package com.example.quakereport;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<Data>> {
    /*
     * Constant value for Earthquake Loader ID(can choose any integer)
     * comes into play if use multiple Loader
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;

    /**
     * URL for earthquake data from the USGS dataset
     */
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&" +
                    "minmag=3&&maxmag=7&limit=100";
    /**
     * Adapter for the list of earthquakes
     */
    private EarthquakeCustomAdapter adapter;
    private TextView empty_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);


        if (isConnected(this)) {
            //  get reference to loader manager to interact with loader
            LoaderManager loaderManager = getLoaderManager();
            Log.d("debugging", "initLoader execute");
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        } else {
            View progressBar = findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);
            empty_view = findViewById(R.id.emptyTextView);
//            set emptyText View to show no internet connection message
            empty_view.setText(R.string.no_internet_connection);
        }


        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = findViewById(R.id.list);
        empty_view = findViewById(R.id.emptyTextView);
        earthquakeListView.setEmptyView(empty_view);


        // Create a new adapter that takes an empty list of earthquakes as input
        adapter = new EarthquakeCustomAdapter(this, new ArrayList<Data>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.


        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Find the current earthquake that was clicked on
                Data currentEarthquake = (Data) adapter.getItem(i);
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthQuakeuri = Uri.parse(currentEarthquake.getUrl());
                // Create a new intent to view the earthquake URI
                Intent intent = new Intent(Intent.ACTION_VIEW, earthQuakeuri);
                // Send the intent to launch a new activity
                startActivity(intent);

            }
        });
    }

    @NonNull
    @Override
    public Loader<List<Data>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d("debugging", "onCreate Loader execute");
        // Create a new Loader for given URL
        return new EarthquakeLoader(this, USGS_REQUEST_URL);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Data>> loader, List<Data> data) {
        //  hide progress bar because data is loaded
        View progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

//        set empty_view(TextView) to show No earthquake data found
        empty_view.setText(R.string.no_earthquake);
        // Clear the adapter of previous earthquake data
        adapter.clear();

        if (!isConnected(this)) {
            empty_view.setText(R.string.no_internet_connection);
        }

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        }

        Log.d("debugging", "called onLoadFinished");
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Data>> loader) {
        Log.d("debugging", "calling onLoadReset");
        // Since Loader reset therefore we clear out our existing data
        adapter.clear();
    }

    /**
     * Loads a list of earthquakes by using an AsyncTask to perform the
     * network request to the given URL.
     */
    private static class EarthquakeLoader extends AsyncTaskLoader<List<Data>> {
        // tag for log messages
        private final String LOG_TAG = EarthquakeLoader.class.getName();

        // Query Url
        private final String mUrl;
        private final Context context;

        /**
         * Constructs a new {@link EarthquakeLoader}.
         *
         * @param context of the activity
         * @param url     to load data from
         */
        public EarthquakeLoader(Context context, String url) {
            super(context);
            mUrl = url;
            this.context = context;
        }

        @Override
        protected void onStartLoading() {
            Log.d("debugging", "onStartLoading method called");
            forceLoad();
        }

        // this method work on background thread
        @Override
        public List<Data> loadInBackground() {
            Log.d("debugging", "loadInBackground execute");
            if (mUrl == null || !EarthquakeActivity.isConnected(context)) {
                return null;
            }
            /* This will fetch the earthquake details from network in background thread
               and return List of earthquake data
             */

            return QueryUtils.fetchEarthquakeData(mUrl);
        }
    }


    public static boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }


}




