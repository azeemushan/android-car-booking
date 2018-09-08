package dickshern.android_car_booking.UserProfile.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class PrefsManager {
    // LogCat tag
    private static String TAG = PrefsManager.class.getSimpleName();

    // Shared Preferences
    public SharedPreferences prefs;

    public Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    //SHARED PREFERENCES
    public static final String prefsMyApp = "MyApp";
    public static final String PREFS_LOGIN = "isLoggedIn";
    public static final String PREFS_AUTH_TOKEN = "auth_token";

    // Shared preferences file name

    private static final String KEY_IS_LOGGED_IN = PREFS_LOGIN;


    public PrefsManager(Context context) {
        this._context = context;
        prefs = _context.getSharedPreferences(prefsMyApp, PRIVATE_MODE);
        editor = prefs.edit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);

        // commit changes
//		editor.commit();
        editor.apply();
        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void deleteLogin() {
        editor.remove(PrefsManager.PREFS_LOGIN);
        editor.apply();
    }

    public void setToken(String newToekn) {
        editor.putString(PREFS_AUTH_TOKEN, newToekn);
        editor.apply();
//		Log.d(TAG, "Auth token saved!" + getToken());
    }

    public String getToken() {
        return prefs.getString(PREFS_AUTH_TOKEN, null);
    }

    public void setCustomPrefs(String prefsKey, String prefsValue) {
        String oldPref = prefs.getString(prefsKey, null);
        editor.putString(prefsKey, prefsValue);
        editor.apply();

        if (oldPref == null)
            Log.d(TAG, "Pref saved! " + prefsKey + ' ' + prefsValue);
        else
            Log.d(TAG, "Pref updated! " + prefsKey + ' ' + prefsValue);
    }

    public String getCustomPrefs(String prefsKey) {
        return prefs.getString(prefsKey, null);
    }

    public void setCustomPrefsBoolean(String prefsKey, Boolean prefsValue) {
        boolean oldPref = prefs.getBoolean(prefsKey, false);
        editor.putBoolean(prefsKey, prefsValue);
        editor.apply();

        if (!oldPref)
            Log.d(TAG, "Pref updated to TRUE" + prefsKey + ' ' + prefsValue);
        else
            Log.d(TAG, "Pref updated to FALSE " + prefsKey + ' ' + prefsValue);
    }

    public Boolean getCustomPrefsBoolean(String prefsKey) {
        return prefs.getBoolean(prefsKey, false);
    }

}
