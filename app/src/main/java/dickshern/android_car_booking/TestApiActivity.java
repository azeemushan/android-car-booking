package dickshern.android_car_booking;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import dickshern.android_car_booking.UserProfile.helper.PrefsManager;
import dickshern.android_car_booking.global.Helpers;
import dickshern.android_car_booking.http.HttpHandler;
import dickshern.android_car_booking.http.HttpResponse;

import static dickshern.android_car_booking.database.DatabaseConfig.WEBTAG_ARR_DROPOFFLOCATIONS;
import static dickshern.android_car_booking.database.DatabaseConfig.WEBTAG_ARR_LOCATION;
import static dickshern.android_car_booking.database.DatabaseConfig.WEBTAG_AVAILABLECARS;
import static dickshern.android_car_booking.database.DatabaseConfig.WEBTAG_DATA;
import static dickshern.android_car_booking.database.DatabaseConfig.WEBTAG_END_TIME;
import static dickshern.android_car_booking.database.DatabaseConfig.WEBTAG_ERROR;
import static dickshern.android_car_booking.database.DatabaseConfig.WEBTAG_ID;
import static dickshern.android_car_booking.database.DatabaseConfig.WEBTAG_LATITUDE;
import static dickshern.android_car_booking.database.DatabaseConfig.WEBTAG_LONGITUDE;
import static dickshern.android_car_booking.database.DatabaseConfig.WEBTAG_MESSAGE;
import static dickshern.android_car_booking.database.DatabaseConfig.WEBTAG_ONTRIP;
import static dickshern.android_car_booking.database.DatabaseConfig.WEBTAG_START_TIME;
import static dickshern.android_car_booking.database.DatabaseConfig.WEBTAG_SUCCESS;
import static dickshern.android_car_booking.database.DatabaseConfig.webEPBookingAvailability;
import static dickshern.android_car_booking.database.DatabaseConfig.webEPCarLocation;

public class TestApiActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnGetBookingAvailability;
    Button btnGetCarLocations;

    EditText etOne;
    EditText etTwo;

    ListView lv;

