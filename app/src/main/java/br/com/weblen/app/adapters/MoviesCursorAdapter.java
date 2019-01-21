package br.com.weblen.app.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import br.com.weblen.app.R;
import br.com.weblen.app.data.MoviesContract;
import br.com.weblen.app.data.MoviesDBPersistence;
import br.com.weblen.app.models.Movie;
import br.com.weblen.app.utilities.NetworkUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesCursorAdapter extends RecyclerView.Adapter<MoviesCursorAdapter.MoviesAdapterViewHolder> {

    private final MoviesCursorAdapterClickListener mClickListener;
    private Cursor mCursor;


    public MoviesCursorAdapter(MoviesCursorAdapterClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movies_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);

        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder holder, int position) {
        holder.setImage();
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    public void swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return;
        }
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
    }

    public interface MoviesCursorAdapterClickListener {
        void onClick(Movie movie);
    }

    class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_movie)
        ImageView mMoviePoster;

        public MoviesAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        void setImage() {
            int imagePathIndex = mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH);
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            String imageUrl = mCursor.getString(imagePathIndex);
            Picasso.with(itemView.getContext())
                    .load(NetworkUtils.buildUrlPosterW342(imageUrl))
                    .placeholder(R.drawable.ic_image_placeholder)
                    .error(R.drawable.ic_image_error)
                    .into(mMoviePoster);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movie = MoviesDBPersistence.cursorToMovieObject(mCursor, adapterPosition);
            mClickListener.onClick(movie);
        }
    }


}