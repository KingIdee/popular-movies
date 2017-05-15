package com.idee.android.popularmovies.data.provider;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by idee on 5/2/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "movie.db";
    private final static int DATABASE_VERSION = 1;
    Context context;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE " +
                MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL,"+
                MovieContract.MovieEntry.COLUMN_MOVIE_AVERAGE + " FLOAT NOT NULL,"+
                MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL"+
                ");";

        Log.d("TAG",CREATE_TABLE);

        try {
            db.execSQL(CREATE_TABLE);
        }  catch (SQLException e){
            e.printStackTrace();
            Log.d("TAG", e.toString());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }

}
