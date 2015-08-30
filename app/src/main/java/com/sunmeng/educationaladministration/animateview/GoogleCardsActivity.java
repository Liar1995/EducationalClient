/*
 * Copyright 2013 Niek Haarman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sunmeng.educationaladministration.animateview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.haarman.listviewanimations.ArrayAdapter;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.sunmeng.educationaladministration.AddExtraActivity;
import com.sunmeng.educationaladministration.MainActivity;
import com.sunmeng.educationaladministration.R;
import com.sunmeng.educationaladministration.net_utils.HttpClientUtil;
import com.sunmeng.educationaladministration.utils.JsonUtil;
import com.sunmeng.educationaladministration.utils.Utils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoogleCardsActivity extends Activity implements OnDismissCallback {

    private GoogleCardsAdapter mGoogleCardsAdapter;
    private HttpUtils httpUtils;
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private ListView listView;
    private ImageView img_back;
    private ImageView img_back2;

    private Map<String, String> classNameArr = null;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_extra_curricular);
        httpUtils = new HttpUtils();
        listView = (ListView) findViewById(R.id.activity_googlecards_listview);

        mGoogleCardsAdapter = new GoogleCardsAdapter(this);

        getData();

        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(new SwipeDismissAdapter(mGoogleCardsAdapter, this));
        swingBottomInAnimationAdapter.setListView(listView);

        listView.setAdapter(swingBottomInAnimationAdapter);

        TextView tvadd = (TextView) findViewById(R.id.tvadd);
        tvadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GoogleCardsActivity.this, AddExtraActivity.class);
                GoogleCardsActivity.this.startActivity(intent);
                GoogleCardsActivity.this.finish();
            }
        });

        getDetails();


        img_back2=(ImageView)findViewById(R.id.img_back);
        img_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(GoogleCardsActivity.this, MainActivity.class);
                GoogleCardsActivity.this.startActivity(intent);
                GoogleCardsActivity.this.finish();


            }
        });



    }


    /**
     * 点击Item获取详细信息
     */
    public void getDetails() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Map<String, Object> map;
                map = (Map<String, Object>) parent.getItemAtPosition(position);
                LayoutInflater inflater1 = getLayoutInflater();
                View view1 = inflater1.inflate(R.layout.activity_extra_detailsinfo,
                        (ViewGroup) findViewById(R.id.toast_layout));

                final Spinner spn_extraclassName = (Spinner) view1.findViewById(R.id.spn_uextraclassName);
                final TextView tv_date = (TextView) view1.findViewById(R.id.ued_date_reply);
                final EditText et_teacher = (EditText) view1.findViewById(R.id.et_uextrateacher);
                final EditText et_jointeacher = (EditText) view1.findViewById(R.id.et_uextrajoin);
                final EditText et_address = (EditText) view1.findViewById(R.id.et_uaddress);
                final TextView tv_update_reply = (TextView) view1.findViewById(R.id.tvUpdate);
                final EditText et_uatime = (EditText) view1.findViewById(R.id.et_uatime);

                img_back=(ImageView)view1.findViewById(R.id.ivBack);
                img_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.cancel();

                    }
                });

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
                                        return classNameArr;
                                    }

                                    @Override
                                    protected void onPostExecute(Map<String, String> s) {                                        //android.R.layout.simple_spinner_dropdown_item// //R.layout.myspinner
                                        spn_extraclassName.setAdapter(new android.widget.ArrayAdapter<String>(GoogleCardsActivity.this, android.R.layout.simple_spinner_dropdown_item, Utils.getMapListToListString(s)));


                                        if (map.get("cnname").toString() != "") {


                                            spn_extraclassName.setSelection(Utils.getMapListId(s, map.get("cnname").toString()));
                                            spn_extraclassName.setEnabled(false);

                                        } else {

                                        }
                                        // et_classname_reply.setSelection(2, true);
                                    }
                                }.execute("");
                            }

                            @Override
                            public void onFailure(HttpException e, String s) {
                            }
                        });

                et_teacher.setText(map.get("ateacher").toString());
                et_jointeacher.setText(map.get("ajointeacher").toString());
                et_address.setText(map.get("aaddress").toString());
                tv_date.setText(map.get("adate").toString());
                et_uatime.setText(map.get("atime").toString());

                tv_update_reply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (tv_update_reply.getText().equals("修改")) {
                            setClickble(true);
                            tv_update_reply.setText("保存");
                        } else if (tv_update_reply.getText().equals("保存")) {
                            setClickble(false);
                            tv_update_reply.setText("修改");
                            // TODO 保存操作
                            updateReply();
                        }
                    }

                    /**
                     * 设置 编辑框是否可用点
                     */
                    private void setClickble(boolean flag) {
                        et_teacher.setEnabled(flag);
                        et_jointeacher.setEnabled(flag);
                        et_address.setEnabled(flag);
                        spn_extraclassName.setEnabled(flag);
                        et_uatime.setEnabled(flag);
                    }

                    public void updateReply() {
                        String etteacher = et_teacher.getText().toString().trim();
                        String etjointeacher = et_jointeacher.getText().toString().trim();
                        String ettime = et_uatime.getText().toString().trim();
                        String etaddress = et_address.getText().toString().trim();
                        String tvdate = tv_date.getText().toString().trim();
                        String classnumid = classNameArr.get(spn_extraclassName.getSelectedItem().toString().trim());//班级id
                        String id = map.get("aid").toString();
                        //Toast.makeText(GoogleCardsActivity.this, "修改值="+etteacher+etjointeacher+ettime+etaddress, Toast.LENGTH_SHORT).show();

                        if ("".equals(etteacher) && "".equals(etjointeacher) && "".equals(etaddress)) {
                            Toast.makeText(GoogleCardsActivity.this, "信息不完整！", Toast.LENGTH_SHORT).show();
                            return;
                        } else {


                            RequestParams params = new RequestParams();
                            params.addBodyParameter("activitiesupcdate", tvdate);
                            params.addBodyParameter("activitiesuptime", ettime);
                            params.addBodyParameter("activitiesupclassnumid", classnumid);
                            params.addBodyParameter("activitiesupteacher", etteacher);
                            params.addBodyParameter("activitiesupjointeacher", etjointeacher);
                            params.addBodyParameter("activitiesupaddress", etaddress);
                            params.addBodyParameter("activitiesuprid", id);
                            Toast.makeText(GoogleCardsActivity.this, "id=="+id, Toast.LENGTH_SHORT).show();

                            httpUtils.send(HttpRequest.HttpMethod.POST, HttpClientUtil.HTTP_URL + "ActivitiesUpdateServlet", params, new RequestCallBack() {

                                @Override
                                public void onSuccess(ResponseInfo responseInfo) {

                                    Toast.makeText(GoogleCardsActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                    getData();
                                    dialog.cancel();
                                }

                                @Override
                                public void onFailure(HttpException e, String s) {
                                    Toast.makeText(GoogleCardsActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }

                });
                builder = new AlertDialog.Builder(parent.getContext());
                builder.setView(view1);
                dialog = builder.create();
                dialog.show();
            }

        });



        }



                /**
                 * 从服务器上获取班级扩展活动的数据
                 * */

    public void getData() {
        httpUtils.send(HttpRequest.HttpMethod.POST, HttpClientUtil.HTTP_URL + "ActivitiesAllServlet",
                null, new RequestCallBack() {
                    @Override
                    public void onFailure(HttpException error,
                                          String message) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo responseInfo) {
                        String resultJson = responseInfo.result
                                .toString();

                        //Log.i("ct", resultJson);
                        list = JsonUtil.getJsonToListMap(resultJson, new String[]
                                {"aid", "adate", "atime", "cnname", "ateacher", "ajointeacher", "aaddress"});

                        mGoogleCardsAdapter.addAll(list);

                    }
                });
    }

    @Override
    public void onDismiss(ListView listView, int[] reverseSortedPositions) {
        for (final int position : reverseSortedPositions) {

            //Toast.makeText(GoogleCardsActivity.this, "删除的position=" + position, Toast.LENGTH_LONG).show();
            //弹出警告框
            AlertDialog.Builder b = new AlertDialog.Builder(GoogleCardsActivity.this);//this  当前的Activity
            //设置图标   系统提供的
            b.setIcon(android.R.drawable.ic_dialog_alert);
            //标题
            b.setTitle("提示信息");
            b.setMessage("您确定删除吗？");

            //设置确定按钮
            b.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Map<String, Object> map = mGoogleCardsAdapter.getItem(position);
                    Toast.makeText(GoogleCardsActivity.this, "删除的aid=" + map.get("aid").toString(), Toast.LENGTH_LONG).show();
                    RequestParams params = new RequestParams();
                    params.addBodyParameter("activitiesdeleted", map.get("aid").toString());
                    httpUtils.send(HttpRequest.HttpMethod.POST, HttpClientUtil.HTTP_URL + "ActivitiesDeleteServlet",
                            params, new RequestCallBack() {
                                @Override
                                public void onFailure(HttpException error,
                                                      String message) {
                                }

                                @Override
                                public void onSuccess(ResponseInfo responseInfo) {
                                    String resultJson = responseInfo.result
                                            .toString();
                                    //Log.i("ct", "返回结果="+resultJson);
                                    mGoogleCardsAdapter.remove(mGoogleCardsAdapter.getItem(position));

                                }
                            });


                }

            });

            b.setNegativeButton("取消", null);
            b.show();//显示弹框
        }
    }

    private static class GoogleCardsAdapter extends ArrayAdapter<Map<String, Object>> {

        private Context mContext;
        private LayoutInflater mInflater;

        public GoogleCardsAdapter(Context context) {
            mContext = context;
            this.mInflater = LayoutInflater.from(mContext);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {

                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_extra_curricular, null);

                viewHolder.tvtimes = (TextView) convertView.findViewById(R.id.tvtimes);
                viewHolder.tvyears = (TextView) convertView.findViewById(R.id.tvyears);
                viewHolder.tvclass = (TextView) convertView.findViewById(R.id.tvclass);
                viewHolder.tvactivitys = (TextView) convertView.findViewById(R.id.tvactivitys);

                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Map<String, Object> map = getItem(position);
            viewHolder.tvtimes.setText(map.get("atime").toString());
            viewHolder.tvyears.setText(map.get("adate").toString());
            viewHolder.tvclass.setText(map.get("cnname").toString());
            viewHolder.tvactivitys.setText(map.get("aaddress").toString());
            return convertView;
        }


        private static class ViewHolder {
            TextView tvtimes;
            TextView tvyears;
            TextView tvclass;
            TextView tvactivitys;
        }
    }
}
