package dickshern.android_car_booking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;

import dickshern.android_car_booking.UserProfile.helper.PrefsManager;

import static dickshern.android_car_booking.AllAvailableBookingsActivity.*;
import static dickshern.android_car_booking.AllAvailableBookingsActivity.TAG_ID;

/**
 * Created by dickshern on 09-Sept-18.
 */

public class ViewAvailableBookingDetailsActivity extends Activity {

    TextView txtId;
    TextView txtLocation;
    TextView txtAvailableCars;
    TextView txtDropOffLocations;

    HashMap<String, String> mapDetails = new HashMap<String, String>();


    PrefsManager prefsManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        txtLocation =  findViewById(R.id.tvLocations);
        txtAvailableCars =  findViewById(R.id.tvAvailableCars);
        txtDropOffLocations =  findViewById(R.id.tvDropLocations);

        // display item data in page
        txtId.setText(String.format("Booking ID:%s", mapDetails.get(TAG_ID)));
        txtLocation.setText(String.format("Available Cars: %s", mapDetails.get(TAG_ARR_LOCATION)));
        txtAvailableCars.setText(String.format("%s", mapDetails.get(TAG_AVAILABLECARS)));
        txtDropOffLocations.setText(String.format("%s", mapDetails.get(TAG_ARR_DROPOFFLOCATIONS)));

        txtId.setFocusable(false);
        txtLocation.setFocusable(false);
        txtAvailableCars.setFocusable(false);
        txtDropOffLocations.setFocusable(false);
    }
}
