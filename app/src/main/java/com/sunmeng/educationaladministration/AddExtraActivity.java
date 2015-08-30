package com.sunmeng.educationaladministration;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sunmeng.educationaladministration.animateview.GoogleCardsActivity;
import com.sunmeng.educationaladministration.net_utils.HttpClientUtil;
import com.sunmeng.educationaladministration.utils.JsonUtil;
import com.sunmeng.educationaladministration.utils.Utils;

import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by admin on 2015/8/27.
 */
public class AddExtraActivity extends Activity {

    @ViewInject(R.id.et_extradate)
    private TextView et_date;
    @ViewInject(R.id.et_extrajoin)
    private EditText et_join;
    @ViewInject(R.id.et_address)
    private EditText et_address;
    @ViewInject(R.id.et_extrateacherNames)
    private EditText et_extrateacherNames;

    private ImageView img_addback;


    @ViewInject(R.id.spn_extraclassName)
    private Spinner spn_extraclassName;

    @ViewInject(R.id.tvsave)
    private TextView tvsave;
    private HttpUtils httpUtils;

    private Map<String, String> classNameArr = null;
   // private Map<String, String> spn_teacherNameArr = null;

    private int year;
    private int month;
    private int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_extra);
        ViewUtils.inject(AddExtraActivity.this);

        httpUtils = new HttpUtils();

        setSpnData();

        //初始化Calendar日历对象
        Calendar mycalendar=Calendar.getInstance(Locale.CHINA);
        Date mydate=new Date(); //获取当前日期Date对象
        mycalendar.setTime(mydate);////为Calendar对象设置时间为当前日期

        year=mycalendar.get(Calendar.YEAR); //获取Calendar对象中的年
        month=mycalendar.get(Calendar.MONTH);//获取Calendar对象中的月
        day=mycalendar.get(Calendar.DAY_OF_MONTH);//获取这个月的第几天
        et_date.setText(year + "-" + (month + 1) + "-" + day); //显示当前的年月日


        saveInfo();

        img_addback=(ImageView)findViewById(R.id.img_addback);
        img_addback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(AddExtraActivity.this, GoogleCardsActivity.class);
                AddExtraActivity.this.startActivity(intent);
                AddExtraActivity.this.finish();
            }
        });
    }


    /**
     * 保存信息到服务器
     * */
    public void saveInfo()
    {
        tvsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               // String classname=spn_extraclassName.getSelectedItem().toString();
                String teachername=et_extrateacherNames.getText().toString();

                String etjoin=et_join.getText().toString().trim();
                String etaddress=et_address.getText().toString().trim();
                et_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatePickerDialog dpd = new DatePickerDialog(AddExtraActivity.this, Datelistener, year, month, day);
                        dpd.show();//显示DatePickerDialog组件

                    }


                    private DatePickerDialog.OnDateSetListener Datelistener = new DatePickerDialog.OnDateSetListener() {
                        /**params：view：该事件关联的组件
                         * params：myyear：当前选择的年
                         * params：monthOfYear：当前选择的月
                         * params：dayOfMonth：当前选择的日
                         */
                        @Override
                        public void onDateSet(DatePicker view, int myyear, int monthOfYear, int dayOfMonth) {


                            //修改year、month、day的变量值，以便以后单击按钮时，DatePickerDialog上显示上一次修改后的值
                            year = myyear;
                            month = monthOfYear;
                            day = dayOfMonth;
                            //更新日期
                            updateDate();

                        }

                        //当DatePickerDialog关闭时，更新日期显示
                        private void updateDate() {
                            //在TextView上显示日期
                            et_date.setText(year + "-" + (month + 1) + "-" + day);
                        }
                    };

                });

                String etdate=et_date.getText().toString().trim();


                if(etjoin.equals("")&&etaddress.equals(""))
                {
                    Toast.makeText(AddExtraActivity.this,"请填写完整信息！",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    RequestParams params = new RequestParams();
                    params.addBodyParameter("activitiesadddate", etdate);
                    params.addBodyParameter("activitiesaddtime","上午8:30-17:20");
                    params.addBodyParameter("activitiesaddclassnumid",classNameArr.get(spn_extraclassName.getSelectedItem().toString().trim()));
                    params.addBodyParameter("activitiesaddteacher",teachername);
                    params.addBodyParameter("activitiesaddajointeacher",etjoin);
                    params.addBodyParameter("activitiesaddaaddress",etaddress);
                    httpUtils.send(HttpRequest.HttpMethod.POST, HttpClientUtil.HTTP_URL + "ActivitiesAddServlet",
                            params, new RequestCallBack() {
                                @Override
                                public void onFailure(HttpException error,
                                                      String message) {
                                }

                                @Override
                                public void onSuccess(ResponseInfo responseInfo) {
                                    String resultJson = responseInfo.result
                                            .toString();
                                    //Log.i("ct", "返回结果=" + resultJson);

                                    Intent intent=new Intent(AddExtraActivity.this, GoogleCardsActivity.class);
                                    AddExtraActivity.this.startActivity(intent);
                                    AddExtraActivity.this.finish();

                                }
                            });
                }



            }
        });
    }


    /**
     * 设置下拉的值
     * */
    public  void setSpnData()
    {
        /**
         * 从服务器获取班级信息
         * */
        httpUtils.send(HttpRequest.HttpMethod.GET,
                HttpClientUtil.HTTP_URL + "ClassAllServlet", new RequestCallBack() {
                    @Override
                    public void onSuccess(final ResponseInfo responseInfo) {
                        //Toast.makeText(mContext, responseInfo.toString(), Toast.LENGTH_LONG).show();
                        final List<String> list = new ArrayList<String>();
                        list.add("cnname");
                        list.add("cnid");
                        new AsyncTask<String, String, Map<String, String>>() {
                            @Override
                            protected Map<String, String> doInBackground(String... params) {
                                classNameArr = JsonUtil.getJsonToSpinnerListMap(responseInfo.result.toString(), list);
                                return classNameArr;
                            }

                            @Override
                            protected void onPostExecute(Map<String, String> s) {
                                spn_extraclassName.setAdapter(new ArrayAdapter<String>(AddExtraActivity.this, android.R.layout.simple_spinner_dropdown_item, Utils.getMapListToListString(s)));//

                                spn_extraclassName.setSelection(Utils.getMapListId(s, spn_extraclassName.getSelectedItem().toString()));
                            }
                        }.execute("");
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                    }
                });


    }


    /**
     * 返回事件
     * */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
