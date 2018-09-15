package dickshern.android_car_booking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dickshern.android_car_booking.UserProfile.helper.PrefsManager;
import dickshern.android_car_booking.global.Helpers;

import static dickshern.android_car_booking.AllAvailableBookingsActivity.TAG_ARR_DROPOFFLOCATIONS;
import static dickshern.android_car_booking.AllAvailableBookingsActivity.TAG_ARR_LOCATION;
import static dickshern.android_car_booking.AllAvailableBookingsActivity.TAG_AVAILABLECARS;
import static dickshern.android_car_booking.AllAvailableBookingsActivity.TAG_BOOKING_ALL_DETAILS;
import static dickshern.android_car_booking.AllAvailableBookingsActivity.TAG_ID;
import static dickshern.android_car_booking.database.DatabaseConfig.WEBTAG_ARR_LOCATION;
import static dickshern.android_car_booking.database.DatabaseConfig.WEBTAG_ID;

/**
 * Created by dickshern on 09-Sept-18.
 */

public class ViewAvailableBookingDetailsActivity extends Activity {

    TextView txtId;
    TextView txtCarCount;
    TextView txtAvailableCars;
    Button btnLocation;

    HashMap<String, String> mapDetails = new HashMap<String, String>();
    String[] location = new String[0];

    PrefsManager prefsManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helpers.setFullScreen(this);
        setContentView(R.layout.database_view_bookings);

        ImageButton imgBtnBack = findViewById(R.id.imgBtnBack);
        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        prefsManager = new PrefsManager(ViewAvailableBookingDetailsActivity.this);
//
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        // getting Available Booking details from intent
        Intent i = getIntent();

        mapDetails = (HashMap<String, String>) i.getSerializableExtra(TAG_BOOKING_ALL_DETAILS);

        txtId = findViewById(R.id.tvID);
        txtCarCount = findViewById(R.id.tvCarCount);
        txtAvailableCars = findViewById(R.id.tvAvailableCars);
        btnLocation = findViewById(R.id.btnLocation);


        //Display data in page
        try {
            location = Helpers.stringToStrArray(mapDetails.get(TAG_ARR_LOCATION));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(Float.valueOf(location[0]), Float.valueOf(location[1]), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // display item data in pag
        String text = getString(R.string.label_booking_id) + mapDetails.get(TAG_ID);
        Spannable spannable = new SpannableString(text);
        spannable.setSpan(new ForegroundColorSpan(Color.CYAN), 10, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        txtId.setText(spannable, TextView.BufferType.SPANNABLE);
        try {
            location = Helpers.stringToStrArray(mapDetails.get(TAG_ARR_LOCATION));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        btnLocation.setText(Helpers.getCompleteAddress(this, Double.valueOf(location[0]), Double.valueOf(location[1])));
        final String[] finalLocation = location;
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helpers.parseToGoogleMaps(ViewAvailableBookingDetailsActivity.this,
                        Double.valueOf(finalLocation[0]), Double.valueOf(finalLocation[1]),
                        Helpers.getCompleteAddress(ViewAvailableBookingDetailsActivity.this, Double.valueOf(finalLocation[0]), Double.valueOf(finalLocation[1])));
            }
        });

        txtCarCount.setText(String.format("%s", mapDetails.get(TAG_AVAILABLECARS)));
//        txtAvailableCars.setText("");

        txtId.setFocusable(false);
        btnLocation.setFocusable(false);
        txtAvailableCars.setFocusable(false);


        //Add buttons dynamically
        //TODO:CLENAUP dynamic text
        LinearLayout dynaDropOffLocations = findViewById(R.id.dynaDropOffLocations);

        //JSON array to hashmap
        HashMap<String, String> drpLocations = new HashMap<>();

        JSONArray jsonArr;
        try {
            jsonArr = new JSONArray(mapDetails.get(TAG_ARR_DROPOFFLOCATIONS));

            for (int k = 0; k < jsonArr.length(); k++) {
                JSONObject c = jsonArr.getJSONObject(k);
                drpLocations.put(TAG_ID + k, c.getString(WEBTAG_ID ));
                drpLocations.put(TAG_ARR_LOCATION + k, c.getString(WEBTAG_ARR_LOCATION));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }




        Integer count = 0;
        for (Map.Entry<String, String> entry : drpLocations.entrySet()) {
            dynaDropOffLocations.setOrientation(LinearLayout.VERTICAL);

            LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View newView = vi.inflate(R.layout.inflate_dropoff_locations, null);

            final TextView tvAddress = newView.findViewById(R.id.tvAddress);

            try {
                if (drpLocations.containsKey(TAG_ARR_LOCATION + count)){
                    location = Helpers.stringToStrArray(drpLocations.get(TAG_ARR_LOCATION + count));

                    tvAddress.setText(Helpers.getCompleteAddress(this, Double.valueOf(location[0]), Double.valueOf(location[1])));
                    Button btnGetDirection =  newView.findViewById(R.id.btnGetDirection);
                    final String[] finalLocation1 = location;
                    btnGetDirection.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Helpers.parseToGoogleMaps(ViewAvailableBookingDetailsActivity.this,
                                    Double.valueOf(finalLocation1[0]), Double.valueOf(finalLocation1[1]),
                                    Helpers.getCompleteAddress(ViewAvailableBookingDetailsActivity.this, Double.valueOf(finalLocation1[0]), Double.valueOf(finalLocation1[1])));
                        }
                    });

                    dynaDropOffLocations.addView(newView);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            count++;
        }
    }

    public String cleanAddr(String inPart){
        if(inPart == null)
            return "";
        else
            return inPart + ",";
    }
}
