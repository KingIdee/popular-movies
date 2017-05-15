package com.idee.android.popularmovies.ui.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.idee.popularmovies.R;
import com.idee.android.popularmovies.data.model.MovieModel;
import com.idee.android.popularmovies.data.provider.MovieContract;
import com.idee.android.popularmovies.utils.PreferenceUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;

import static com.idee.android.popularmovies.ui.activity.MovieListActivity.BUNDLE_EXTRA;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>  {

    private static final int TASK_LOADER_ID = 1998;
    MovieModel movieModel;
    boolean isFavourite = false;

    public MovieDetailFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        setRetainInstance(true);
        TextView releaseDate, overview, voteAverage;
        ImageView imageBackground = (ImageView) view.findViewById(R.id.image_background);
        ImageView posterBackground = (ImageView) view.findViewById(R.id.iv_movie_poster_image);

        releaseDate = (TextView) view.findViewById(R.id.tv_release_date);
        overview = (TextView) view.findViewById(R.id.tv_overview);
        voteAverage = (TextView) view.findViewById(R.id.tv_vote_average);

        Bundle bundle = getArguments();

        if (bundle!=null)
            movieModel = bundle.getParcelable(BUNDLE_EXTRA);

        if (movieModel!=null){
            Toast.makeText(getActivity(), movieModel.getId(), Toast.LENGTH_SHORT).show();
            //getActivity().getSupportActionBar().setTitle(bundle.getTitle());
            releaseDate.setText(movieModel.getReleaseDate());
            overview.setText(movieModel.getOverview());
            voteAverage.setText(movieModel.getAverage());
            Picasso.with(getActivity()).
                    load("http://image.tmdb.org/t/p/w185/" +movieModel.getPosterPath()).into(posterBackground);

            //Picasso.with(getActivity()).
              //      load("http://image.tmdb.org/t/p/w185/" +movieModel.getBackdropPath()).into(imageBackground);

            getActivity().getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);

        }

        favouriteButton = (ImageButton) view.findViewById(R.id.ib_favourite);
        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
                handleFavButtonClick();
            }
        });

        return view;

    }

    ImageButton favouriteButton;
    public void handleFavButtonClick(){

        Toast.makeText(getActivity(), "buttonClicked", Toast.LENGTH_SHORT).show();
        if (isFavourite){
            Log.d("TAG","about to remove from favourites");
            //It was already in favourites
            adjustFavButton(true);
            removeFromFavourites();
            //TODO: remove from content provider
        } else {
            Log.d("TAG","about to add to favourites");
            // was never among the favourites
            adjustFavButton(false);
            addToFavourites();
        }

    }

    private void removeFromFavourites() {

        isFavourite = false;
        String mSelection = MovieContract.MovieEntry.COLUMN_MOVIE_ID+"=?";
        String [] mSelectionArgs = {movieModel.getId()};
        getActivity().getContentResolver().delete(MovieContract.MovieEntry.MOVIE_URI,mSelection,mSelectionArgs) ;

        //TODO: restart loader in MainActivity Fragment;
        //TODO: set isFavourite to false;
        //MovieContract.MovieEntry.MOVIE_URI.buildUpon().appendPath().build()

    }

    private void addToFavourites(){
        Log.d("TAG","addToFavourites");
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,movieModel.getTitle());
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID,movieModel.getId());
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,movieModel.getOverview());
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_AVERAGE,movieModel.getAverage());

        Uri uri = getActivity().getContentResolver().insert(MovieContract.MovieEntry.MOVIE_URI,values);

        if (uri!=null){
            Toast.makeText(getActivity(), uri.toString(), Toast.LENGTH_SHORT).show();
            Log.d("TAG", uri.toString());
            isFavourite = true;
        } else
            Toast.makeText(getActivity(), "uri is null", Toast.LENGTH_SHORT).show();

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        final Cursor data = null;

        AsyncTaskLoader<Cursor> asyncTaskLoader;
        asyncTaskLoader =  new AsyncTaskLoader<Cursor>(getActivity()) {
            @Override
            public Cursor loadInBackground() {

                String mSelection = MovieContract.MovieEntry.COLUMN_MOVIE_ID+"=?";
                String [] mSelectionArgs = {movieModel.getId()};

                try {

                    return getActivity().getContentResolver().query(
                            MovieContract.MovieEntry.MOVIE_URI,
                            null,
                            mSelection, //selection
                            mSelectionArgs, // selection args
                            null
                    );

                } catch (Exception e){
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            protected void onStartLoading() {
                forceLoad();
            }
        };

        return asyncTaskLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {


        if (data!=null) {
            Toast.makeText(getActivity(), String.valueOf(data.getCount()), Toast.LENGTH_SHORT).show();

            if (data.getCount() == 1) {
                isFavourite = true;
                adjustFavButton(true);
                Toast.makeText(getActivity(), loader.dataToString(data), Toast.LENGTH_SHORT).show();
                Log.d("TAG", loader.dataToString(data));
            } else {
                isFavourite = false;
                adjustFavButton(false);
                Toast.makeText(getActivity(), "It is not among the favourite", Toast.LENGTH_SHORT).show();
            }
        } else {
            //null
        }
    }

    private void adjustFavButton(boolean b) {

       /* if (b){
            favouriteButton.setBackgroundResource(R.drawable.favourite);
        } else {
            favouriteButton.setBackgroundResource(R.drawable.unfavourite);
        }*/

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}