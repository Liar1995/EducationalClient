package com.sunmeng.educationaladministration;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sunmeng.educationaladministration.adapter.SecheduleAdapter;
import com.sunmeng.educationaladministration.net_utils.HttpClientUtil;
import com.sunmeng.educationaladministration.utils.JsonUtil;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecheduleActivity extends Activity {

    @ViewInject(R.id.gv_schedules)
    private GridView gridView;

    /**
     * 日期选择
     */
    @ViewInject(R.id.sech_data_month)
    private TextView sech_data_month;

    @ViewInject(R.id.sech_data_day)
    private TextView sech_data_day;

    @ViewInject(R.id.sech_data_week)
    private TextView sech_data_week;

    //总日期
    public static String selectDate;
    public static String selectWeek;

    private SecheduleAdapter secheduleAdapter;
    private LayoutInflater inflater;

    private HttpUtils httpUtils;
private List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_schedule);
        ViewUtils.inject(SecheduleActivity.this);

        init();

       // setAdapter();

    }

    public void init() {

        inflater = LayoutInflater.from(SecheduleActivity.this);
        httpUtils = new HttpUtils();

        View alertDialogSechedule = inflater.inflate(R.layout.alertdialog_data_select, null);
        final DatePicker datePicker = (DatePicker) alertDialogSechedule.findViewById(R.id.datePicker);

        new AlertDialog.Builder(SecheduleActivity.this).setCancelable(false).setView(alertDialogSechedule).setTitle("请选择排课日期").
                setIcon(R.mipmap.table_column).
                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectDate = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
                        selectWeek=getWeek(selectDate);
                        String yue=(datePicker.getMonth() + 1)+"月";
                        String ri=datePicker.getDayOfMonth()+"";
                        sech_data_month.setText(yue);
                        sech_data_day.setText(ri);
                        sech_data_week.setText(getWeek(selectDate));
                        //Toast.makeText(SecheduleActivity.this, selectDate + "-" + getWeek(selectDate), Toast.LENGTH_LONG).show();
                        getData();
                    }
                }).show();
    }

    public void setAdapter() {
        secheduleAdapter = new SecheduleAdapter(this);

        secheduleAdapter.setList(list);
        gridView.setAdapter(secheduleAdapter);

    }


    public void getData() {

        RequestParams params = new RequestParams();
        params.addBodyParameter("selectTime",selectDate);
        httpUtils.send(HttpRequest.HttpMethod.POST, HttpClientUtil.HTTP_URL + "TotalsQueryAllServlet",
                params, new RequestCallBack() {
                    @Override
                    public void onFailure(HttpException error,
                                          String message) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo responseInfo) {
                        String resultJson = responseInfo.result
                                .toString();

                        list= JsonUtil.getJsonToListMap(resultJson,new String[]{"toid","location","teid","tename","cnid","cnname","toname"});

                        setAdapter();

                    }
                });

    }

    private String getWeek(String date) {
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
