package dickshern.android_car_booking;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;

import dickshern.android_car_booking.UserProfile.helper.PrefsManager;
import dickshern.android_car_booking.global.Helpers;

import static dickshern.android_car_booking.AllCarLocationsActivity.TAG_CAR_LOCATIONS_ALL_DETAILS;
import static dickshern.android_car_booking.AllCarLocationsActivity.TAG_ID;
import static dickshern.android_car_booking.AllCarLocationsActivity.TAG_LATITUDE;
import static dickshern.android_car_booking.AllCarLocationsActivity.TAG_LONGITUDE;
import static dickshern.android_car_booking.AllCarLocationsActivity.TAG_ONTRIP;

/**
 * Created by dickshern on 09-Sept-18.
 */

public class ViewCarLocationDetailsActivity extends Activity {

    TextView txtId;
    Button btnLocation;
    TextView txtOnTrip;

    HashMap<String, String> mapDetails = new HashMap<String, String>();


    PrefsManager prefsManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helpers.setFullScreen(this);
        setContentView(R.layout.database_view_car_locations);

        ImageButton imgBtnBack = findViewById(R.id.imgBtnBack);
        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        prefsManager = new PrefsManager(ViewCarLocationDetailsActivity.this);
//
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        // getting Car Location details details from intent
        Intent i = getIntent();

        mapDetails = (HashMap<String, String>) i.getSerializableExtra(TAG_CAR_LOCATIONS_ALL_DETAILS);

        txtId = findViewById(R.id.tvID);
        btnLocation = findViewById(R.id.btnLocation);
        txtOnTrip =  findViewById(R.id.tvOnTrip);

        // display item data in page
        txtId.setText(String.format(getString(R.string.label_car_id), mapDetails.get(TAG_ID)));

        btnLocation.setText(Helpers.getCompleteAddress(this, Double.valueOf(mapDetails.get(TAG_LATITUDE)), Double.valueOf(mapDetails.get(TAG_LONGITUDE))));
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helpers.parseToGoogleMaps(ViewCarLocationDetailsActivity.this,
                        Double.valueOf(mapDetails.get(TAG_LATITUDE)), Double.valueOf(mapDetails.get(TAG_LONGITUDE)),
                        Helpers.getCompleteAddress(ViewCarLocationDetailsActivity.this, Double.valueOf(mapDetails.get(TAG_LATITUDE)), Double.valueOf(mapDetails.get(TAG_LONGITUDE))));
            }
        });

        String status;
        if(mapDetails.get(TAG_ONTRIP).equals("true")) {
            status = getString(R.string.message_car_on_trip);
            txtOnTrip.setTextColor(Color.RED);
        }else if(mapDetails.get(TAG_ONTRIP).equals("false")) {
            status = getString(R.string.message_car_available);
            txtOnTrip.setTextColor(Color.GREEN);
        }
        else
            status = getString(R.string.message_status_unknown);
        txtOnTrip.setText(status);
        txtOnTrip.setTextSize(24);

        txtId.setFocusable(false);
        txtOnTrip.setFocusable(false);
    }
}
