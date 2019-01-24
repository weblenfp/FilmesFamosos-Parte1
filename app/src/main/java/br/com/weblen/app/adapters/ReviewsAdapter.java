package br.com.weblen.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.weblen.app.R;
import br.com.weblen.app.models.ReviewResult;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    private ArrayList<ReviewResult> mResultReviews = new ArrayList<>();

    public ReviewsAdapter() {
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context        context             = viewGroup.getContext();
        int            layoutIdForListItem = R.layout.reviews_list_item;
        LayoutInflater inflater            = LayoutInflater.from(context);
        View           view                = inflater.inflate(layoutIdForListItem, viewGroup, false);

        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.setAuthorAndContent();
    }

    @Override
    public int getItemCount() {
        return mResultReviews.size();
    }

    public void setReviews(ArrayList<ReviewResult> reviews) {
        this.mResultReviews = reviews;
        notifyDataSetChanged();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.author)
        TextView author;
        @BindView(R.id.content)
        TextView content;

        ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setAuthorAndContent() {
            int          adapterPosition = getAdapterPosition();
            ReviewResult review          = mResultReviews.get(adapterPosition);
            String       author          = review.getAuthor();
            String       content         = review.getContent();

            this.author.setText(author);
            this.content.setText(content);
        }

    }
}