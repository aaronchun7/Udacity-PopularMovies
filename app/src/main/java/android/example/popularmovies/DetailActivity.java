package android.example.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.example.popularmovies.model.Movie;
import android.example.popularmovies.utilities.NetworkUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView mPoster = findViewById(R.id.iv_movie_poster);
        TextView mReleaseDateDetail = findViewById(R.id.tv_release_date_detail);
        TextView mVoteAverageDetail = findViewById(R.id.tv_vote_average_detail);
        TextView mPlotSynopsisDetail = findViewById(R.id.tv_plot_synopsis_detail);

        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            String movieData = intent.getStringExtra(Intent.EXTRA_TEXT);
            mMovie = NetworkUtils.parseMovieDataFromJson(movieData);
        }

        if(mMovie != null) {
            this.setTitle(mMovie.getTitle());
            String mImageSize = "w500";
            String imageUrl = NetworkUtils.IMAGE_BASE_URL + mImageSize + mMovie.getPosterPath();
            Picasso.get().load(imageUrl).into(mPoster);
            mReleaseDateDetail.setText(mMovie.getReleaseDate());
            mVoteAverageDetail.setText(String.valueOf(mMovie.getVoteAverage()));
            mPlotSynopsisDetail.setText(mMovie.getOverview());
        } else {
            // TODO: Show and error message or something similar
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
}
