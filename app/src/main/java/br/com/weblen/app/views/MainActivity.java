package br.com.weblen.app.views;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.weblen.app.BuildConfig;
import br.com.weblen.app.R;
import br.com.weblen.app.data.MoviesAdapter;
import br.com.weblen.app.models.Movie;
import br.com.weblen.app.models.MovieCollection;
import br.com.weblen.app.utilities.APIClient;
import br.com.weblen.app.utilities.APIInterface;
import br.com.weblen.app.utilities.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterClickListener {

    @BindView(R.id.rv_movies)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_error_message_display)
    TextView     mErrorMessage;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar  mProgressBar;
    private MoviesAdapter   moviesAdapter;
    private MovieCollection movies;
    private APIInterface    apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        int               spanCount         = 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        moviesAdapter = new MoviesAdapter(this);
        mRecyclerView.setAdapter(moviesAdapter);

        showProgressBar();

        fetchMovies(Constants.searchType.BY_POPULAR);
    }

    private void fetchMovies(int paramSearchType) {

        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<MovieCollection> call;

        switch (paramSearchType) {
            case Constants.searchType.BY_POPULAR:
                call = apiInterface.doGetPopularMovies(BuildConfig.VALUE_API_KEY);
                break;
            case Constants.searchType.BY_TOP_RATED:
                call = apiInterface.doGetTopRatedMovies(BuildConfig.VALUE_API_KEY);
                break;

            default:
                showErrorMessage();
                return;
        }

        call.enqueue(new Callback<MovieCollection>() {
            @Override
            public void onResponse(Call<MovieCollection> call, Response<MovieCollection> response) {
                Log.d("TAG", response.code() + "");
                MovieCollection responseMovies = response.body();
                processFinish(responseMovies);
            }

            @Override
            public void onFailure(Call<MovieCollection> call, Throwable t) {
                call.cancel();
                showErrorMessage();
            }
        });
    }

    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }

        return netInfo != null && netInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int selectedMenuId = item.getItemId();

        try {
            switch (selectedMenuId) {

                case R.id.menu_popularity:
                    showProgressBar();
                    if (isInternetAvailable())
                        fetchMovies(Constants.searchType.BY_POPULAR);
                    else
                        showErrorInternetConnection();
                    break;
                case R.id.menu_rating:
                    showProgressBar();
                    if (isInternetAvailable())
                        fetchMovies(Constants.searchType.BY_TOP_RATED);
                    else
                        showErrorInternetConnection();
                default:
                    break;
            }

        } catch (Exception e) {
            showErrorInternetConnection();
        }

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void OnClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(Intent.EXTRA_REFERRER, movie);
        startActivity(intent);
    }

    private void processFinish(Object output) {

        movies = (MovieCollection) output;

        if (movies != null) {
            ArrayList<Movie> mMovies;
            mMovies = movies.getObjMovies();
            moviesAdapter.setMoviesData(mMovies);
            showRecyclerView();
        } else
            showErrorMessage();
    }

    private void showRecyclerView() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    private void showErrorInternetConnection() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
        mErrorMessage.setText(R.string.error_no_internet_connection);
    }
}