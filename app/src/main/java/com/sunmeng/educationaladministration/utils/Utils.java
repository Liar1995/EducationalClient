package com.sunmeng.educationaladministration.utils;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
     */
    public static List<String> getMapListToListString(Map<String, String> param) {
        List<String> mList = new ArrayList<String>();
        Iterator<String> it = param.keySet().iterator();
        while (it.hasNext()) {

            String k = (String) it.
                    next();
            mList.add(k);
        }
        return mList;
    }

    public static int getMapListId(Map<String, String> param, String name) {

        Iterator<String> it = param.keySet().iterator();
        int i = 0;
        while (it.hasNext()) {

            String k = (String) it.next();
            if (k.equals(name)) {

                return i;

            }
            i++;
        }
        return i;
    }

    /**
     * 对日期进行计算
     */
    public static String calculationDate(String paramDate, int dataInt) {

        SimpleDateFormat sdf = null;
        Date dt1 = null;
        try {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(paramDate);
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DAY_OF_YEAR, dataInt);
            dt1 = rightNow.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return sdf.format(dt1);
    }


    public static String getWeek(String date) {
        String Week = "星期";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String pTime = date;
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                Week += "日";
                break;
            case 2:
                Week += "一";
                break;
            case 3:
                Week += "二";
                break;
            case 4:
                Week += "三";
                break;
            case 5:
                Week += "四";
                break;
            case 6:
                Week += "五";
                break;
            case 7:
                Week += "六";
                break;
            default:
                break;
        }
        return Week;
    }

}
