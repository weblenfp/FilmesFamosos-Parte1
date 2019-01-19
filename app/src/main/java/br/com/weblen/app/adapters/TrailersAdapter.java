package br.com.weblen.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.weblen.app.R;
import br.com.weblen.app.models.Trailer;
import br.com.weblen.app.utilities.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;


public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersViewHolder> {

    private ArrayList<Trailer> mTrailers = new ArrayList<>();

    public TrailersAdapter() {
    }

    @Override
    public TrailersViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context        context             = viewGroup.getContext();
        int            layoutIdForListItem = R.layout.trailers_list_item;
        LayoutInflater inflater            = LayoutInflater.from(context);
        View           view                = inflater.inflate(layoutIdForListItem, viewGroup, false);

        return new TrailersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailersViewHolder holder, int position) {
        holder.setTrailerTitle();
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public void setTrailers(ArrayList<Trailer> trailers) {
        this.mTrailers = trailers;
        notifyDataSetChanged();
    }

    public class TrailersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.cardVideo)
        CardView cardImage;
        @BindView(R.id.trailerTitle)
        TextView trailerTitle;

        TrailersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            cardImage.setOnClickListener(this);
        }

        void setTrailerTitle() {
            int     adapterPosition = getAdapterPosition();
            Trailer trailer         = mTrailers.get(adapterPosition);
            String  trailerTitle    = trailer.getName();
            this.trailerTitle.setText(trailerTitle);
        }

        @Override
        public void onClick(View view) {
            int     adapterPosition = getAdapterPosition();
            Trailer trailer         = mTrailers.get(adapterPosition);
            String  url             = Constants.YOUTUBE_URL + trailer.getKey();
            Uri     uri             = Uri.parse(url);
            Intent  intent          = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            view.getContext().startActivity(intent);
        }
    }
}
