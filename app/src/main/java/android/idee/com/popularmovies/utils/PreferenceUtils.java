package android.idee.com.popularmovies.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.idee.com.popularmovies.R;
import android.preference.PreferenceManager;

/**
 * Created by idee on 4/12/17.
 */

public class PreferenceUtils {

    public static String currentSortOrder(Context mContext){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return prefs.getString(mContext.getString(R.string.pref_list_key), mContext.getString(R.string.default_value_list));
    }

}
