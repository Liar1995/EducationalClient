package com.sunmeng.educationaladministration.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.sunmeng.educationaladministration.R;
import com.sunmeng.educationaladministration.SecheduleActivity;
import com.sunmeng.educationaladministration.net_utils.HttpClientUtil;
import com.sunmeng.educationaladministration.utils.JsonUtil;
import com.sunmeng.educationaladministration.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Sunmeng on 2015-08-20.
 * Email:Sunmeng1995@outlook.com
 */
public class SecheduleAdapter extends BaseAdapter {


    private Context mContext;
    private LayoutInflater inflater;

    private List<Map<String, Object>> list;

    public List<Map<String, Object>> getList() {
        return list;
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }


    /**
     * 新增课程选择下拉
     */
    private Spinner spn_className;
    private Spinner spn_teacherName;
    private AutoCompleteTextView act_courseName;

    private int classRoomInt = 0;
    private int timeSelect = 0;

    private Map<String, String> classNameArr = null;
    private Map<String, String> spn_teacherNameArr = null;


    /**
     * HttpUtils
     */
    HttpUtils mHttpUtils;

    public SecheduleAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mHttpUtils = new HttpUtils();
    }

    @Override
    public int getCount() {

        return 48;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int selectId = 3;

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {

        view = inflater.inflate(R.layout.item_schedule, null);
        final RelativeLayout rlInfo = (RelativeLayout) view.findViewById(R.id.rl_item_sch);
        final TextView tvclass = (TextView) view.findViewById(R.id.iv_item_class);
        final TextView tvTeacher = (TextView) view.findViewById(R.id.iv_item_teacher);
        final TextView tvid = (TextView) view.findViewById(R.id.iv_item_id);
        final TextView tvtoname = (TextView) view.findViewById(R.id.iv_item_toname);

        //s"toid","location","teid","tename","cnid","cnname","toname"});
        int location = 0;


        Map<String, Object> map = null;

        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                map = list.get(i);
                location = Integer.parseInt(map.get("location").toString());
                if (location == position) {

                    tvclass.setText(map.get("cnname").toString());
                    tvTeacher.setText(map.get("tename").toString());
                    tvid.setText(map.get("toid").toString());
                    tvtoname.setText(map.get("toname").toString());

                    rlInfo.setBackgroundColor(mContext.getResources().getColor(R.color.bjcolor));
                }

            }
        }


        rlInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int js = (position + 1) / 4 + 1;//教室ID
                int date = (position + 1) % 4;//时间分类
                if (date == 0) {
                    date = 4;
                    js = js - 1;
                }
                classRoomInt = js;
                timeSelect = date;


                /**
                 * 课程添加弹出框
                 * */
                View alertDialogSechedule = inflater.inflate(R.layout.alertdialog_add_sechedule, null);
                spn_className = (Spinner) alertDialogSechedule.findViewById(R.id.spn_className);
                spn_teacherName = (Spinner) alertDialogSechedule.findViewById(R.id.spn_teacherName);
                act_courseName = (AutoCompleteTextView) alertDialogSechedule.findViewById(R.id.act_courseName);


                /**
                 * 从服务器获取班级信息
                 * */
                mHttpUtils.send(HttpRequest.HttpMethod.GET,
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
                                        spn_className.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, Utils.getMapListToListString(s)));//

                                        if (tvclass.getText().toString() != "") {


                                            spn_className.setSelection(Utils.getMapListId(s, tvclass.getText().toString()));
                                        }
                                        else{

                                        }
                                    }
                                }.execute("");
                            }

                            @Override
                            public void onFailure(HttpException e, String s) {
                            }
                        });


                /**
                 * 从服务器获取教师信息
                 * */
                mHttpUtils.send(HttpRequest.HttpMethod.GET,
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
                                        spn_teacherNameArr = JsonUtil.getJsonToSpinnerListMap(responseInfo.result.toString(), list);
                                        return spn_teacherNameArr;
                                    }

                                    @Override
                                    protected void onPostExecute(Map<String, String> s) {
                                        spn_teacherName.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, Utils.getMapListToListString(s)));
                                        if (tvTeacher.getText().toString() != "") {


                                            spn_teacherName.setSelection(Utils.getMapListId(s, tvTeacher.getText().toString()));
                                        }else{

                                        }
                                    }
                                }.execute("");
                            }

                            @Override
                            public void onFailure(HttpException e, String s) {
                            }
                        });
                /**
                 * 从服务器获取所以历史课程名称信息
                 * */
                mHttpUtils.send(HttpRequest.HttpMethod.GET,
                        HttpClientUtil.HTTP_URL + "CurriculumAllServlet", new RequestCallBack() {
                            @Override
                            public void onSuccess(final ResponseInfo responseInfo) {
                                new AsyncTask<String, String, List<String>>() {
                                    @Override
                                    protected List<String> doInBackground(String... params) {
                                        return JsonUtil.getJsonToList(responseInfo.result.toString());
                                    }

                                    @Override
                                    protected void onPostExecute(List<String> s) {
                                        act_courseName.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, s));

                                        if (tvtoname.getText().toString() != "") {


                                            act_courseName.setText(tvtoname.getText().toString());
                                        }else{

                                        }


                                    }
                                }.execute("");
                            }

                            @Override
                            public void onFailure(HttpException e, String s) {
                            }
                        });


                if (tvclass.getText() == "") {

                    new AlertDialog.Builder(mContext).setView(alertDialogSechedule).setCancelable(false).setTitle("添加课程信息").
                            setIcon(R.mipmap.table_column).
                            setPositiveButton("添加", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    /**
                                     * 新增课程所需要参数
                                     * */
                                    String courseName = act_courseName.getText().toString();
                                    int week = Utils.parseintWeekToInt(SecheduleActivity.selectWeek);
                                    int teacher = 2;
                                    int weizhi = position;
                                    RequestParams params = new RequestParams();
                                    if (courseName.equals("") ||

                                            classNameArr.get(spn_className.getSelectedItem().toString()).equals("") ||
                                            spn_teacherNameArr.get(spn_teacherName.getSelectedItem().toString()).equals("")) {
                                        Toast.makeText(mContext, "请填写完整信息在提交", Toast.LENGTH_LONG).show();
                                        return;
                                    } else {
                                        params.addBodyParameter("Toname", courseName);
                                        params.addBodyParameter("Totime", SecheduleActivity.selectDate);
                                        params.addBodyParameter("Week", week + "");
                                        params.addBodyParameter("ClassRoomid", classRoomInt + "");
                                        params.addBodyParameter("ClassNuber", classNameArr.get(spn_className.getSelectedItem().toString().trim()));
                                        params.addBodyParameter("TeacherId", spn_teacherNameArr.get(spn_teacherName.getSelectedItem().toString().trim()));
                                        params.addBodyParameter("TimeBucket", timeSelect + "");
                                        params.addBodyParameter("Location", position + "");
                                    }
                                    mHttpUtils.send(HttpRequest.HttpMethod.POST,
                                            HttpClientUtil.HTTP_URL + "TotalsAddServlet", params, new RequestCallBack() {
                                                @Override
                                                public void onSuccess(ResponseInfo responseInfo) {
                                                    Toast.makeText(mContext, spn_teacherName.getSelectedItemPosition() + "--" + spn_teacherName.getSelectedItemId(), Toast.LENGTH_LONG).show();
                                                    tvclass.setText(spn_className.getSelectedItem().toString());
                                                    tvTeacher.setText(spn_teacherName.getSelectedItem().toString());
                                                    rlInfo.setBackgroundColor(mContext.getResources().getColor(R.color.bjcolor));
                                                }

                                                @Override
                                                public void onFailure(HttpException e, String s) {
                                                }
                                            });

                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                } else

                {


                    new AlertDialog.Builder(mContext).setView(alertDialogSechedule).setCancelable(false).setTitle("修改课程信息").
                            setIcon(R.mipmap.table_column).
                            setPositiveButton("修改", new DialogInterface.OnClickListener() {


                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    /**
                                     * 新增课程所需要参数
                                     * */
                                    String courseName = act_courseName.getText().toString();

                                    RequestParams params = new RequestParams();
                                    if (courseName.equals("") ||
                                            classNameArr.get(spn_className.getSelectedItem().toString()).equals("") ||
                                            spn_teacherNameArr.get(spn_teacherName.getSelectedItem().toString()).equals("")) {
                                        Toast.makeText(mContext, "请填写完整信息在提交", Toast.LENGTH_LONG).show();
                                        return;
                                    } else {


                                        params.addBodyParameter("updateRcourseName", courseName);
                                        params.addBodyParameter("updateClassNumberID", classNameArr.get(spn_className.getSelectedItem().toString().trim()));
                                        params.addBodyParameter("updateTeacherId", spn_teacherNameArr.get(spn_teacherName.getSelectedItem().toString().trim()));
                                        params.addBodyParameter("updateTotalsID", tvid.getText().toString());
                                    }
                                    mHttpUtils.send(HttpRequest.HttpMethod.POST,
                                            HttpClientUtil.HTTP_URL + "TotalsUpdateServlet", params, new RequestCallBack() {
                                                @Override
                                                public void onSuccess(ResponseInfo responseInfo) {

                                                    tvclass.setText(spn_className.getSelectedItem().toString());
                                                    tvTeacher.setText(spn_teacherName.getSelectedItem().toString());
                                                    rlInfo.setBackgroundColor(mContext.getResources().getColor(R.color.bjcolor));
                                                }

                                                @Override
                                                public void onFailure(HttpException e, String s) {
                                                }
                                            });

                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();

                }
            }
        });


        return view;
    }


}
