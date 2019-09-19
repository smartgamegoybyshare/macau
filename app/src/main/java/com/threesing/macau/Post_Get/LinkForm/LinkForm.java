package com.threesing.macau.Post_Get.LinkForm;

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

public class LinkForm {

    private String TAG = "LinkForm";
    private Context context;
    private String[] apiurl = {
            "http://100co-kz.zyue88.com/api/link_form",
            "https://api.kz168168.com/api/link_form",
            "http://mc-kz.zyue88.com/api/link_form",
            "http://fb-kz.zyue88.com/api/link_form",
            "http://peter-kz.zyue88.com/api/link_form",
            "http://demo.kz168168.com/api/link_form",
            "http://nuba.kz168168.com/api/link_form"
    };

    public LinkForm(Context context) {
        this.context = context;
    }

    public void setConnect(String company, String account, String new_company, String new_account, String password,
                           GetLinkForm getLinkForm) {

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());

        String url = apiurl[Value.api_flag];

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        Log.e(TAG, "LinkFormresponse = " + response);
                        JSONObject jsonObject = new JSONObject(response);
                        getLinkForm.toLink(jsonObject);
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
                params.put("to_code", new_company);
                params.put("to_user", new_account);
                params.put("to_password", password);
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
