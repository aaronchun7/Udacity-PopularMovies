package android.example.popularmovies;

import android.content.Context;
import android.example.popularmovies.model.Movie;
import android.example.popularmovies.utilities.NetworkUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

    private final MovieItemClickListener mOnClickListener;
    private int mNumberItems;
    private String mTitle;
    private List<String> mMovieListData;

    public interface MovieItemClickListener {
        void onMovieItemClick(String movieData);
    }

    MovieAdapter(MovieItemClickListener movieItemClickListener) {
        // TODO This is a default constructor which we may need to fill in
        mOnClickListener = movieItemClickListener;
    }

    @NonNull
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // TODO Need to implement this
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieViewHolder movieAdapterViewHolder, int position) {
        // TODO Need to implement this
        String movieDataJson = mMovieListData.get(position);
        Movie movie = NetworkUtils.parseMovieDataFromJson(movieDataJson);
        movieAdapterViewHolder.mMovieTitle.setText(movie.getTitle());
        String imagePath = NetworkUtils.IMAGE_BASE_URL + NetworkUtils.IMAGE_SIZE + movie.getPosterPath();
//        Picasso.get().load(imagePath).resize(50,50).centerCrop().into(movieAdapterViewHolder.mPosterThumbnail);
        Picasso.get().setLoggingEnabled(true);
        Picasso.get().load(imagePath).into(movieAdapterViewHolder.mPosterThumbnail);
    }

    @Override
    public int getItemCount() {
        // TODO Need to implement this
        if(null == mMovieListData)
            return 0;
        return mMovieListData.size();
    }

    public void setMovieListData(List<String> movieListData) {
        mMovieListData = movieListData;
        notifyDataSetChanged();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // TODO Add any variables we need here
        ImageView mPosterThumbnail;
        TextView mMovieTitle;


        MovieViewHolder(View itemView) {
            super(itemView);
            mPosterThumbnail = (ImageView) itemView.findViewById(R.id.iv_poster_thumbnail);
            mMovieTitle = (TextView) itemView.findViewById(R.id.tv_movie_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onMovieItemClick(mMovieListData.get(clickedPosition));
        }
    }
}
