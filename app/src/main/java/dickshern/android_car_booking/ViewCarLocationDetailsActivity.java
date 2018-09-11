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

import static dickshern.android_car_booking.AllCarLocationsActivity.*;

/**
 * Created by dickshern on 09-Sept-18.
 */

public class ViewCarLocationDetailsActivity extends Activity {

    TextView txtId;
    TextView txtLatitude;
    TextView txtLongitude;
    TextView txtOnTrip;

    HashMap<String, String> mapDetails = new HashMap<String, String>();


    PrefsManager prefsManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        String status;
        if(mapDetails.get(TAG_ONTRIP).equals("true")) {
            status = "Car in currently on a trip";
        }else if(mapDetails.get(TAG_ONTRIP).equals("false")) {
            status = "Car is currently on standby and available for use";
        }
        else
            status = "Status unknown";

        txtId = findViewById(R.id.tvID);
        txtLatitude =  findViewById(R.id.tvLatitude);
        txtLongitude =  findViewById(R.id.tvLongitude);
        txtOnTrip =  findViewById(R.id.tvOnTrip);

        // display item data in page
        txtId.setText(String.format("Car ID:%s", mapDetails.get(TAG_ID)));
        txtLatitude.setText(String.format("Latitude: %s", mapDetails.get(TAG_LATITUDE)));
        txtLongitude.setText(String.format("Longitude: %s", mapDetails.get(TAG_LONGITUDE)));
        txtOnTrip.setText(status);



        txtId.setFocusable(false);
        txtLatitude.setFocusable(false);
        txtLongitude.setFocusable(false);
        txtOnTrip.setFocusable(false);
    }
}
