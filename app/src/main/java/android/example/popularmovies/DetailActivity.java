package android.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.example.popularmovies.model.Movie;
import android.example.popularmovies.model.Trailer;
import android.example.popularmovies.utilities.NetworkUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerItemClickListener, AdapterView.OnItemSelectedListener {

    private Movie mMovie;
    private ImageView mPoster;
    private TextView mReleaseDateDetail, mDurationDetail,
            mVoteAverageDetail, mPlotSynopsisDetail, mTrailerLabel;
    private RecyclerView mTrailerList;
    private static TrailerAdapter mTrailerAdapter;

    @SuppressLint("StaticFieldLeak")
    private static ProgressBar mProgressBar;

    @SuppressLint("StaticFieldLeak")
    private static TextView mErrorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mPoster = findViewById(R.id.iv_movie_poster);
        mReleaseDateDetail = findViewById(R.id.tv_release_date_detail);
        mDurationDetail = findViewById(R.id.tv_duration_detail);
        mVoteAverageDetail = findViewById(R.id.tv_vote_average_detail);
        mPlotSynopsisDetail = findViewById(R.id.tv_plot_synopsis_detail);
        mTrailerLabel = findViewById(R.id.tv_trailer_label);

        mTrailerList = findViewById(R.id.rv_trailers);

        mProgressBar = findViewById(R.id.pb_movie_detail);
        mErrorTextView = findViewById(R.id.tv_detail_activity_error);

        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            String movieData = intent.getStringExtra(Intent.EXTRA_TEXT);
            mMovie = NetworkUtils.parseMovieDataFromJson(movieData);
        }

        if(mMovie != null) {
            new DetailActivity.FetchMovieDetailTask().execute(mMovie.getMovieId());

            showFields();
            this.setTitle(mMovie.getTitle());
            String mImageSize = "w400";
            String imageUrl = NetworkUtils.IMAGE_BASE_URL + mImageSize + mMovie.getPosterPath();
            Picasso.get().load(imageUrl).into(mPoster);
            mReleaseDateDetail.setText(mMovie.getReleaseDate());
            mDurationDetail.setText(String.valueOf(mMovie.getRuntime()));
            mVoteAverageDetail.setText(String.valueOf(mMovie.getVoteAverage()));
            mPlotSynopsisDetail.setText(mMovie.getOverview());

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            mTrailerList.setLayoutManager(linearLayoutManager);
            mTrailerList.setHasFixedSize(true);

            mTrailerAdapter = new TrailerAdapter(this, this);
            mTrailerList.setAdapter(mTrailerAdapter);

//            new DetailActivity.FetchMovieDetailTask().execute(mMovie.getMovieId());

        } else {
            showError();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return(super.onOptionsItemSelected(item));
    }

    private void showFields() {
        mPoster.setVisibility(View.VISIBLE);
        mReleaseDateDetail.setVisibility(View.VISIBLE);
        mDurationDetail.setVisibility(View.VISIBLE);
        mVoteAverageDetail.setVisibility(View.VISIBLE);
        mPlotSynopsisDetail.setVisibility(View.VISIBLE);
        mTrailerLabel.setVisibility(View.VISIBLE);
        mTrailerList.setVisibility(View.VISIBLE);
        mErrorTextView.setVisibility(View.INVISIBLE);

    }

    private void showError() {
        mPoster.setVisibility(View.INVISIBLE);
        mDurationDetail.setVisibility(View.INVISIBLE);
        mReleaseDateDetail.setVisibility(View.INVISIBLE);
        mVoteAverageDetail.setVisibility(View.INVISIBLE);
        mPlotSynopsisDetail.setVisibility(View.INVISIBLE);
        mTrailerLabel.setVisibility(View.INVISIBLE);
        mTrailerList.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTrailerItemClick(Trailer trailer) {
        String trailerKey = trailer.getKey();
        try {
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NetworkUtils.YOUTUBE_APP_LINK + trailerKey));
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NetworkUtils.YOUTUBE_WATCH_LINK + trailerKey));
            startActivity(webIntent);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Go to youtube website or app

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing

    }

    private static class FetchMovieDetailTask extends AsyncTask<Integer, Void, Movie> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // TODO: set visibility for the fields
            mErrorTextView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie doInBackground(Integer... params) {

            if (params.length == 0) {
                return null;
            }

            int movieId = params[0];
            String[] extraMovieDetails = new String[]{"videos","reviews"};
            URL movieDetailUrl = NetworkUtils.getMovieDetails(movieId, NetworkUtils.language, extraMovieDetails);

            try {
                String movieDetailJson = NetworkUtils.getResponseFromMovieDetailsUrl(movieDetailUrl);
                return NetworkUtils.parseMovieDetailDataFromJson(movieDetailJson);
            } catch (Exception pme) {
                pme.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie movieDetails) {
            // TODO: set visibility for the fields
            mProgressBar.setVisibility(View.INVISIBLE);
            if (movieDetails != null) {
                mTrailerAdapter.setTrailerListData(movieDetails.getTrailers());
            } else {
                mErrorTextView.setVisibility(View.VISIBLE);
            }
        }
    }
}
