package com.sunmeng.educationaladministration.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Sunmeng on 2015-08-23.
 * Email:Sunmeng1995@outlook.com
 */
public class Utils {

    /**
     * 将星期转换为数字
     */

    public static int parseintWeekToInt(String intWeek) {
        int intintWeek = 0;
        switch (intWeek) {
            case "星期日":
                intintWeek = 7;
                break;
            case "星期一":
                intintWeek = 1;
                break;
            case "星期二":
                intintWeek = 2;
                break;
            case "星期三":
                intintWeek = 3;
                break;
            case "星期四":
                intintWeek = 4;
                break;
            case "星期五":
                intintWeek = 5;
                break;
            case "星期六":
                intintWeek = 6;
                break;
            default:
                break;
        }
        return intintWeek;
    }


    /**
     * 将List<Map<String, String>> 转为为List<String>
     * */
    public static List<String> getMapListToListString(Map<String, String> param) {
        List<String> mList = new ArrayList<String>();
        Iterator<String> it=param.keySet().iterator();
        while (it.hasNext()){
            String k=(String)it.next();
            mList.add(k);
        }
        return mList;
    }
}
