package br.com.weblen.app.adapters;

import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.com.weblen.app.R;
import br.com.weblen.app.models.Movie;
import br.com.weblen.app.utilities.NetworkHelper;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    private final MoviesAdapterClickListener mClickListener;
    private       ArrayList<Movie>           movies = new ArrayList<>();
    private       long                       mLastClickTime;

    public MoviesAdapter(MoviesAdapterClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    public void setMoviesData(ArrayList<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MoviesAdapter.MoviesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context        context             = parent.getContext();
        int            layoutIdForListItem = R.layout.movies_list_item;
        LayoutInflater inflater            = LayoutInflater.from(context);
        View           view                = inflater.inflate(layoutIdForListItem, parent, false);

        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapter.MoviesAdapterViewHolder holder, int position) {
        holder.setImage();
    }

    @Override
    public int getItemCount() {
        if (null == movies) return 0;
        return movies.size();
    }

    public interface MoviesAdapterClickListener {
        void OnClick(Movie movie);
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_movie)
        ImageView mMoviePoster;

        MoviesAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        void setImage() {
            int   adapterPosition = getAdapterPosition();
            Movie movie           = movies.get(adapterPosition);
            Picasso.with(itemView.getContext())
                    .load(NetworkHelper.buildUrlPosterW342(movie.getPosterPath()))
                    .placeholder(R.drawable.ic_image_placeholder)
                    .error(R.drawable.ic_image_error)
                    .into(mMoviePoster);
        }

        @Override
        public void onClick(View v) {
            // double-clicking prevention, using threshold of 1000 ms
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            int   adapterPosition = getAdapterPosition();
            Movie movie           = movies.get(adapterPosition);
            mClickListener.OnClick(movie);
        }
    }
}
