package com.threesing.macau.Support;

import com.threesing.macau.ListView.UserDataList;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;

public class Value {
    public static int language_flag;
    public static int api_flag;
    public static boolean login_in = false;
    public static JSONObject check_user;
    public static JSONObject get_user_data;
    public static JSONObject get_record;
    public static JSONArray record;
    public static UserDataList mUserDataList;
    public static List<String> user_record;
    public static String updateTime;
    public static String ver;
    public static String updatestring = "All data are based on our system | Update time : ";
    public static String copyright_text = "Copyright © 2019. All rights reserved." + " ver ";
}