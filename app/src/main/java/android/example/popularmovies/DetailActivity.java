package android.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.example.popularmovies.model.Movie;
import android.example.popularmovies.utilities.NetworkUtils;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerItemClickListener, AdapterView.OnItemSelectedListener {

    private Movie mMovie;
    private ImageView mPoster;
    private TextView mReleaseDateDetail, mDurationDetail,
            mVoteAverageDetail, mPlotSynopsisDetail, mTrailerLabel, mErrorMessage;
    private RecyclerView mTrailerList;
    private TrailerAdapter mTrailerAdapter;

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
        mErrorMessage = findViewById(R.id.tv_error_message);

        mTrailerList = findViewById(R.id.rv_trailers);

        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            String movieData = intent.getStringExtra(Intent.EXTRA_TEXT);
            mMovie = NetworkUtils.parseMovieDataFromJson(movieData);
        }

        if(mMovie != null) {
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
        mErrorMessage.setVisibility(View.INVISIBLE);

    }

    private void showError() {
        mPoster.setVisibility(View.INVISIBLE);
        mDurationDetail.setVisibility(View.INVISIBLE);
        mReleaseDateDetail.setVisibility(View.INVISIBLE);
        mVoteAverageDetail.setVisibility(View.INVISIBLE);
        mPlotSynopsisDetail.setVisibility(View.INVISIBLE);
        mTrailerLabel.setVisibility(View.INVISIBLE);
        mTrailerList.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTrailerItemClick(String trailerData) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Go to youtube website or app

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing

    }
}
