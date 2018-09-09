//package dickshern.android_car_booking;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.StrictMode;
//import android.util.Log;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.newtons.restaurantmenu.UserProfile.helper.PrefsManager;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
///**
// * Created by dickshern on 27-Jan-17.
// */
//public class ViewAvailableBookingDetailsActivity extends Activity {
//
//    EditText txtBillNum;
//    TextView txtFoodItems;
//    EditText txtGross;
//    EditText txtNet;
//    EditText txtTableNum;
//    EditText txtStatus;
//    EditText txtCreatedAt;
//
//    String pid;
//    HashMap<String, String> mapDetails = new HashMap<String, String>();
//
//    // Progress Dialog
//    private ProgressDialog pDialog;
//
//    // JSON parser class
//    JSONParser jsonParser = new JSONParser();
//
//    // JSON Node names
//    private static final String TAG_SUCCESS = "success";
//    private static final String TAG_BILL = "bill";
//    private static final String TAG_BILLID = "billNum";
////    private static final String TAG_BILLITEM = "item";
//    private static final String TAG_TOTAL = "total";
//    private static final String TAG_TABLEID = "tableID";
//    private static final String TAG_STATUS = "status";
//
//    private String statusChange;
//
//    PrefsManager prefsManager;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.database_view_bill);
//
//        prefsManager = new PrefsManager(ViewBillActivity.this);
////
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//
//
//        // getting product details from intent
//        Intent i = getIntent();
//
//        // getting product id (pid) from intent
//        pid = i.getStringExtra(TAG_BILLID);
//
//        mapDetails = (HashMap<String, String>) i.getSerializableExtra(AllBillsActivity.TAG_BOOKING_ALL_DETAILS);
//
//
//        // Getting complete product details in background thread
////        new GetProductDetails().execute();
//
//        txtBillNum = (EditText) findViewById(R.id.inputBillID);
//        txtGross = (EditText) findViewById(R.id.inputGrossTotal);
//        txtNet = (EditText) findViewById(R.id.inputNetTotal);
//        txtFoodItems = (TextView) findViewById(R.id.inputFoodItems);
//        txtTableNum = (EditText) findViewById(R.id.inputSeatID);
//        txtStatus = (EditText) findViewById(R.id.inputStatus);
//
//        // display product data in EditText
//
//        txtBillNum.setText(mapDetails.get(AllBillsActivity.TAG_BILLID));
//        txtGross.setText(String.format("RM%s", mapDetails.get(AllBillsActivity.TAG_GROSS)));
//        txtNet.setText(String.format("RM%s", mapDetails.get(AllBillsActivity.TAG_NET)));
//        txtTableNum.setText(mapDetails.get(AllBillsActivity.TAG_SEAT));
////        statusChange = mapDetails.get(AllBillsActivity.TAG_STATUS);
//
////        if(statusChange.equals("0"))
////        {
////            txtStatus.setText("Unpaid");
////        }
////        else if(statusChange.equals("1"))
////        {
////            txtStatus.setText("Completed");
////        }
//
//        String foods = "";
////        for(int i = 1; i <= mapDetails.get("itemQty"); i++)
////        {
////            foods += mapDetails.get("item" + i) + " " + mapDetails.get("qty"+i) + " x " + "RM" + mapDetails.get("price"+ i) + "\n";
////        }
////        txtFoodItems.setText(foods);
//
//        txtBillNum.setFocusable(false);
//        txtGross.setFocusable(false);
//        txtNet.setFocusable(false);
//        txtFoodItems.setFocusable(false);
//        txtTableNum.setFocusable(false);
//        txtStatus.setFocusable(false);
//
//
//    }
//
//    /**
//     * Background Async Task to Get complete product details
//     * */
//    class GetProductDetails extends AsyncTask<String, String, String> {
//
//        /**
//         * Before starting background thread Show Progress Dialog
//         * */
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(ViewBillActivity.this);
//            pDialog.setMessage("Loading product details. Please wait...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(true);
//            pDialog.show();
//        }
//
//        /**
//         * Getting product details in background thread
//         * */
//        protected String doInBackground(String... params) {
//
//            // updating UI from Background Thread
//            runOnUiThread(new Runnable() {
//                public void run() {
//                    // Check for success tag
//                    int success;
//                    try {
//                        // Building Parameters
//                        List<NameValuePair> params = new ArrayList<NameValuePair>();
//                        params.add(new BasicNameValuePair(DatabaseConfig.DBAPI_ACTION,DatabaseConfig.DBAPI_VIEW_ONE_BILL));
//                        params.add(new BasicNameValuePair(TAG_BILLID, pid));
//
//                        // getting product details by making HTTP request
//                        // Note that product details url will use GET request
//                        JSONObject json = jsonParser.makeHttpRequest(DatabaseConfig.prefix + prefsManager.getIP() +
//                                DatabaseConfig.url_manage_bills, "GET", params);
//
//                        // check your log for json response
//                        Log.d("Single Product Details", json.toString());
//
//                        // json success tag
//                        success = json.getInt(TAG_SUCCESS);
//                        if (success == 1) {
//                            // successfully received product details
//                            JSONArray productObj = json
//                                    .getJSONArray(TAG_BILL); // JSON Array
//
//                            // get first product object from JSON Array
//                            JSONObject bill = productObj.getJSONObject(0);
//
//                            // product with this pid found
//                            // Edit Text
//                            txtBillNum = (EditText) findViewById(R.id.inputBillID);
//                            txtNet = (EditText) findViewById(R.id.inputNetTotal);
//                            txtFoodItems = (TextView) findViewById(R.id.inputFoodItems);
//                            txtTableNum = (EditText) findViewById(R.id.inputTableNum);
//                            txtStatus = (EditText) findViewById(R.id.inputStatus);
//
//                            // display product data in EditText
//
//                            txtBillNum.setText(bill.getString(TAG_BILLID));
//                            txtNet.setText("RM" + bill.getString(TAG_TOTAL));
//                            txtTableNum.setText(bill.getString(TAG_TABLEID));
//                            statusChange = bill.getString(TAG_STATUS);
//
//                            if(statusChange.equals("0"))
//                            {
//                                txtStatus.setText("Unpaid");
//                            }
//                            else if(statusChange.equals("1"))
//                            {
//                                txtStatus.setText("Completed");
//                            }
//
//                            String foods = "";
//                            for(int i = 1; i <= bill.getInt("itemQty"); i++)
//                            {
//                                foods += bill.getString("item" + i) + " " + bill.getString("qty"+i) + " x " + "RM" + bill.getString("price"+ i) + "\n";
//                            }
//                            txtFoodItems.setText(foods);
//
//                            txtBillNum.setFocusable(false);
//                            txtNet.setFocusable(false);
//                            txtFoodItems.setFocusable(false);
//                            txtTableNum.setFocusable(false);
//                            txtStatus.setFocusable(false);
//
//                        }else{
//                            // product with pid not found
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//
//            return null;
//        }
//
//        /**
//         * After completing background task Dismiss the progress dialog
//         * **/
//        protected void onPostExecute(String file_url) {
//            // dismiss the dialog once got all details
//            pDialog.dismiss();
//        }
//    }
//
//
//}
