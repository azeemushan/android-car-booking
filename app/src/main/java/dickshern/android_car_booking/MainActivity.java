package dickshern.android_car_booking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import dickshern.android_car_booking.global.Helpers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnGoToBookingAvailability;
    Button btnGoToCarLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helpers.setFullScreen(this);
        setContentView(R.layout.activity_main);

        btnGoToBookingAvailability = findViewById(R.id.btnGoToBookingAvailability);
        btnGoToBookingAvailability.setOnClickListener(this);
        btnGoToCarLocations = findViewById(R.id.btnGoToCarLocations);
        btnGoToCarLocations.setOnClickListener(this);

    }

    @Override
    public void onClick(View click) {
        int id = click.getId();
        Intent newIntent;

        switch (id) {
            case R.id.btnGoToBookingAvailability:
                newIntent = new Intent(this, AllAvailableBookingsActivity.class);
                startActivity(newIntent);
                break;

            case R.id.btnGoToCarLocations:
                newIntent = new Intent(this, AllCarLocationsActivity.class);
                startActivity(newIntent);
                break;

            default:
                Helpers.replaceToast(this, getString(R.string.system_message_button_does_not_have_function), Toast.LENGTH_SHORT);
                break;
        }

    }
}
