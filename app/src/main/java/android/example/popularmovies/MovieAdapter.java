package android.example.popularmovies;

import android.content.Context;
import android.example.popularmovies.model.Movie;
import android.example.popularmovies.utilities.NetworkUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

    private final MovieItemClickListener mOnClickListener;
    private List<String> mMovieListData;

    public interface MovieItemClickListener {
        void onMovieItemClick(String movieData);
    }

    MovieAdapter(MovieItemClickListener movieItemClickListener) {
        mOnClickListener = movieItemClickListener;
    }

    @NonNull
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieViewHolder movieAdapterViewHolder, int position) {
        String movieDataJson = mMovieListData.get(position);
        Movie movie = NetworkUtils.parseMovieDataFromJson(movieDataJson);
        String imagePath = NetworkUtils.IMAGE_BASE_URL + NetworkUtils.IMAGE_SIZE + movie.getPosterPath();
        Picasso.get().setLoggingEnabled(true);
        Picasso.get().load(imagePath).resize(MainActivity.mThumbnailWidth,0).into(movieAdapterViewHolder.mPosterThumbnail);
    }

    @Override
    public int getItemCount() {
        if(null == mMovieListData)
            return 0;
        return mMovieListData.size();
    }

    public void setMovieListData(List<String> movieListData) {
        mMovieListData = movieListData;
        notifyDataSetChanged();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mPosterThumbnail;


        MovieViewHolder(View itemView) {
            super(itemView);
            mPosterThumbnail = itemView.findViewById(R.id.iv_poster_thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onMovieItemClick(mMovieListData.get(clickedPosition));
        }
    }
}
