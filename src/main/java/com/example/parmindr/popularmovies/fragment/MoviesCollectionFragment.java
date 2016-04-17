package com.example.parmindr.popularmovies.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.parmindr.popularmovies.BuildConfig;
import com.example.parmindr.popularmovies.R;
import com.example.parmindr.popularmovies.activity.MovieDetailsActivity;
import com.example.parmindr.popularmovies.activity.SettingsActivity;

import com.example.parmindr.popularmovies.adapter.MoviesAdapter;
import com.example.parmindr.popularmovies.data.MovieListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MoviesCollectionFragment extends Fragment {

    private MoviesAdapter moviesAdapter;

    private List<MovieListItem> movieListItems;

    private static String MOVIE_LIST_BUNDLE_KEY = "movie_list";

    public static String MOVIE_LIST_ITEM_INTENT_KEY = "movie_list_item";

    private final String LOG_TAG = this.getClass().getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movies_collection, container, false);
        GridView moviesGridView = (GridView) rootView.findViewById(R.id.grid_view_movies_collection);
        moviesGridView.setAdapter(moviesAdapter);
        moviesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailsActivityIntent = new Intent(getActivity(), MovieDetailsActivity.class);
                detailsActivityIntent.putExtra(MOVIE_LIST_ITEM_INTENT_KEY, moviesAdapter.getItem(position));
                startActivity(detailsActivityIntent);
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        moviesAdapter = createMoviesAdapter();
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        bindDataToAdapter();
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_activity, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private MoviesAdapter createMoviesAdapter() {
        return new MoviesAdapter(getActivity());
    }

    private void bindDataToAdapter() {
        String sortKey = getSortKey();
        Log.d(LOG_TAG, "Sorting by : " + sortKey);
        new FetchMovieDataTask().execute(sortKey);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        ArrayList<MovieListItem> movieListItems = new ArrayList<>();
        movieListItems.addAll(this.movieListItems);
        outState.putParcelableArrayList(MOVIE_LIST_BUNDLE_KEY, movieListItems);
        super.onSaveInstanceState(outState);
    }

    private String getSortKey() {
        return PreferenceManager.getDefaultSharedPreferences(
                getActivity())
                .getString(getString(R.string.pref_sort_key), "");

    }


    public class FetchMovieDataTask extends AsyncTask<String, Void, List<MovieListItem>> {

        private final String LOG_TAG = this.getClass().getSimpleName();
        private final static String POPULAR_MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/popular";
        private final static String TOP_RATED_MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/top_rated";
        private final static String MOVIE_POSTER_BASE_URL = "http://image.tmdb.org/t/p/w780";
        private final static String API_KEY = "api_key";
        private final static String REQUEST_METHOD_GET = "GET";

        @Override
        protected List<MovieListItem> doInBackground(String[] params) {

            HttpURLConnection connection = null;
            BufferedReader bufferedReader = null;
            List<MovieListItem> movieListItems = null;

            try {

                if(params[0].equals(getString(R.string.pref_sort_highest_rated_key))){
                    connection = getMovieDBConnection(TOP_RATED_MOVIE_BASE_URL);
                } else {
                    connection = getMovieDBConnection(POPULAR_MOVIE_BASE_URL);
                }
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String movieDataJsonString = bufferedReader.readLine();
                Log.d(LOG_TAG, movieDataJsonString);
                movieListItems = createMovieListItems(getMovieDataJsonArray(movieDataJsonString));
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error fetching movie data", e);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error closing the reader", e);
                }
            }
            return movieListItems;
        }

        private HttpURLConnection getMovieDBConnection(String baseUrl) {
            Uri discoveryUri = Uri.parse(baseUrl).buildUpon()
                    .appendQueryParameter(API_KEY, BuildConfig.MOVIE_DB_API_KEY)
                    .build();
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL(discoveryUri.toString()).openConnection();
                connection.setRequestMethod(REQUEST_METHOD_GET);
                connection.connect();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error creating connection to movie DB", e);
            }
            return connection;
        }

        private JSONArray getMovieDataJsonArray(String movieDataJsonString) {
            JSONArray movieDataList = null;
            try {
                JSONObject movieDataJsonObject = new JSONObject(movieDataJsonString);
                movieDataList = movieDataJsonObject.getJSONArray("results");
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error while parsing movie data JSON string", e);
            }
            return movieDataList;
        }

        private List<MovieListItem> createMovieListItems(JSONArray movieDetailsList) {
            List<MovieListItem> movieListItems = new ArrayList<>();
            for (int i = 0; i < movieDetailsList.length(); i++) {
                try {
                    movieListItems.add(createMovieListItem(movieDetailsList.getJSONObject(i)));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Error retrieving movie details JSONObject from the JSONArray", e);
                }
            }
            return movieListItems;
        }

        private MovieListItem createMovieListItem(JSONObject movieDetails) {
            MovieListItem movieListItem = new MovieListItem();
            movieListItem.setTitle(getTitle(movieDetails));
            movieListItem.setPosterUri(getPosterUri(movieDetails));
            movieListItem.setPlotSynopsis(getPlotSynopsis(movieDetails));
            movieListItem.setRelaseDate(getReleaseDate(movieDetails));
            movieListItem.setViewerRating(getViewerRating(movieDetails));
            Log.d(LOG_TAG, "Movie Data: " + movieListItem.toString());
            return movieListItem;
        }

        private String getTitle(JSONObject movieDetails) {
            String title = null;
            try {
                title = movieDetails.getString("original_title");
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error parsing movie name from the JSONObject", e);
            }
            return title;
        }

        private Uri getPosterUri(JSONObject movieDetails) {
            Uri posterUri = null;
            try {
                String posterPath = movieDetails.getString("poster_path").replaceAll("\\/", "");
                posterUri = Uri.parse(MOVIE_POSTER_BASE_URL).buildUpon().appendPath(posterPath).build();
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error parsing movie poster path from the JSONObject", e);
            }
            return posterUri;
        }

        private String getPlotSynopsis(JSONObject movieDetails) {
            String plotSynopsis = null;
            try {
                plotSynopsis = movieDetails.getString("overview");
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error parsing movie name from the JSONObject", e);
            }
            return plotSynopsis;
        }

        private String getReleaseDate(JSONObject movieDetails) {
            String releaseDate = null;
            try {
                releaseDate = movieDetails.getString("release_date");
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error parsing movie name from the JSONObject", e);
            }
            return releaseDate;
        }

        private String getViewerRating(JSONObject movieDetails) {
            String rating = null;
            try {
                rating = movieDetails.getString("vote_average");
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error parsing movie rating from the JSONObject", e);
            }
            return rating;
        }

        @Override
        protected void onPostExecute(List<MovieListItem> movieListItems) {
            moviesAdapter.clear();
            moviesAdapter.addAll(movieListItems);
            MoviesCollectionFragment.this.movieListItems = movieListItems;
        }
    }
}
