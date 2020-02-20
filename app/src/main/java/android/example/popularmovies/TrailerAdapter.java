package android.example.popularmovies;

import android.content.Context;
import android.example.popularmovies.model.Trailer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>{

    private final TrailerItemClickListener mOnClickListener;
    private List<Trailer> mTrailerListData;
    Context mContext;

    public interface TrailerItemClickListener {
//        void onTrailerItemClick(String trailerData);
        void onTrailerItemClick(Trailer trailer);
    }

    TrailerAdapter(TrailerItemClickListener trailerItemClickListener, Context context) {
        mOnClickListener = trailerItemClickListener;
        mContext = context;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new TrailerAdapter.TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder trailerViewHolder, int position) {
        Trailer trailer = mTrailerListData.get(position);

//        String trailerText = String.format(mContext.getResources().getString(R.string.trailerItemText), position + 1);
        String trailerText = trailer.getName();
        trailerViewHolder.mTrailerLabel.setText(trailerText);
    }

    @Override
    public int getItemCount() {
        if(null == mTrailerListData)
            return 0;
        return mTrailerListData.size();
    }

    public void setTrailerListData(List<Trailer> trailerListData) {
        mTrailerListData = trailerListData;
        notifyDataSetChanged();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final View mTrailerView;
        final TextView mTrailerLabel;

        TrailerViewHolder(View itemView) {
            super(itemView);
            mTrailerView = (View) itemView.findViewById(R.id.view_trailer_item);
            mTrailerLabel = (TextView) itemView.findViewById(R.id.tv_trailer_label);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(mContext, "Go to youtube to view the trailer.", Toast.LENGTH_SHORT).show();

            int clickedPosition = getAdapterPosition();
            mOnClickListener.onTrailerItemClick(mTrailerListData.get(clickedPosition));
        }
    }
}
