package dickshern.android_car_booking.http;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import dickshern.android_car_booking.UserProfile.helper.PrefsManager;
import dickshern.android_car_booking.global.Helpers;

import static dickshern.android_car_booking.database.DatabaseConfig.WEBTAG_ACCEPT;
import static dickshern.android_car_booking.database.DatabaseConfig.WEBTAG_AUTHORIZATIONN;
import static dickshern.android_car_booking.database.DatabaseConfig.WEBTAG_TOKEN;
import static dickshern.android_car_booking.database.DatabaseConfig.WEB_PROPERTY_HEADER;

public class HttpHandler {

    PrefsManager prefsManager;

    private static final String TAG = HttpHandler.class.getSimpleName();

    public HttpHandler(Context otherContext) {
        prefsManager = new PrefsManager(otherContext);
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    public HttpResponse makeHttpRequest(String requestURL, String method,
                                        HashMap<String, String> postDataParams, Boolean usesRequestBody) {
        URL url;
        String response = "";
        int responseCode = 0;

        try {
            if(method == "GET")
                requestURL += Helpers.addQueryArg(postDataParams);

            Log.e("@@@URL", requestURL);

            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);

            if (!method.equals(WEB_PROPERTY_HEADER)) {
                conn.setRequestMethod(method);
            }

            conn.setDoInput(true);
//            conn.setDoOutput(true);


            if (usesRequestBody) {
                if (postDataParams.get(WEBTAG_TOKEN) != null && prefsManager.getToken() != null) {
                    conn.addRequestProperty(WEBTAG_ACCEPT, "application/json");
                    conn.addRequestProperty(WEBTAG_AUTHORIZATIONN
                            , "Bearer " + prefsManager.getToken());
                } else {
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(postDataParams));

                    writer.flush();
                    writer.close();
                    os.close();
                }
            }


            responseCode = conn.getResponseCode();

            Log.d("@@@HTTP CODE", String.valueOf(responseCode));
            if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_CREATED && responseCode != HttpURLConnection.HTTP_NO_CONTENT)
                response = convertStreamToString(conn.getErrorStream());
            else
                response = convertStreamToString(conn.getInputStream());
            Log.d("@@@RESPONSE", response);

//            if (responseCode == HttpsURLConnection.HTTP_OK) {
////                String line;
////                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
////                while ((line = br.readLine()) != null) {
////                    response += line;
////                }
//                response = convertStreamToString(conn.getInputStream());
//            } else {
//                response = "";
//
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new HttpResponse(responseCode, response);
    }

    //Http Request with JSON Object payload
    public HttpResponse makeHttpRequest(String requestURL, String method,
                                        JSONObject postJSONParams, Boolean usesRequestBody) {
        URL url;
        String response = "";
        int responseCode = 0;

        try {
            if(method == "GET")
                requestURL += Helpers.addQueryArg(postJSONParams);

            Log.e("@@@URL", requestURL);

            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);

            if (!method.equals(WEB_PROPERTY_HEADER)) {
                conn.setRequestMethod(method);
            }

            conn.setDoInput(true);
//            conn.setDoOutput(true);

            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            Log.e("TOKEN", String.valueOf(postJSONParams.has(WEBTAG_TOKEN)));
            if (usesRequestBody) {
                //If using OAuth in Authorization header
                if (postJSONParams.has(WEBTAG_TOKEN) && prefsManager.getToken() != null) {
                    conn.addRequestProperty(WEBTAG_ACCEPT, "application/json");
                    conn.addRequestProperty(WEBTAG_AUTHORIZATIONN
                            , "Bearer " + prefsManager.getToken());
                    postJSONParams.remove(WEBTAG_TOKEN);
                    Log.e("JSON OBJECT", String.valueOf(postJSONParams));
                } else {
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(postJSONParams.toString());

                    Log.e("JSON OBJECT", String.valueOf(postJSONParams));
                    writer.flush();
                    writer.close();
                    os.close();
                }
            }


            responseCode = conn.getResponseCode();

            Log.d("@@@HTTP CODE", String.valueOf(responseCode));
            if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_CREATED && responseCode != HttpURLConnection.HTTP_NO_CONTENT)
                response = convertStreamToString(conn.getErrorStream());
            else
                response = convertStreamToString(conn.getInputStream());
            Log.d("@@@RESPONSE", response);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new HttpResponse(responseCode, response);
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}