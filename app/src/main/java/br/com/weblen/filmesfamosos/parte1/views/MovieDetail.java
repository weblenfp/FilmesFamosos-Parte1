package br.com.weblen.filmesfamosos.parte1.views;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.com.weblen.filmesfamosos.parte1.R;
import br.com.weblen.filmesfamosos.parte1.models.Movie;
import br.com.weblen.filmesfamosos.parte1.utilities.NetworkUtils;

public class MovieDetail extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        TextView mTitle = findViewById(R.id.tv_title);
        ImageView mPoster = findViewById(R.id.iv_poster);
        TextView mOverview    = findViewById(R.id.tv_overview);
        TextView mVoteAverage = findViewById(R.id.tv_vote_average);
        TextView mReleaseDate = findViewById(R.id.tv_release_date);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(Intent.EXTRA_REFERRER)) {
            Bundle savedData = intent.getExtras();

            Movie  movie = null;

            if (savedData != null)
                movie = savedData.getParcelable(Intent.EXTRA_REFERRER);

            if (movie != null) {
                mTitle.setText(movie.getTitle());
                Picasso.with(this.getApplicationContext()).load(NetworkUtils.buildUrlPosterW185(movie.getPosterPath())).into(mPoster);

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
                }
                else
                    mReleaseDate.append("-");
            }
        }
    }

}
