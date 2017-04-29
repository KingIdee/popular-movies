package com.idee.android.popularmovies.utils;

import android.content.Context;

import com.android.idee.popularmovies.BuildConfig;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by idee on 4/12/17.
 */

public class NetworkUtils {

    private final static String MOVIE_BASE_URL = "http://api.themoviedb.org/";
    private final static String PARAM_API_KEY = "api_key";

    public static boolean isOnline(Context mContext) {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public interface MovieApiClient {
        @GET("/3/movie/{sort_order}?api_key="+ BuildConfig.MOVIEDB_API_KEY)
        Call<String> movieModelList(
                @Path("sort_order") String sortOrder
        );
    }

    public static MovieApiClient retrofitInstance() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(MOVIE_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());;

        Retrofit retrofit = builder
                .client(httpClient.build())
                .build();

        return retrofit.create(MovieApiClient.class);

    }

}
