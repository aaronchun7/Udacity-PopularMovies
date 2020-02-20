package android.example.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.example.popularmovies.utilities.NetworkUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieItemClickListener, AdapterView.OnItemSelectedListener {

    private String[] mApiParams;

    private static int mPageNumber;
    private static int mTotalPageNumber;
    private static String mSortType;
    private static ArrayList<Integer> mPageArray;
    private static MovieAdapter mMovieAdapter;
    public static int mThumbnailWidth;

    @SuppressLint("StaticFieldLeak")
    private static ProgressBar mProgressBar;

    @SuppressLint("StaticFieldLeak")
    private static TextView mErrorTextView;
    private Spinner mSortBySpinner;

    @SuppressLint("StaticFieldLeak")
    private static Spinner mPageSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPageSpinner = findViewById(R.id.spinner_page);
        mSortBySpinner = findViewById(R.id.spinner_sort_by);
        RecyclerView mMovieList = findViewById(R.id.rv_movies);
        mProgressBar = findViewById(R.id.pb_movie_list);
        mErrorTextView = findViewById(R.id.tv_error_message);

        if (savedInstanceState != null) {
            mPageArray = savedInstanceState.getIntegerArrayList("pageArrayList");
            if (mPageArray != null) {
                ArrayAdapter<Integer> pageSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mPageArray);
                mPageSpinner.setAdapter(pageSpinnerAdapter);
            }
            mPageSpinner.setSelection(savedInstanceState.getInt("pageSpinner", 0));
            mSortBySpinner.setSelection(savedInstanceState.getInt("sortBySpinner", 0));

        }

        mPageSpinner.setOnItemSelectedListener(this);
        mSortBySpinner.setOnItemSelectedListener(this);

        // Get screen width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenPixelWidth = displayMetrics.widthPixels;

        int gridWidthSpan;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            gridWidthSpan = 3;
        } else {
            gridWidthSpan = 5;
        }

        mThumbnailWidth = screenPixelWidth/gridWidthSpan;

        GridLayoutManager layoutManager = new GridLayoutManager(this,gridWidthSpan, GridLayoutManager.VERTICAL, false);
        mMovieList.setLayoutManager(layoutManager);
        mMovieList.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);
        mMovieList.setAdapter(mMovieAdapter);

        if (mPageNumber == 0 || mPageNumber > mTotalPageNumber) {
            mPageNumber = 1;
            mPageSpinner.setSelection(0);
        }

        if (mSortType == null) {
            mSortType = getResources().getString(R.string.popular_sort);
            mSortBySpinner.setSelection(0);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);

        bundle.putIntegerArrayList("pageArrayList", mPageArray);
        bundle.putInt("pageSpinner", mPageSpinner.getSelectedItemPosition());
        bundle.putInt("sortBySpinner", mSortBySpinner.getSelectedItemPosition());
    }

    @Override
    public void onMovieItemClick(String movieData) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, movieData);
        startActivity(intentToStartDetailActivity);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mMovieAdapter.setMovieListData(null);
        int selectedId = parent.getId();
        String mRegion = "";
        switch(selectedId) {
            case (R.id.spinner_page):
                mPageNumber = mPageArray.get(position);
                mApiParams = new String[]{
                        mSortType,
                        NetworkUtils.language,
                        String.valueOf(mPageNumber),
                        mRegion
                };
                break;
            case (R.id.spinner_sort_by):
                mSortType = mSortBySpinner.getItemAtPosition(position).toString();
                mApiParams = new String[]{
                        mSortType,
                        NetworkUtils.language,
                        String.valueOf(mPageNumber),
                        mRegion
                };
                break;
        }
        new FetchMoviesTask(this).execute(mApiParams);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }

    private static class FetchMoviesTask extends AsyncTask<String, Void, String[]> {

        @SuppressLint("StaticFieldLeak")
        final Context mContext;

        FetchMoviesTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mErrorTextView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... params) {

            if(params.length == 0) {
                return null;
            }

            String sort = params[0];
            String language = params[1];
            int pageNumber = Integer.parseInt(params[2]);
            String region = params[3];

            switch(sort){
                case "Popular":
                    URL popularUrl = NetworkUtils.getPopularMovies(language, pageNumber, region);

                    try {
                        String popularMovieJson = NetworkUtils.getResponseFromHttpsUrl(popularUrl);
                        return NetworkUtils.parseMovieListDataFromJson(popularMovieJson);
                    } catch (Exception pme) {
                        pme.printStackTrace();
                        return null;
                    }
                case "Top Rated":
                    URL topRatedUrl = NetworkUtils.getTopRatedMovies(language, pageNumber, region);

                    try {
                        String topRatedMovieJson = NetworkUtils.getResponseFromHttpsUrl(topRatedUrl);
                        return NetworkUtils.parseMovieListDataFromJson(topRatedMovieJson);
                    } catch (Exception pme) {
                        pme.printStackTrace();
                        return null;
                    }
                default:
                    break;
            }

            return new String[0];
        }

        @Override
        protected void onPostExecute(String[] movieListData) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if (movieListData != null) {
                mTotalPageNumber = Integer.parseInt(movieListData[2]);
                if (mPageArray == null) {
                    mPageArray = new ArrayList<>();
                }

                if (mPageArray.size() != mTotalPageNumber) {
                    mPageArray.clear();
                    for (int i = 1; i <= mTotalPageNumber; i++) {
                        mPageArray.add(i);
                    }
                    ArrayAdapter<Integer> pageSpinnerAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, mPageArray);
                    mPageSpinner.setAdapter(pageSpinnerAdapter);
                }
                mMovieAdapter.setMovieListData(NetworkUtils.parseMovieListFromJson(movieListData[3]));
            } else {
                mErrorTextView.setVisibility(View.VISIBLE);
            }
        }
    }
}
