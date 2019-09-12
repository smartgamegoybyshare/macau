package com.threesing.macau.Post_Get.Login;

import android.content.Context;
import android.util.Log;
import android.widget.CheckBox;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Connected {

    private static final String TAG = "Connected";
    private Context context;

    public Connected(Context context) {
        this.context = context;
    }

    public void setConnect(String company, String account, String password, GetConnect getConnect,
                           CheckBox checkBox) {

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());

        String url = "https://api.kz168168.com/api/check_user"; //http://100co-kz.zyue88.com/api/check_user

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
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
