package com.threesing.macau.Post_Get.UserData;

import android.content.Context;
import android.util.Log;
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

public class ConnectUserDataBase {

    private String TAG = "ConnectUserDataBase";
    private Context context;
    private String[] apiurl = {
            "http://100co-kz.zyue88.com/api/get_user_data",
            "https://api.kz168168.com/api/get_user_data",
            "http://mc-kz.zyue88.com/api/get_user_data",
            "http://fb-kz.zyue88.com/api/get_user_data",
            "http://peter-kz.zyue88.com/api/get_user_data",
            "http://demo.kz168168.com/api/get_user_data",
            "http://nuba.kz168168.com/api/get_user_data"
    };

    public ConnectUserDataBase(Context context) {
        this.context = context;
    }

    public void setConnect(String company, String account, GetUserData getUserData) {

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());

        String url = apiurl[Value.api_flag];

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        getUserData.connected(jsonObject);
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
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);
    }
}
