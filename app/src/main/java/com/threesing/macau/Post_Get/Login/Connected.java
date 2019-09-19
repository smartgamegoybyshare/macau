package com.threesing.macau.Post_Get.Login;

import android.content.Context;
import android.util.Log;
import android.widget.CheckBox;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.threesing.macau.Support.Value;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Connected {

    private static final String TAG = "Connected";
    private Context context;
    private String[] apiurl = {
            "http://100co-kz.zyue88.com/api/check_user",
            "https://api.kz168168.com/api/check_user",
            "http://mc-kz.zyue88.com/api/check_user",
            "http://fb-kz.zyue88.com/api/check_user",
            "http://peter-kz.zyue88.com/api/check_user",
            "http://demo.kz168168.com/api/check_user",
            "http://nuba.kz168168.com/api/check_user"
    };

    public Connected(Context context) {
        this.context = context;
        Value.api_count = apiurl.length;
        Value.api_flag = 0;
    }

    public void setConnect(String company, String account, String password, GetConnect getConnect,
                           CheckBox checkBox) {

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());

        String url = apiurl[Value.api_flag];

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Log.e(TAG, "jsonObject = " + jsonObject);
                        Log.e(TAG, "url = " + url);
                        getConnect.connected(jsonObject, company, account, password, checkBox);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e(TAG, "VolleyError = " + error)) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("code", company);
                params.put("identity", account);
                params.put("password", password);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new

                DefaultRetryPolicy(
                5000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);
    }
}
