package com.sunmeng.educationaladministration;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.sunmeng.educationaladministration.excel.JXLUtil;
import com.sunmeng.educationaladministration.net_utils.HttpClientUtil;
import com.sunmeng.educationaladministration.utils.JsonUtil;
import com.sunmeng.educationaladministration.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WriteException;

public class SecheduleActivity extends Activity implements View.OnClickListener, Animation.AnimationListener, GestureDetector.OnGestureListener {

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
    private ImageButton sched_next;

    private SecheduleAdapter secheduleAdapter;
    private LayoutInflater inflater;

    private HttpUtils httpUtils;
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

    //课表容器
    @ViewInject(R.id.lin_body)
    private LinearLayout lin_body;

    /**
     * 手势滑动
     */
    private GestureDetector dectector;// 手势检测器


    /**
     * Excel导出加载动画
     */
    private View alertdialog_exporting;
    private AlertDialog exportingDialog = null;

    /**
     * Excel工作簿初始化
     */
    public static WritableFont arial18font = null;

    public static WritableFont arial12font = null;
    public static WritableCellFormat arial12format = null;

    /**
     * Excel文件导出对象
     */
    private File file;

    /**
     * 导出按钮标识
     * */
    private boolean flag=false;



    /**
     * 格式定义
     */
    public static void format() {
        try {
            arial18font = new WritableFont(WritableFont.ARIAL, 18,
                    WritableFont.BOLD);
            arial18font.setColour(jxl.format.Colour.BLACK);


            arial12font = new WritableFont(WritableFont.ARIAL, 12);
            arial12format = new WritableCellFormat(arial12font);
            arial12format.setBorder(jxl.format.Border.ALL,
                    jxl.format.BorderLineStyle.THIN);
            arial12format.setAlignment(jxl.format.Alignment.CENTRE);


        } catch (WriteException e) {

            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_schedule);
        ViewUtils.inject(SecheduleActivity.this);
        init();

    }

    public void init() {
        dectector = new GestureDetector(this);// 实例化手势检测器对象
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
                        if (Utils.parseintWeekToInt(Utils.getWeek(selectDate)) == 7) {
                            flag=true;
                        } else {
                            flag=false;
                        }
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
        alertdialog_exporting = inflater.inflate(R.layout.alertdialog_exporting, null);

        /**
         * 点击日期选择后  要做到星期和日期同时联动，而且需要判断日月年的进一，以及合法性
         * */
        switch (v.getId()) {
            //返回
            case R.id.sched_pre:
                finish();
                break;
            //导出
            case R.id.sched_next:
                RequestParams dateAll = new RequestParams();
                dateAll.addBodyParameter("selectTime", selectDate);
                httpUtils.send(HttpRequest.HttpMethod.POST, HttpClientUtil.HTTP_URL + "TotalsQueryAllDateServlet",
                        dateAll, new RequestCallBack() {
                            @Override
                            public void onFailure(HttpException error,
                                                  String message) {
                            }

                            @Override
                            public void onSuccess(ResponseInfo responseInfo) {
                                String resultJson = responseInfo.result
                                        .toString();
                                SecheduleToExcel(resultJson);
                            }
                        });
                //Toast.makeText(this,"开始导出到Excel",Toast.LENGTH_LONG).show();
                break;
        }

    }

    /**
     * 将一周数据导出到Excel
     */
    private void SecheduleToExcel(String resultJson) {

        if (flag) {

            exportingDialog = new AlertDialog.Builder(SecheduleActivity.this).setView(alertdialog_exporting).show();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            file = new File(getSDPath() + "/Excel");
            makeDir(file);
            JXLUtil.initExcel(file.toString() + "/excel.xls");
            JXLUtil.writeObjListToExcel(resultJson, getSDPath() + "/Excel/excel.xls",
                    this);
            exportingDialog.dismiss();
            Toast.makeText(SecheduleActivity.this, "导出完毕", Toast.LENGTH_LONG).show();
        } else if (!flag) {
            Toast.makeText(SecheduleActivity.this,"请将日期选择到周日在进行导出",Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 判断文件夹是否存在及创建
     */
    public static void makeDir(File dir) {
        if (!dir.getParentFile().exists()) {
            makeDir(dir.getParentFile());
        }
        dir.mkdirs();
    }

    /**
     * 判断SD卡是否可用
     */
    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        }
        String dir = sdDir.toString();
        return dir;
    }


    /**
     * Activity中onTouchEvent
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dectector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent x1, MotionEvent x2, float velocityX, float velocityY) {

        if (x1.getX() - x2.getX() < -350) {// 向右滑动

            Animation aniRight = new TranslateAnimation(0F, +lin_body.getWidth(), 0F, 0F);
            aniRight.setAnimationListener(this);
            aniRight.setDuration(300);
            aniRight.setFillAfter(true);
            lin_body.startAnimation(aniRight);
            selectDate = Utils.calculationDate(selectDate, -1);
            selectWeek = Utils.getWeek(selectDate);
            String selectDateArrPre[] = selectDate.split("-");
            sech_data_month.setText(selectDateArrPre[1] + "月");
            sech_data_day.setText(selectDateArrPre[2]);
            sech_data_week.setText(selectWeek);

        } else if (x1.getX() - x2.getX() > 350) {// 向左滑动
            //Toast.makeText(SecheduleActivity.this,(x1.getX() - x2.getX())+"",Toast.LENGTH_LONG).show();
            Animation aniLeft = new TranslateAnimation(0F, -lin_body.getWidth(), 0F, 0F);
            aniLeft.setAnimationListener(this);
            aniLeft.setDuration(300);
            aniLeft.setFillAfter(true);
            lin_body.startAnimation(aniLeft);
            selectDate = Utils.calculationDate(selectDate, +1);
            selectWeek = Utils.getWeek(selectDate);
            String selectDateArrNext[] = selectDate.split("-");
            sech_data_month.setText(selectDateArrNext[1] + "月");
            sech_data_day.setText(selectDateArrNext[2]);
            sech_data_week.setText(selectWeek);
        }
        if (Utils.parseintWeekToInt(Utils.getWeek(selectDate)) == 7) {
            flag=true;
        } else {
            flag=false;
        }
        return false;
    }

    /**
     * dispatchTouchEvent用于事件的分发
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        dectector.onTouchEvent(ev); //让GestureDetector响应触碰事件
        super.dispatchTouchEvent(ev); //让Activity响应触碰事件
        return false;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        animation = new AlphaAnimation(0.1F, 1.0F);
        animation.setDuration(1000);
        lin_body.startAnimation(animation);
        getData();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
