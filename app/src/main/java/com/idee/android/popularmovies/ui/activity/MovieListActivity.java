package com.idee.android.popularmovies.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.android.idee.popularmovies.R;

public class MovieListActivity extends AppCompatActivity {

    public static final String EXTRA_PARCELABLE = "extra_parcelable";
    public static final String BUNDLE_EXTRA = "bundle_fragment_extra";
    public static boolean twoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.movie_detail_tablet)!=null){
            twoPane = true;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_tablet,
                             new MovieDetailFragment())
                    .commit();

        } else
            twoPane = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_list, menu);
        return true;
    }

}