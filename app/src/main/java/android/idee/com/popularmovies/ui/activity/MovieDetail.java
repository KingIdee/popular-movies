package android.idee.com.popularmovies.ui.activity;

import android.idee.com.popularmovies.data.model.MovieModel;
import android.idee.com.popularmovies.ui.fragment.MovieListActivityFragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.idee.com.popularmovies.R;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetail extends AppCompatActivity {

    MovieModel bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bundle  = getIntent().getParcelableExtra(MovieListActivityFragment.EXTRA_PARCEABLE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView releaseDate, overview, voteAverage;
        ImageView imageBackground = (ImageView) findViewById(R.id.image_background);
        ImageView posterBackground = (ImageView) findViewById(R.id.iv_movie_poster_image);

        releaseDate = (TextView) findViewById(R.id.tv_release_date);
        overview = (TextView) findViewById(R.id.tv_overview);
        voteAverage = (TextView) findViewById(R.id.tv_vote_average);

        if (bundle!=null){
            getSupportActionBar().setTitle(bundle.getTitle());
            releaseDate.setText(bundle.getReleaseDate());
            overview.setText(bundle.getOverview());
            voteAverage.setText(bundle.getAverage());
            Picasso.with(getApplicationContext()).
                    load("http://image.tmdb.org/t/p/w185/" +bundle.getPosterPath()).into(posterBackground);

            Picasso.with(getApplicationContext()).
                    load("http://image.tmdb.org/t/p/w185/" +bundle.getBackdropPath()).into(imageBackground);

        }

    }
}
