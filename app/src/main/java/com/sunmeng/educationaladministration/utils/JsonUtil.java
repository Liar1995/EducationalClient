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

    public static Map<String, String> getJsonToSpinnerListMap(String jsonstr, List<String> key) {
        Map<String, String> list = new HashMap<String, String>();
        try {
            JSONArray mJSONArray = new JSONArray(jsonstr);
            for (int i = 0; i < mJSONArray.length(); i++) {
                JSONObject mJSONObject = mJSONArray.getJSONObject(i);
                list.put(mJSONObject.getString(key.get(0)), mJSONObject.getString(key.get(1)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<String> getJsonToList(String jsonstr) {
        List<String> mList = new ArrayList<String>();
        try {
            JSONArray mJSONArray = new JSONArray(jsonstr);
            for (int i = 0; i < mJSONArray.length(); i++) {
                JSONObject mJSONObject = mJSONArray.getJSONObject(i);
                mList.add(mJSONObject.getString("toname"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mList;
    }

    public static List<Map<String, Object>> getJsonToListMap(String jsonstr, int week) {
        List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
        try {
            JSONObject jsonObject = new JSONObject(jsonstr);
            JSONArray jsonArray = jsonObject.getJSONArray("" + week);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObjectTwo = jsonArray.getJSONObject(i);
                Map<String,Object> map=new HashMap<String,Object>();
                map.put("totime",jsonObjectTwo.getString("totime"));
                map.put("location",jsonObjectTwo.getString("location"));
                map.put("clid",jsonObjectTwo.getString("clid"));
                map.put("weid",jsonObjectTwo.getString("weid"));
                map.put("tiid",jsonObjectTwo.getString("tiid"));
                map.put("cnname",jsonObjectTwo.getString("cnname"));
                map.put("teid",jsonObjectTwo.getString("teid"));
                map.put("cnid",jsonObjectTwo.getString("cnid"));
                map.put("toname",jsonObjectTwo.getString("toname"));
                map.put("tename",jsonObjectTwo.getString("tename"));
                map.put("toid", jsonObjectTwo.getString("toid"));
                map.put("wename",jsonObjectTwo.getString("wename"));
                map.put("clname",jsonObjectTwo.getString("clname"));
                tempList.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tempList;
    }

}
