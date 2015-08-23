package com.sunmeng.educationaladministration.utils;

import java.util.ArrayList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {


    public static List<Map<String, Object>> getJsonToListMap(String jsonstr,
                                                             String[] keyArray) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            JSONArray jsonArray = new JSONArray(jsonstr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                Map<String, Object> map = new HashMap<String, Object>();
                for (int j = 0; j < keyArray.length; j++) {
                    if (object.get(keyArray[j]) != null) {
                        map.put(keyArray[j], object.get(keyArray[j]));
                    } else {
                        map.put(keyArray[j], "null");
                    }
                }
                list.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Map<String, String> getJsonToSpinnerListMap(String jsonstr,List<String> key) {
        Map<String, String> list = new HashMap<String, String>();
        try {
            JSONArray mJSONArray=new JSONArray(jsonstr);
            for (int i=0;i<mJSONArray.length();i++){
                JSONObject mJSONObject=mJSONArray.getJSONObject(i);
                list.put(mJSONObject.getString(key.get(0)),mJSONObject.getString(key.get(1)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<String> getJsonToList(String jsonstr) {
        List<String> mList=new ArrayList<String>();
        try {
        JSONArray mJSONArray=new JSONArray(jsonstr);
            for (int i=0;i<mJSONArray.length();i++){
                JSONObject mJSONObject=mJSONArray.getJSONObject(i);
                mList.add(mJSONObject.getString("kecheng"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  mList;
    }

}
