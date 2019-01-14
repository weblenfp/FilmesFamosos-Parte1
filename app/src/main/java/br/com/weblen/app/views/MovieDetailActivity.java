package br.com.weblen.app.views;

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

import br.com.weblen.app.R;
import br.com.weblen.app.models.Movie;
import br.com.weblen.app.utilities.NetworkUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView  mTitle;
    @BindView(R.id.iv_poster)
    ImageView mPoster;
    @BindView(R.id.tv_overview)
    TextView  mOverview;
    @BindView(R.id.tv_vote_average)
    TextView  mVoteAverage;
    @BindView(R.id.tv_release_date)
    TextView  mReleaseDate;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(Intent.EXTRA_REFERRER)) {
            Bundle savedData = intent.getExtras();

            Movie movie = null;

            if (savedData != null)
                movie = savedData.getParcelable(Intent.EXTRA_REFERRER);

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
        }
    }

}
