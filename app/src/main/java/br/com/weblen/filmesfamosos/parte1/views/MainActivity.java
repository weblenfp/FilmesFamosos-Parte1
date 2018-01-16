package br.com.weblen.filmesfamosos.parte1.views;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.weblen.filmesfamosos.parte1.R;
import br.com.weblen.filmesfamosos.parte1.data.MoviesAdapter;
import br.com.weblen.filmesfamosos.parte1.models.Movie;
import br.com.weblen.filmesfamosos.parte1.models.MovieCollection;
import br.com.weblen.filmesfamosos.parte1.utilities.AsyncTaskSearchMovies;
import br.com.weblen.filmesfamosos.parte1.utilities.NetworkUtils;
import br.com.weblen.filmesfamosos.parte1.utilities.SearchMovies;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterClickListener, AsyncTaskSearchMovies {

    private RecyclerView mRecyclerView;
    private TextView mErrorMessage;
    private ProgressBar mProgressBar;
    private MoviesAdapter moviesAdapter;
    private MovieCollection movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_movies);
        mErrorMessage = findViewById(R.id.tv_error_message_display);
        mProgressBar = findViewById(R.id.pb_loading_indicator);

        int spanCount = 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        moviesAdapter = new MoviesAdapter(this);
        mRecyclerView.setAdapter(moviesAdapter);

        showProgressBar();

        if (isInternetAvailable())
            new SearchMovies(this, movies).execute(NetworkUtils.buildUrlPopularMovies());
        else
            showErrorInternetConnection();
    }

    private boolean isInternetAvailable() {
        ConnectivityManager cm      = (ConnectivityManager)  getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo         netInfo = null;
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
                        new SearchMovies(this, movies).execute(NetworkUtils.buildUrlPopularMovies());
                    else
                        showErrorInternetConnection();
                    break;
                case R.id.menu_rating:
                    showProgressBar();
                    if (isInternetAvailable())
                        new SearchMovies(this, movies).execute(NetworkUtils.buildUrlTopRatedMovies());
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
        Intent intent = new Intent(this, MovieDetail.class);
        intent.putExtra(Intent.EXTRA_REFERRER, movie);
        startActivity(intent);
    }

    @Override
    public void processFinish(Object output) {

        movies = (MovieCollection) output;

        if (movies != null) {
            ArrayList<Movie> mMovies;
            mMovies = movies.getObjMovies();
            moviesAdapter.setMoviesData(mMovies);
            showRecyclerView();
        }
        else
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