package com.threesing.macau.Post_Get.Message;

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

public class Post {

    private String TAG = "Post";
    private Context context;
    private String[] apiurl = {
            "http://100co-kz.zyue88.com/api/get_post",
            "https://api.kz168168.com/api/get_post",
            "http://mc-kz.zyue88.com/api/get_post",
            "http://fb-kz.zyue88.com/api/get_post",
            "http://peter-kz.zyue88.com/api/get_post",
            "http://demo.kz168168.com/api/get_post",
            "http://nuba.kz168168.com/api/get_post"
    };

    public Post(Context context) {
        this.context = context;
    }

    public void setConnect(String company, String account, GetPost getPost) {

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());

        String url = apiurl[Value.api_flag];

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        getPost.getMessage(jsonObject);
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