//    HashMap<String, CustomHashMap> settingsMap = new HashMap<String, CustomHashMap>();

    private Helpers helpers = new Helpers();

    PrefsManager prefsManager;

    ArrayList<HashMap<String, String>> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_api_layout);

        prefsManager = new PrefsManager(TestApiActivity.this);

        btnGetBookingAvailability = findViewById(R.id.btnGetBookingAvailability);
        btnGetBookingAvailability.setOnClickListener(this);
        btnGetCarLocations = findViewById(R.id.btnGetCarLocations);
        btnGetCarLocations.setOnClickListener(this);

        etOne = findViewById(R.id.etOne);
        etTwo = findViewById(R.id.etTwo);

        lv = findViewById(R.id.listViewMain);

        new webGetBooking().execute();
    }

    @Override
    public void onClick(View click) {
        int id = click.getId();

        switch (id) {
            case R.id.btnGetBookingAvailability:
                new webGetBooking().execute();
                Helpers.replaceToast(this, "Retrieving available bookings", Toast.LENGTH_SHORT);
                break;

            case R.id.btnGetCarLocations:
                new webGetCarLocation().execute();
                Helpers.replaceToast(this, "Retrieving available bookings", Toast.LENGTH_SHORT);
                break;

            default:
                Helpers.replaceToast(this, getString(R.string.system_message_button_does_not_have_function), Toast.LENGTH_SHORT);
                break;
        }

    }


    public void reset() {
        jsonMap = new HashMap<>();
        tempMessage = "";
        listItems = new ArrayList<HashMap<String, String>>();
    }

    ProgressDialog pDialog;
    HashMap<String, String> jsonMap = new HashMap<>();
    HashMap<String, String> tempMessageMap = new HashMap<>();
    String tempMessage = "";


    private class webGetBooking extends AsyncTask<Void, Void, HashMap<String, String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(TestApiActivity.this);
            pDialog.setMessage(getString(R.string.loading_custom, "Retrieving available bookings"));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

            reset();
        }

        @Override
        protected HashMap<String, String> doInBackground(Void... arg0) {
            HttpHandler httpHandler = new HttpHandler(TestApiActivity.this);
            HashMap<String, String> params = new HashMap<>();

            long startTime = System.currentTimeMillis() / 1000;
            long endtime = (System.currentTimeMillis() / 1000) + 3600;


            Log.e("@@@START TIME", String.valueOf(startTime));
            Log.e("@@@END  TIME", String.valueOf(endtime));
            params.put(WEBTAG_START_TIME, String.valueOf(startTime));
            params.put(WEBTAG_END_TIME, String.valueOf(endtime));

            JSONObject jsonParam = new JSONObject();
            try {
                jsonParam.put(WEBTAG_START_TIME, String.valueOf(startTime));
                jsonParam.put(WEBTAG_END_TIME, String.valueOf(endtime));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            HttpResponse response = httpHandler.makeHttpRequest(webEPBookingAvailability, "GET", jsonParam, false);
            String jsonStr = response.getResponse();

            if (jsonStr != null) {
                try {

                    switch (response.getHTTPCode()) {
                        case HttpsURLConnection.HTTP_OK:

                            JSONObject jsonobj = new JSONObject(jsonStr);
                            JSONArray jsonArr = jsonobj.getJSONArray(WEBTAG_DATA);

                            // looping through all Bookings
                            for (int i = 0; i < jsonArr.length(); i++) {
                                JSONObject c = jsonArr.getJSONObject(i);

                                // creating new HashMap
                                HashMap<String, String> map = new HashMap<String, String>();

                                map.put(WEBTAG_ID, c.getString(WEBTAG_ID));
                                map.put(WEBTAG_ARR_LOCATION, c.getString(WEBTAG_ARR_LOCATION));
                                map.put(WEBTAG_AVAILABLECARS, c.getString(WEBTAG_AVAILABLECARS));
                                map.put(WEBTAG_ARR_DROPOFFLOCATIONS, c.getString(WEBTAG_ARR_DROPOFFLOCATIONS));

                                Log.d("MAP", String.valueOf(map));
                                // adding HashList to ArrayList
                                listItems.add(map);
                            }

                            tempMessageMap.put(WEBTAG_SUCCESS, tempMessage);
                            return tempMessageMap;
                        default:
                            jsonobj = new JSONObject(jsonStr);
                            tempMessage += jsonobj.getString(WEBTAG_MESSAGE);

                            tempMessageMap.put(WEBTAG_MESSAGE, tempMessage);
                            return tempMessageMap;
                    }
                } catch (final JSONException e) {
                    Log.e("@@@DEBUG", getString(R.string.system_message_error_parsing_json) + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    getString(R.string.system_message_error_parsing_json) + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e("@@@DEBUG", getString(R.string.system_message_fail_get_json_server));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.system_message_fail_get_json_server),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> response) {
            // dismiss the dialog once done
            if (pDialog.isShowing())
                pDialog.dismiss();
            String key = "";
            String value = "";

            if (response != null) {
                for (Map.Entry<String, String> entry : response.entrySet()) {
                    key = entry.getKey();
                    value = entry.getValue();
                }

                if (key.equals(WEBTAG_SUCCESS)) {
                    //updating UI from Background Thread
                    runOnUiThread(new Runnable() {
                        public void run() {
                            /**
                             * Updating parsed JSON data into ListView
                             * */
                            ListAdapter adapter = new SimpleAdapter(
                                    TestApiActivity.this, listItems,
                                    R.layout.database_list_booking_availability, new String[]{WEBTAG_ID,
                                    WEBTAG_ARR_LOCATION, WEBTAG_AVAILABLECARS, WEBTAG_ARR_DROPOFFLOCATIONS},
                                    new int[]{R.id.listTVID, R.id.listTVLocation, R.id.tvAvailableCars, R.id.listTVDropOffLocations});
                            //updating listview
                            lv.setAdapter(adapter);
                        }
                    });
                } else{
                    Helpers.replaceToast(TestApiActivity.this, value, Toast.LENGTH_SHORT);

                    //updating UI from Background Thread
                    runOnUiThread(new Runnable() {
                        public void run() {
                            /**
                             * Updating parsed JSON data into ListView
                             * */
                            ListAdapter adapter = new SimpleAdapter(
                                    TestApiActivity.this, listItems,
                                    R.layout.database_list_error, new String[]{WEBTAG_ERROR},
                                    new int[]{R.id.tvError});
                            //updating listview
                            lv.setAdapter(adapter);
                        }
                    });
                }
            } else
                Helpers.replaceToast(TestApiActivity.this, getString(R.string.custom_message_error, getString(R.string.system_response_null)), Toast.LENGTH_SHORT);
        }
    }




    private class webGetCarLocation extends AsyncTask<Void, Void, HashMap<String, String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(TestApiActivity.this);
            pDialog.setMessage(getString(R.string.loading_custom, "Retrieving Car Locations"));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

            reset();
        }

        @Override
        protected HashMap<String, String> doInBackground(Void... arg0) {
            HttpHandler httpHandler = new HttpHandler(TestApiActivity.this);
            HashMap<String, String> params = new HashMap<>();

            HttpResponse response = httpHandler.makeHttpRequest(webEPCarLocation, "GET", params, false);
            String jsonStr = response.getResponse();

            if (jsonStr != null) {
                try {

                    switch (response.getHTTPCode()) {
                        case HttpsURLConnection.HTTP_OK:

                            JSONObject jsonobj = new JSONObject(jsonStr);
                            JSONArray jsonArr = jsonobj.getJSONArray(WEBTAG_DATA);

                            // looping through Car |Locations
                            for (int i = 0; i < jsonArr.length(); i++) {
                                JSONObject c = jsonArr.getJSONObject(i);

                                // creating new HashMap
                                HashMap<String, String> map = new HashMap<String, String>();

                                // Storing each json item in variable
                                map.put(WEBTAG_ID, c.getString(WEBTAG_ID));
                                map.put(WEBTAG_LATITUDE + WEBTAG_LONGITUDE, c.getString(WEBTAG_LATITUDE) + ", " + c.getString(WEBTAG_LONGITUDE));
                                map.put(WEBTAG_ONTRIP, c.getString(WEBTAG_ONTRIP));

                                Log.d("MAP", String.valueOf(map));
                                // adding HashList to ArrayList
                                listItems.add(map);
                            }
//
//
                            tempMessageMap.put(WEBTAG_SUCCESS, tempMessage);
                            return tempMessageMap;
                        default:
                            jsonobj = new JSONObject(jsonStr);
                            tempMessage += jsonobj.getString(WEBTAG_MESSAGE);

                            tempMessageMap.put(WEBTAG_MESSAGE, tempMessage);
                            return tempMessageMap;
                    }
                } catch (final JSONException e) {
                    Log.e("@@@DEBUG", getString(R.string.system_message_error_parsing_json) + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    getString(R.string.system_message_error_parsing_json) + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e("@@@DEBUG", getString(R.string.system_message_fail_get_json_server));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.system_message_fail_get_json_server),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }

            return tempMessageMap;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> response) {
            // dismiss the dialog once done
            if (pDialog.isShowing())
                pDialog.dismiss();
            String key = "";
            String value = "";

            if (response != null) {
                for (Map.Entry<String, String> entry : response.entrySet()) {
                    key = entry.getKey();
                    value = entry.getValue();
                }

                if (key.equals(WEBTAG_SUCCESS)) {
                    //updating UI from Background Thread
                    runOnUiThread(new Runnable() {
                        public void run() {
                            /**
                             * Updating parsed JSON data into ListView
                             * */
                            ListAdapter adapter = new SimpleAdapter(
                                    TestApiActivity.this, listItems,
                                    R.layout.database_list_car_locations, new String[]{WEBTAG_ID,
                                    WEBTAG_LATITUDE + WEBTAG_LONGITUDE, WEBTAG_ONTRIP},
                                    new int[]{R.id.listTVID, R.id.listTVLocation, R.id.listTVOnTrip});

                            //updating listview
                            lv.setAdapter(adapter);
                        }
                    });
                } else
                    Helpers.replaceToast(TestApiActivity.this, value, Toast.LENGTH_SHORT);
            } else
                Helpers.replaceToast(TestApiActivity.this, getString(R.string.custom_message_error, getString(R.string.system_response_null)), Toast.LENGTH_SHORT);
        }
    }




    public void popUpMessage(final boolean connectSuccess) {
        final Dialog dialog;
        dialog = new Dialog(TestApiActivity.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogue_popup_generic_v2);

        Button btnContinue = (Button) dialog.findViewById(R.id.btnContinue);
        // set the custom dialog components - text, image and button
        if (connectSuccess) {
//            ImageView ivWelcomeImage = (ImageView) dialog.findViewById(R.id.ivWelcomeImage);
            TextView tvWelcome = (TextView) dialog.findViewById(R.id.tvTitle);
            tvWelcome.setText(getString(R.string.popup_message_start_welcome));
            btnContinue.setBackgroundColor(ContextCompat.getColor(
                    TestApiActivity.this, R.color.button_green));
            btnContinue.setText(getString(R.string.default_messsage_continue));
        }


        ImageButton close = (ImageButton) dialog.findViewById(R.id.btnClose);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectSuccess) {
                    Intent newIntent = new Intent(TestApiActivity.this, TestApiActivity.class);
                    startActivity(newIntent);
                }
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
    }

}
