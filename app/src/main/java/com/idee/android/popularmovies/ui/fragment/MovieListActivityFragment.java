package com.idee.android.popularmovies.ui.fragment;

import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.idee.popularmovies.R;
import com.idee.android.popularmovies.model.MovieModel;
import com.idee.android.popularmovies.ui.activity.MovieDetail;
import com.idee.android.popularmovies.ui.activity.NewSettingsActivity;
import com.idee.android.popularmovies.ui.adapter.MovieListAdapter;
import com.idee.android.popularmovies.utils.NetworkUtils;
import com.idee.android.popularmovies.utils.PreferenceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */

public class MovieListActivityFragment extends Fragment implements MovieListAdapter.ItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener  {

    private static final String LOG_TAG = MovieListActivityFragment.class.getSimpleName();
    private static final String CURRENT_SORT_ORDER = "current_sort_order";
    private static final String MY_LIST = "rv_array_list";
    public static final String EXTRA_PARCEABLE = "extra_parceable";
    private ArrayList<MovieModel> movieArrayList = new ArrayList<>();
    MovieListAdapter movieListAdapter;
    SharedPreferences sharedPreferences;

    public MovieListActivityFragment() {}

    @BindView(R.id.pb_loading_movie_list)
    ProgressBar progressBar;

    @BindView(R.id.tv_error_message)
    TextView errorMessage;

    @BindView(R.id.rv_movie_list)
    RecyclerView mMovieList;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MY_LIST, movieArrayList);
        Log.d(LOG_TAG,"On Save Instance");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.fragment_movie_list, container, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        ButterKnife.bind(this,mView);
        setHasOptionsMenu(true);

        errorMessage.setText(R.string.error_message);
        mMovieList = (RecyclerView) mView.findViewById(R.id.rv_movie_list);
        mMovieList.setLayoutManager(gridLayoutManager);
        movieListAdapter = new MovieListAdapter(getActivity(), this);

        if (savedInstanceState!=null)
            movieArrayList = savedInstanceState.getParcelableArrayList(MY_LIST);
        else
            fetchMovieList(PreferenceUtils.currentSortOrder(getActivity()));

        mMovieList.setAdapter(movieListAdapter);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        return mView;
    }

    public void showErrorMessage() {
        progressBar.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
        mMovieList.setVisibility(View.INVISIBLE);
    }

    public void showResponse() {
        progressBar.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
        mMovieList.setVisibility(View.VISIBLE);
    }

    public void fetchMovieList(String sortOrder) {

        progressBar.setVisibility(View.VISIBLE);
        movieArrayList.clear();

        if (NetworkUtils.isOnline(getActivity())) {

            Call<String> call = NetworkUtils.retrofitInstance().movieModelList(sortOrder);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.d(LOG_TAG, String.valueOf(response.body()));
                    showResponse();
                    if (response.isSuccessful())
                        parseJson(response.body());
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    showErrorMessage();
                    Toast.makeText(getActivity(), "An error occured", Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG, String.valueOf(t));

                }
            });

        } else {

            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();

        }

    }

    private void parseJson(String response) {

        JSONObject jsonObject;
        JSONArray jsonArray;

        try {
            jsonObject = new JSONObject(response);
            jsonArray = jsonObject.getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                MovieModel mMovieModel = new MovieModel();
                mMovieModel.setId(object.getString("id"));
                mMovieModel.setPosterPath(object.getString("poster_path"));
                mMovieModel.setTitle(object.getString("original_title"));
                mMovieModel.setBackdropPath(object.getString("backdrop_path"));
                mMovieModel.setReleaseDate(object.getString("release_date"));
                mMovieModel.setAverage(object.getString("vote_average"));
                mMovieModel.setOverview(object.getString("overview"));

                movieArrayList.add(mMovieModel);
            }

            movieListAdapter.setMovieList(movieArrayList);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void recyclerViewOnClick(int position) {
        MovieModel movieModel = movieArrayList.get(position);
        startActivity(new Intent(getActivity(),MovieDetail.class)
                .putExtra(EXTRA_PARCEABLE,movieModel));

    }

  /*  @Override
    public void onStart() {
        super.onStart();
        if(mSavedBundle!=null) {
            if (PreferenceUtils.currentSortOrder(getActivity()).equals(currentSortOrder)) {
                movieArrayList = mSavedBundle.getParcelableArrayList(MY_LIST);
                movieListAdapter.setMovieList(movieArrayList);
                showResponse();
            } else {
                fetchMovieList(PreferenceUtils.currentSortOrder(getActivity()));
            }
        } else {

        }

    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), NewSettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(getString(R.string.pref_list_key))) {
            fetchMovieList(PreferenceUtils.currentSortOrder(getActivity()));
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}