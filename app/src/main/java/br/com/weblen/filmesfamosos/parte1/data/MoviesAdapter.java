package br.com.weblen.filmesfamosos.parte1.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.com.weblen.filmesfamosos.parte1.R;
import br.com.weblen.filmesfamosos.parte1.models.Movie;
import br.com.weblen.filmesfamosos.parte1.utilities.NetworkUtils;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    private ArrayList<Movie> movies = new ArrayList<>();
    private final MoviesAdapterClickListener mClickListener;

    public MoviesAdapter(MoviesAdapterClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    public interface MoviesAdapterClickListener {
        void OnClick(Movie movie);
    }

    public void setMoviesData(ArrayList<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @Override
    public MoviesAdapter.MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context        context             = parent.getContext();
        int            layoutIdForListItem = R.layout.movies_list_item;
        LayoutInflater inflater            = LayoutInflater.from(context);
        View           view                = inflater.inflate(layoutIdForListItem, parent, false);

        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapter.MoviesAdapterViewHolder holder, int position) {
        holder.setImage();
    }

    @Override
    public int getItemCount() {
        if (null == movies) return 0;
        return movies.size();
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView mMoviePoster;

        public MoviesAdapterViewHolder(View itemView) {
            super(itemView);
            mMoviePoster = itemView.findViewById(R.id.iv_movie);
            itemView.setOnClickListener(this);
        }

        void setImage() {
            int adapterPosition = getAdapterPosition();
            Movie movie = movies.get(adapterPosition);
            Picasso.with(itemView.getContext()).load(NetworkUtils.buildUrlPosterW342(movie.getPosterPath())).into(mMoviePoster);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movie = movies.get(adapterPosition);
            mClickListener.OnClick(movie);
        }
    }
}
