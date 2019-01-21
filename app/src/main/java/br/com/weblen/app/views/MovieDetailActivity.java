package br.com.weblen.app.views;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import br.com.weblen.app.R;
import br.com.weblen.app.adapters.ReviewsAdapter;
import br.com.weblen.app.adapters.TrailersAdapter;
import br.com.weblen.app.api.APIClient;
import br.com.weblen.app.api.APIInterface;
import br.com.weblen.app.data.MoviesDBPersistence;
import br.com.weblen.app.models.Movie;
import br.com.weblen.app.models.ReviewCollection;
import br.com.weblen.app.models.ReviewResult;
import br.com.weblen.app.models.Trailer;
import br.com.weblen.app.models.TrailerCollection;
import br.com.weblen.app.utilities.Constants;
import br.com.weblen.app.utilities.NetworkUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static br.com.weblen.app.BuildConfig.VALUE_API_KEY;

public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.scroll_view)
    NestedScrollView mScrollView;
    @BindView(R.id.tv_title)
    TextView         mTitle;
    @BindView(R.id.iv_poster)
    ImageView        mPoster;
    @BindView(R.id.tv_overview)
    TextView         mOverview;
    @BindView(R.id.tv_vote_average)
    TextView         mVoteAverage;
    @BindView(R.id.tv_release_date)
    TextView         mReleaseDate;
    @BindView(R.id.iv_star)
    ImageView        mStaredMovie;
    @BindView(R.id.rv_trailers)
    RecyclerView     mRecyclerViewTrailers;
    @BindView(R.id.rv_reviews)
    RecyclerView     mRecyclerViewReviews;
    @BindView(R.id.pb_loading_indicator_trailers)
    ProgressBar      mProgressBarTrailers;
    @BindView(R.id.pb_loading_indicator_reviews)
    ProgressBar      mProgressBarReviews;

    Movie mMovie = null;
    private TrailersAdapter    mTrailersAdapter;
    private ArrayList<Trailer> mTrailersArray     = new ArrayList<>();
    private TrailerCollection  mTrailerCollection = new TrailerCollection(mTrailersArray);

    private ReviewsAdapter          mReviewsAdapter;
    private ArrayList<ReviewResult> mReviewsArray     = new ArrayList<>();
    private ReviewCollection        mReviewCollection = new ReviewCollection(mReviewsArray);

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.MOVIE, mMovie);
        outState.putParcelable(Constants.TRAILERS, mTrailerCollection);
        outState.putParcelable(Constants.REVIEWS, mReviewCollection);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        mTrailerCollection = null;
        mReviewCollection = null;

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(Constants.MOVIE))
                mMovie = savedInstanceState.getParcelable(Constants.MOVIE);
            if (savedInstanceState.containsKey(Constants.TRAILERS))
                mTrailerCollection = savedInstanceState.getParcelable(Constants.TRAILERS);

            buildScreen(mMovie, mTrailerCollection, mReviewCollection);
        }

        mScrollView.scrollTo(0, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        LinearLayoutManager lmReviewsManager  = new LinearLayoutManager(this);
        LinearLayoutManager lmTrailersManager = new LinearLayoutManager(this);
        mRecyclerViewTrailers.setLayoutManager(lmReviewsManager);
        mRecyclerViewReviews.setLayoutManager(lmTrailersManager);

        mRecyclerViewTrailers.setHasFixedSize(true);
        mRecyclerViewReviews.setHasFixedSize(true);

        mTrailersAdapter = new TrailersAdapter();
        mReviewsAdapter = new ReviewsAdapter();

        mRecyclerViewTrailers.setAdapter(mTrailersAdapter);
        mRecyclerViewReviews.setAdapter(mReviewsAdapter);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(Intent.EXTRA_REFERRER)) {
            //Bundle savedData = intent.getExtras();
            Movie movie = intent.getParcelableExtra(Intent.EXTRA_REFERRER);
            buildScreen(movie, null, null);
        }

        mStaredMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStarredStatus();
                isMovieStarred();
            }
        });

        showProgressBarTrailers();
        fetchTrailers();
        showProgressBarReviews();
        fetchReviews();
    }

    private void buildScreen(Movie movie, TrailerCollection trailers, ReviewCollection reviews) {

        mMovie = movie;

        if (movie != null) {
            mTitle.setText(movie.getTitle());
            Picasso.with(getApplicationContext()).load(NetworkUtils.buildUrlPosterW185(movie.getPosterPath())).into(mPoster);

            if (!movie.getOverview().equals(""))
                mOverview.append(movie.getOverview());
            else
                mOverview.append("-");

            if (!String.valueOf(movie.getVoteAverage()).equals(""))
                mVoteAverage.append(String.valueOf(movie.getVoteAverage()));
            else
                mVoteAverage.append("-");

            if (!movie.getReleaseDate().equals("")) {
                Date date;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(movie.getReleaseDate());
                    String newDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date);
                    mReleaseDate.append(newDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else
                mReleaseDate.append("-");
        }

        mMovie = MoviesDBPersistence.checkFavoriteMovies(mMovie, this);

        isMovieStarred();

        if (trailers != null)
            processFinishTrailers(trailers);

        if (reviews != null)
            processFinishReviews(reviews);
    }


    private void isMovieStarred() {

        if (mMovie != null) {
            if (mMovie.isStarred()) {
                mStaredMovie.setImageResource(R.drawable.ic_star_blue_24dp);
            } else {
                mStaredMovie.setImageResource(R.drawable.ic_star_border_blue_24dp);
            }
        }
    }

    private void changeStarredStatus() {

        if (mMovie != null) {
            if (mMovie.isStarred()) {
                mMovie = MoviesDBPersistence.excludeStarredMovie(mMovie, getApplicationContext());
            } else {
                mMovie = MoviesDBPersistence.includeStarredMovie(mMovie, getApplicationContext());
            }
        }
    }

    private void fetchTrailers() {

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<TrailerCollection> call;
        call = apiInterface.doGetMovieTrailers(String.valueOf(mMovie.getId()), VALUE_API_KEY);

        call.enqueue(new Callback<TrailerCollection>() {
            @Override
            public void onResponse(@NonNull Call<TrailerCollection> call, @NonNull Response<TrailerCollection> response) {
                Log.d("TAG", response.code() + "");
                TrailerCollection responseTrailers = response.body();
                processFinishTrailers(responseTrailers);
                hideProgressBarTrailers();
            }

            @Override
            public void onFailure(@NonNull Call<TrailerCollection> call, @NonNull Throwable t) {
                call.cancel();
            }
        });
    }

    private void fetchReviews() {

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<ReviewCollection> call;
        call = apiInterface.doGetMovieReviews(String.valueOf(mMovie.getId()), VALUE_API_KEY);

        call.enqueue(new Callback<ReviewCollection>() {
            @Override
            public void onResponse(@NonNull Call<ReviewCollection> call, @NonNull Response<ReviewCollection> response) {
                Log.d("TAG", response.code() + "");
                ReviewCollection responseReviews = response.body();
                processFinishReviews(responseReviews);
                hideProgressBarReviews();
            }

            @Override
            public void onFailure(@NonNull Call<ReviewCollection> call, @NonNull Throwable t) {
                call.cancel();
            }
        });
    }

    private void processFinishTrailers(TrailerCollection trailers) {

        if (trailers != null && trailers.getTrailers().size() > 0) {
            mTrailersArray.clear();
            mTrailersArray.addAll(trailers.getTrailers());
            mTrailersAdapter.setTrailers(mTrailersArray);
            mTrailerCollection.setTrailers(mTrailersArray);
            showRecyclerViewTrailers();
        }
    }

    private void processFinishReviews(ReviewCollection review) {

        if (review != null && review.getReviews().size() > 0) {
            mReviewsArray.clear();
            mReviewsArray.addAll(review.getReviews());
            mReviewsAdapter.setReviews(mReviewsArray);
            mReviewCollection.setReviews(mReviewsArray);
            showRecyclerViewReviews();
        }
    }

    private void showRecyclerViewTrailers() {
        mProgressBarTrailers.setVisibility(View.INVISIBLE);
        mRecyclerViewTrailers.setVisibility(View.VISIBLE);
    }

    private void showRecyclerViewReviews() {
        mProgressBarReviews.setVisibility(View.INVISIBLE);
        mRecyclerViewReviews.setVisibility(View.VISIBLE);
    }

    private void showProgressBarTrailers() {
        mProgressBarTrailers.setVisibility(View.VISIBLE);
        mRecyclerViewTrailers.setVisibility(View.INVISIBLE);
    }

    private void hideProgressBarTrailers() {
        mProgressBarTrailers.setVisibility(View.INVISIBLE);
    }

    private void showProgressBarReviews() {
        mProgressBarReviews.setVisibility(View.VISIBLE);
        mRecyclerViewReviews.setVisibility(View.INVISIBLE);
    }

    private void hideProgressBarReviews() {
        mProgressBarReviews.setVisibility(View.INVISIBLE);
    }
}
