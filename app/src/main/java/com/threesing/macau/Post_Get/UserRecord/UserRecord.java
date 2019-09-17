package com.threesing.macau.Post_Get.UserRecord;

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

public class UserRecord {

    private String TAG = "UserRecord";
    private Context context;

    public UserRecord(Context context) {
        this.context = context;
    }

    public void setConnect(String company, String account, GetUserRecord getUserRecord) {

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());

        String url = "";

        if(Value.api_flag == 0){
            url = "http://100co-kz.zyue88.com/api/get_record";
        }else if(Value.api_flag == 1){
            url = "https://api.kz168168.com/api/get_record";
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        Log.e(TAG, "剛接收 = " + response);
                        JSONObject jsonObject = new JSONObject(response);
                        getUserRecord.connected(jsonObject);
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
