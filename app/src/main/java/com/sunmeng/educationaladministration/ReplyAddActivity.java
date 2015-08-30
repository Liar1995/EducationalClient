package com.sunmeng.educationaladministration;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.sunmeng.educationaladministration.net_utils.HttpClientUtil;
import com.sunmeng.educationaladministration.utils.JsonUtil;
import com.sunmeng.educationaladministration.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by YUZEPENG on 2015/8/30.
 */
public class ReplyAddActivity extends Activity {

    private HttpUtils httpUtils;
    private Map<String, String> classNameArr = null;
    private Map<String, String> classRoomArr = null;
    private Map<String, String> teacherNameArr = null;

    int mYear;
    int mMonth;
    int mDay;

    ImageView iv_back_replyadd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_reply);
        init();
    }

    private void init() {

        httpUtils = new HttpUtils();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        String[] strs = str.split("-");
        mYear = Integer.parseInt(strs[0]);
        mMonth = Integer.parseInt(strs[1]) - 1;
        mDay = Integer.parseInt(strs[2]);

        final Spinner et_classname_reply = (Spinner) findViewById(R.id.et_classname_reply);
        final TextView ed_date_reply = (TextView) findViewById(R.id.ed_date_reply);
        final TextView et_time_reply = (TextView) findViewById(R.id.et_time_reply);
        final TextView et_course_reply = (TextView) findViewById(R.id.et_course_reply);
        final TextView et_address_reply = (TextView) findViewById(R.id.et_address_reply);
        final Spinner ed_classroom_reply = (Spinner) findViewById(R.id.ed_classroom_reply);
        final Spinner et_teacher_reply = (Spinner) findViewById(R.id.et_teacher_reply);
        final TextView ed_receiveteacher_reply = (TextView) findViewById(R.id.ed_receiveteacher_reply);
        iv_back_replyadd= (ImageView) findViewById(R.id.iv_back_replyadd);
        //   ImageView iv_imgclose_reply = (ImageView) findViewById(R.id.ivBack);
        TextView tv_save_reply = (TextView) findViewById(R.id.tv_save_reply);

        ed_date_reply.setText(str);


        if (Utils.classnumberArr != null) {
            et_classname_reply.setAdapter(new android.widget.ArrayAdapter<String>(ReplyAddActivity.this, android.R.layout.simple_spinner_dropdown_item, Utils.getMapListToListString(Utils.classnumberArr)));
        } else {

            /**
             * 从服务器获取班级列表
             */
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
                                    Utils.classnumberArr = classNameArr;
                                    return classNameArr;
                                }

                                @Override
                                protected void onPostExecute(Map<String, String> s) {                                        //android.R.layout.simple_spinner_dropdown_item// //R.layout.myspinner
                                    et_classname_reply.setAdapter(new android.widget.ArrayAdapter<String>(ReplyAddActivity.this, android.R.layout.simple_spinner_dropdown_item, Utils.getMapListToListString(s)));


                                }
                            }.execute("");
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                        }
                    });
        }

        if (Utils.classroomArr != null) {
            ed_classroom_reply.setAdapter(new android.widget.ArrayAdapter<String>(ReplyAddActivity.this, android.R.layout.simple_spinner_dropdown_item, Utils.getMapListToListString(Utils.classroomArr)));
        } else {

            /**
             * 从服务器获取教室
             * */
            httpUtils.send(HttpRequest.HttpMethod.GET,
                    HttpClientUtil.HTTP_URL + "ClassRoomAllServlet", new RequestCallBack() {
                        @Override
                        public void onSuccess(final ResponseInfo responseInfo) {
                            //Toast.makeText(mContext, responseInfo.toString(), Toast.LENGTH_LONG).show();
                            final List<String> list = new ArrayList<String>();

                            list.add("clname");
                            list.add("clid");
                            new AsyncTask<String, String, Map<String, String>>() {
                                @Override
                                protected Map<String, String> doInBackground(String... params) {
                                    classRoomArr = JsonUtil.getJsonToSpinnerListMap(responseInfo.result.toString(), list);
                                    Utils.classroomArr = classRoomArr;
                                    return classRoomArr;
                                }

                                @Override
                                protected void onPostExecute(Map<String, String> s) {
                                    ed_classroom_reply.setAdapter(new android.widget.ArrayAdapter<String>(ReplyAddActivity.this, android.R.layout.simple_spinner_dropdown_item, Utils.getMapListToListString(s)));
                                }
                            }.execute("");
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                        }
                    });

        }

        if (Utils.teachernameArr != null) {
            et_teacher_reply.setAdapter(new android.widget.ArrayAdapter<String>(ReplyAddActivity.this, android.R.layout.simple_spinner_dropdown_item, Utils.getMapListToListString(Utils.teachernameArr)));
        } else {
            /**
             * 从服务器获取教师信息
             * */
            httpUtils.send(HttpRequest.HttpMethod.GET,
                    HttpClientUtil.HTTP_URL + "TeacherAllServlet", new RequestCallBack() {
                        @Override
                        public void onSuccess(final ResponseInfo responseInfo) {
                            //Toast.makeText(mContext, responseInfo.toString(), Toast.LENGTH_LONG).show();
                            final List<String> list = new ArrayList<String>();
                            list.add("tename");
                            list.add("teid");
                            new AsyncTask<String, String, Map<String, String>>() {
                                @Override
                                protected Map<String, String> doInBackground(String... params) {
                                    teacherNameArr = JsonUtil.getJsonToSpinnerListMap(responseInfo.result.toString(), list);
                                    Utils.teachernameArr = teacherNameArr;
                                    return teacherNameArr;
                                }

                                @Override
                                protected void onPostExecute(Map<String, String> s) {
                                    et_teacher_reply.setAdapter(new android.widget.ArrayAdapter<String>(ReplyAddActivity.this, android.R.layout.simple_spinner_dropdown_item, Utils.getMapListToListString(s)));
                                }
                            }.execute("");
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                        }
                    });

        }

        ed_date_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 创建DatePickerDialog对象
                DatePickerDialog dpd = new DatePickerDialog(ReplyAddActivity.this,
                        Datelistener, mYear, mMonth, mDay);

                dpd.show();// 显示DatePickerDialog组件

            }


            private DatePickerDialog.OnDateSetListener Datelistener = new DatePickerDialog.OnDateSetListener() {
                /**
                 * params：view：该事件关联的组�?params：myyear：当前�?择的�?params：monthOfYear：当前�?择的�
                 * ? params：dayOfMonth：当前�?择的�?
                 */
                @Override
                public void onDateSet(DatePicker view, int myyear, int monthOfYear,
                                      int dayOfMonth) {

                    // 修改year、month、day的变量�?，以便以后单击按钮时，DatePickerDialog上显示上�?��修改后的�?
                    mYear = myyear;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    // 更新日期
                    updateDate();

                }

                // 当DatePickerDialog关闭时，更新日期显示
                private void updateDate() {
                    // 在TextView上显示日�?
                    ed_date_reply.setText(mYear + "-" + (mMonth + 1) + "-" + mDay);
                }
            };

        });
        tv_save_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = ed_date_reply.getText().toString().trim(); //日期
                String time = et_time_reply.getText().toString().trim();//时间
                String classnumid = classNameArr.get(et_classname_reply.getSelectedItem().toString().trim());//班级id
                String course = et_course_reply.getText().toString().trim();//课程
                String address = et_address_reply.getText().toString().trim();//地址
                String classroomid = classRoomArr.get(ed_classroom_reply.getSelectedItem().toString().trim());// 教室id
                String teacherid = teacherNameArr.get(et_teacher_reply.getSelectedItem().toString().trim()); //教师id
                String receiveteacher = ed_receiveteacher_reply.getText().toString().trim();//接受任务老师 String
                if ("".equals(date) || "".equals(time) || "0".equals(classnumid) || "".equals(course) || "".equals(address) || "0".equals(classroomid) || "".equals(address) || "0".equals(teacherid) || "".equals(receiveteacher) ) {
                    Toast.makeText(ReplyAddActivity.this, "信息不完整！", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    // Toast.makeText(ReplyActivity.this, date + "," + time + "," + classnumid + "," + course + "," + address + "," + classroomid + "," + teacherid + "," + receiveteacher + "," + id, Toast.LENGTH_LONG).show();


                    RequestParams params = new RequestParams();
                    params.addBodyParameter("replyadddate", date);//日期
                    params.addBodyParameter("replyaddtime", time);//时间
                    params.addBodyParameter("replyaddclassnumid", classnumid);//班级id
                    params.addBodyParameter("replyaddcourse", course);// 课程
                    params.addBodyParameter("replyaddaddress", address);//地点
                    params.addBodyParameter("replyaddclassroomid", classroomid);//教室
                    params.addBodyParameter("replyaddteacherid", teacherid);//教员
                    params.addBodyParameter("replyaddreceiveteacher", receiveteacher);//接受任务老师


                    httpUtils.send(HttpRequest.HttpMethod.POST, HttpClientUtil.HTTP_URL + "ReplyAddServlet", params, new RequestCallBack() {

                        @Override
                        public void onSuccess(ResponseInfo responseInfo) {

                            Toast.makeText(ReplyAddActivity.this, "增加成功!", Toast.LENGTH_SHORT).show();
                            et_time_reply.setText("");
                            et_course_reply.setText("");
                            et_address_reply.setText("");
                            ed_receiveteacher_reply.setText("");
                            //   getDate();
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            Toast.makeText(ReplyAddActivity.this, "增加失败!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
        iv_back_replyadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ReplyAddActivity.this,ReplyActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(ReplyAddActivity.this,ReplyActivity.class);
        startActivity(intent);
        finish();
    }
}
