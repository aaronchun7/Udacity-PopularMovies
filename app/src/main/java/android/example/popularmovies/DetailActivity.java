package android.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.example.popularmovies.model.Movie;
import android.example.popularmovies.utilities.NetworkUtils;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private Movie mMovie;
    private ImageView mPoster;
    private TextView mReleaseDateLabel, mVoteAverageLabel, mPlotSynopsisLabel, mReleaseDateDetail,
            mVoteAverageDetail, mPlotSynopsisDetail, mErrorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mPoster = findViewById(R.id.iv_movie_poster);
        mReleaseDateLabel = findViewById(R.id.tv_release_date_label);
        mVoteAverageLabel = findViewById(R.id.tv_vote_average_label);
        mPlotSynopsisLabel = findViewById(R.id.tv_plot_synopsis_label);
        mReleaseDateDetail = findViewById(R.id.tv_release_date_detail);
        mVoteAverageDetail = findViewById(R.id.tv_vote_average_detail);
        mPlotSynopsisDetail = findViewById(R.id.tv_plot_synopsis_detail);
        mErrorMessage = findViewById(R.id.tv_error_message);

        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            String movieData = intent.getStringExtra(Intent.EXTRA_TEXT);
            mMovie = NetworkUtils.parseMovieDataFromJson(movieData);
        }

        if(mMovie != null) {
            showFields();
            this.setTitle(mMovie.getTitle());
            String mImageSize = "w500";
            String imageUrl = NetworkUtils.IMAGE_BASE_URL + mImageSize + mMovie.getPosterPath();
            Picasso.get().load(imageUrl).into(mPoster);
            mReleaseDateDetail.setText(mMovie.getReleaseDate());
            mVoteAverageDetail.setText(String.valueOf(mMovie.getVoteAverage()));
            mPlotSynopsisDetail.setText(mMovie.getOverview());
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
        mReleaseDateLabel.setVisibility(View.VISIBLE);
        mVoteAverageLabel.setVisibility(View.VISIBLE);
        mPlotSynopsisLabel.setVisibility(View.VISIBLE);
        mReleaseDateDetail.setVisibility(View.VISIBLE);
        mVoteAverageDetail.setVisibility(View.VISIBLE);
        mPlotSynopsisDetail.setVisibility(View.VISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
    }

    private void showError() {
        mPoster.setVisibility(View.INVISIBLE);
        mReleaseDateLabel.setVisibility(View.INVISIBLE);
        mVoteAverageLabel.setVisibility(View.INVISIBLE);
        mPlotSynopsisLabel.setVisibility(View.INVISIBLE);
        mReleaseDateDetail.setVisibility(View.INVISIBLE);
        mVoteAverageDetail.setVisibility(View.INVISIBLE);
        mPlotSynopsisDetail.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }
}
