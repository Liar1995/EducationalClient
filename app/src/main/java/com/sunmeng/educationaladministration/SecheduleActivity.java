package com.sunmeng.educationaladministration;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
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
import com.sunmeng.educationaladministration.utils.Utils;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecheduleActivity extends Activity implements View.OnClickListener {

    @ViewInject(R.id.gv_schedules)
    private GridView gridView;

    /**
     * 页面日期选择
     */
    @ViewInject(R.id.sech_data_month)
    private TextView sech_data_month;

    @ViewInject(R.id.sech_data_day)
    private TextView sech_data_day;

    @ViewInject(R.id.sech_data_week)
    private TextView sech_data_week;

    /**
     * 总日期
     */
    public static String selectDate;
    public static String selectWeek;

    /**
     * 课表日期选择按钮
     */
    @ViewInject(R.id.sched_pre)
    private ImageView sched_pre;
    @ViewInject(R.id.sched_next)
    private ImageView sched_next;

    private SecheduleAdapter secheduleAdapter;
    private LayoutInflater inflater;

    private HttpUtils httpUtils;
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_schedule);
        ViewUtils.inject(SecheduleActivity.this);
        init();

    }

    public void init() {

        inflater = LayoutInflater.from(SecheduleActivity.this);
        httpUtils = new HttpUtils();

        //注册课表日期选择事件
        sched_pre.setOnClickListener(this);
        sched_next.setOnClickListener(this);

        View alertDialogSechedule = inflater.inflate(R.layout.alertdialog_data_select, null);
        final DatePicker datePicker = (DatePicker) alertDialogSechedule.findViewById(R.id.datePicker);

        new AlertDialog.Builder(SecheduleActivity.this).setCancelable(false).setView(alertDialogSechedule).setTitle("请选择排课日期").
                setIcon(R.mipmap.table_column).
                setPositiveButton("添加", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectDate = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
                        selectWeek = Utils.getWeek(selectDate);
                        String yue = (datePicker.getMonth() + 1) + "月";
                        String ri = datePicker.getDayOfMonth() + "";
                        sech_data_month.setText(yue);
                        sech_data_day.setText(ri);
                        sech_data_week.setText(Utils.getWeek(selectDate));
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
        params.addBodyParameter("selectTime", selectDate);
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

                        list = JsonUtil.getJsonToListMap(resultJson, new String[]{"toid", "location", "teid", "tename", "cnid", "cnname", "toname"});

                        setAdapter();

                    }
                });

    }


    @Override
    public void onClick(View v) {
        /**
         * 点击日期选择后  要做到星期和日期同时联动，而且需要判断日月年的进一，以及合法性
         * */
        switch (v.getId()) {
            //上一天
            case R.id.sched_pre:
                selectDate= Utils.calculationDate(selectDate,-1);
                selectWeek=Utils.getWeek(selectDate);
                String selectDateArrPre[] =selectDate.split("-");
                sech_data_month.setText(selectDateArrPre[1]+"月");
                sech_data_day.setText(selectDateArrPre[2]);
                sech_data_week.setText(selectWeek);
                getData();
                break;
            //下一天
            case R.id.sched_next:
                selectDate= Utils.calculationDate(selectDate,+1);
                selectWeek=Utils.getWeek(selectDate);
                String selectDateArrNext[] =selectDate.split("-");
                sech_data_month.setText(selectDateArrNext[1]+"月");
                sech_data_day.setText(selectDateArrNext[2]);
                sech_data_week.setText(selectWeek);
                getData();
                break;
        }
    }


}
