package com.idee.android.popularmovies.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.android.idee.popularmovies.R;
import com.idee.android.popularmovies.data.model.MovieModel;

import static com.idee.android.popularmovies.ui.activity.MovieListActivity.BUNDLE_EXTRA;
import static com.idee.android.popularmovies.ui.activity.MovieListActivity.EXTRA_PARCELABLE;

public class MovieDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_movie_detail);
        setContentView(R.layout.content_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Toast.makeText(this, "MovieDetail", Toast.LENGTH_SHORT).show();

        if (savedInstanceState==null) {

            if (getIntent().getExtras() != null) {
                MovieModel model = getIntent()
                        .getExtras()
                        .getParcelable(EXTRA_PARCELABLE);

                Bundle args = new Bundle();
                args.putParcelable(BUNDLE_EXTRA, model);

                MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
                movieDetailFragment.setArguments(args);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fl_movie_detail, movieDetailFragment)
                        .commit();
            }
        }

    }

}