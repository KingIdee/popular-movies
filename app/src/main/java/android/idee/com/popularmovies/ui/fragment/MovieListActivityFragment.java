package android.idee.com.popularmovies.ui.fragment;

import android.content.Intent;
import android.idee.com.popularmovies.R;
import android.idee.com.popularmovies.data.model.MovieModel;
import android.idee.com.popularmovies.ui.activity.MovieDetail;
import android.idee.com.popularmovies.ui.activity.SettingsActivity;
import android.idee.com.popularmovies.ui.adapter.MovieListAdapter;
import android.idee.com.popularmovies.utils.NetworkUtils;
import android.idee.com.popularmovies.utils.PreferenceUtils;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */

public class MovieListActivityFragment extends Fragment implements MovieListAdapter.ItemClickListener {

    private static final String LOG_TAG = MovieListActivityFragment.class.getSimpleName();
    private static final String CURRENT_SORT_ORDER = "current_sort_order";
    private static final String MY_LIST = "rv_array_list";
    public static final String EXTRA_PARCEABLE = "extra_parceable";
    private ArrayList<MovieModel> movieArrayList = new ArrayList<>();
    MovieListAdapter movieListAdapter;
    ProgressBar progressBar;
    TextView errorMessage;
    private String currentSortOrder;
    RecyclerView mMovieList;
    Bundle mSavedBundle;

    public MovieListActivityFragment() {}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(CURRENT_SORT_ORDER, currentSortOrder);
        outState.putParcelableArrayList(MY_LIST, movieArrayList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.fragment_movie_list, container, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);

        mSavedBundle=savedInstanceState;
        setHasOptionsMenu(true);

        progressBar = (ProgressBar) mView.findViewById(R.id.pb_loading_movie_list);
        errorMessage = (TextView) mView.findViewById(R.id.tv_error_message);
        errorMessage.setText(R.string.error_message);
        mMovieList = (RecyclerView) mView.findViewById(R.id.rv_movie_list);
        mMovieList.setLayoutManager(gridLayoutManager);
        movieListAdapter = new MovieListAdapter(getActivity(), this);

        if (savedInstanceState!=null){
            movieArrayList =  savedInstanceState.getParcelableArrayList(MY_LIST);

        }

        movieListAdapter.setMovieList(movieArrayList);
        mMovieList.setAdapter(movieListAdapter);

        return mView;
    }

    public void showErrorMessage() {
        progressBar.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
    }

    public void showResponse() {
        progressBar.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
    }

    public void fetchMovieList(String sortOrder) {

        progressBar.setVisibility(View.VISIBLE);
        movieArrayList.clear();
        currentSortOrder = sortOrder;

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
                movieListAdapter.notifyDataSetChanged();
            }

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

    @Override
    public void onStart() {
        super.onStart();
        if (mSavedBundle!=null){
            if (PreferenceUtils.currentSortOrder(getActivity()).equals(mSavedBundle.getString(CURRENT_SORT_ORDER))){
                movieArrayList = mSavedBundle.getParcelableArrayList(MY_LIST);
                movieListAdapter.setMovieList(movieArrayList);
                showResponse();
            } else {
                fetchMovieList(PreferenceUtils.currentSortOrder(getActivity()));
            }

        } else {
            fetchMovieList(PreferenceUtils.currentSortOrder(getActivity()));
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}