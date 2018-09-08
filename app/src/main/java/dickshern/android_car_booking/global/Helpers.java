package dickshern.android_car_booking.global;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
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


}
