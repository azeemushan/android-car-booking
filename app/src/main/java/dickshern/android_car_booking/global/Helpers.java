package dickshern.android_car_booking.global;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dickshern.android_car_booking.R;

/**
 * Created by dickshern on 06-Sept-18.
 */

public class Helpers {

    public Dialog dialog;
    public Button btnPrimary;
    static Toast toastInstance;

    public static void replaceToast(Context otherContext, String text, int duration) {
        if (toastInstance == null) {
            toastInstance = Toast.makeText(otherContext, text, duration);
        }

        toastInstance.setText(text);
        toastInstance.setDuration(duration);
        toastInstance.show();
    }

    public static void showToast(Context otherContext, String text, int duration) {
        Toast.makeText(otherContext, text, duration).show();
    }


    public void createPopupDialogue(String title, String message, String btnMessage, Context otherContext) {
//        final Dialog dialog;
        dialog = new Dialog(otherContext);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogue_popup_generic_v2);

        //Set elements' text
        TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
        tvTitle.setText(title);
        TextView tvMessage = (TextView) dialog.findViewById(R.id.tvMessage);
        tvMessage.setText(message);
        Button btnContinue = (Button) dialog.findViewById(R.id.btnContinue);
//        btnContinue.setBackgroundColor(ContextCompat.getColor(EnterAppActivity.this,R.color.button_green));
        btnContinue.setText(btnMessage);

        ImageButton close = (ImageButton) dialog.findViewById(R.id.btnClose);
        btnPrimary = (Button) dialog.findViewById(R.id.btnContinue);

        // Close Button
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Primary Button
        btnPrimary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
    }

    public static String addQueryArg(HashMap<String, String> map){
        StringBuilder temp = new StringBuilder("?");

        int count = 0;
        for(Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (count != 0) {
                temp.append("&");
            }
            temp.append(key).append("=").append(value);
            count++;
        }
        return temp.toString();
    }

    public static String addQueryArg(JSONObject inputJSON) throws JSONException {
        StringBuilder tempStr = new StringBuilder("?");

        int count = 0;
        Iterator<String> temp;
        temp = inputJSON.keys();
        while (temp.hasNext()) {
            String key = temp.next();
            Object value = inputJSON.get(key);

            if (count != 0) {
                tempStr.append("&");
            }
            tempStr.append(key).append("=").append(value);
            count++;
        }
        return tempStr.toString();
    }

    public static void DelayedRefreshStop(final SwipeRefreshLayout swipeRefreshLayout) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

    public static String[] stringToStrArray(String inStr) throws JSONException {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(inStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String[] strArr = new String[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            strArr[i] = jsonArray.getString(i);
        }

        return strArr;
    }

    public static String getCompleteAddress(Context otherContext, double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(otherContext, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("@@@Location", strReturnedAddress.toString());
            } else {
                Log.w("@@@Location", "No address found!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("@@@Location", "Fail to get address!");
        }
        return strAdd;
    }

    public static void parseToGoogleMaps(Context otherContext, Double latitude, Double longitude, String address){
        Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + address);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(otherContext.getPackageManager()) != null) {
            otherContext.startActivity(mapIntent);
        }

    }
}
