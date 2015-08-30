package com.sunmeng.educationaladministration;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.sunmeng.educationaladministration.adapter.ReplyAdapter;

import com.sunmeng.educationaladministration.animateview.OnDismissCallback;
import com.sunmeng.educationaladministration.animateview.SwingBottomInAnimationAdapter;
import com.sunmeng.educationaladministration.animateview.SwipeDismissAdapter;
import com.sunmeng.educationaladministration.net_utils.HttpClientUtil;
import com.sunmeng.educationaladministration.utils.JsonUtil;
import com.sunmeng.educationaladministration.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by YUZEPENG on 2015/8/23.
 */
public class ReplyActivity extends Activity implements View.OnClickListener, OnDismissCallback {


    private ImageView iv_back_reply;

    private TextView tv_add_reply;

    private ListView listview_reply;

    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();



    private ReplyAdapter adapter;
    private GoogleCardsAdapter mGoogleCardsAdapter;


    AlertDialog.Builder builder;
    AlertDialog dialog;

    int mYear;
    int mMonth;
    int mDay;


    /**
     * HttpUtils
     */
    private HttpUtils httpUtils;

    private Map<String, String> classNameArr = null;
    private Map<String, String> teacherNameArr = null;
    private Map<String, String> classRoomArr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        init();
        getDate();

    }

    public void init() {
        httpUtils = new HttpUtils();
        iv_back_reply = (ImageView) findViewById(R.id.iv_back_reply);
        listview_reply = (ListView) findViewById(R.id.listview_reply);
        tv_add_reply= (TextView) findViewById(R.id.tv_add_reply);
        iv_back_reply.setOnClickListener(this);
        tv_add_reply.setOnClickListener(this);
        listview_reply.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                  @Override
                                                  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                                      final Map<String, Object> map;
                                                      map = (Map<String, Object>) parent.getItemAtPosition(position);
                                                      LayoutInflater inflater1 = getLayoutInflater();
                                                      View view1 = inflater1.inflate(R.layout.activity_replyinfo,
                                                              (ViewGroup) findViewById(R.id.toast_layout));


                                                      final Spinner et_classname_reply = (Spinner) view1.findViewById(R.id.et_classname_reply);
                                                      final TextView ed_date_reply = (TextView) view1.findViewById(R.id.ed_date_reply);
                                                      final TextView et_time_reply = (TextView) view1.findViewById(R.id.et_time_reply);
                                                      final TextView et_course_reply = (TextView) view1.findViewById(R.id.et_course_reply);
                                                      final TextView et_address_reply = (TextView) view1.findViewById(R.id.et_address_reply);
                                                      final Spinner ed_classroom_reply = (Spinner) view1.findViewById(R.id.ed_classroom_reply);
                                                      final Spinner et_teacher_reply = (Spinner) view1.findViewById(R.id.et_teacher_reply);
                                                      final TextView ed_receiveteacher_reply = (TextView) view1.findViewById(R.id.ed_receiveteacher_reply);
                                                      ImageView iv_imgclose_reply = (ImageView) view1.findViewById(R.id.ivBack);
                                                      final TextView tv_update_reply = (TextView) view1.findViewById(R.id.tvUpdate);
                                                      /**
                                                       * 代码优化  从服务器获取 班级列表 教室列表 教师列表  可以重复使用 三个集合的数据  有时间研究实现
                                                       */
//
//                                                      if(classNameArr.size()<=0){
//
//                                                      }


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
                                                                              Utils.classnumberArr=classNameArr;
                                                                              return classNameArr;
                                                                          }

                                                                          @Override
                                                                          protected void onPostExecute(Map<String, String> s) {                                        //android.R.layout.simple_spinner_dropdown_item// //R.layout.myspinner
                                                                              et_classname_reply.setAdapter(new android.widget.ArrayAdapter<String>(ReplyActivity.this, android.R.layout.simple_spinner_dropdown_item, Utils.getMapListToListString(s)));


                                                                              if (map.get("cnname").toString() != "") {


                                                                                  et_classname_reply.setSelection(Utils.getMapListId(s, map.get("cnname").toString()));
                                                                                  et_classname_reply.setEnabled(false);

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
                                                                             Utils.teachernameArr=teacherNameArr;
                                                                              return teacherNameArr;
                                                                          }

                                                                          @Override
                                                                          protected void onPostExecute(Map<String, String> s) {
                                                                              et_teacher_reply.setAdapter(new android.widget.ArrayAdapter<String>(ReplyActivity.this, android.R.layout.simple_spinner_dropdown_item, Utils.getMapListToListString(s)));
                                                                              if (map.get("tename").toString() != "") {


                                                                                  et_teacher_reply.setSelection(Utils.getMapListId(s, map.get("tename").toString()));
                                                                                  et_teacher_reply.setEnabled(false);
                                                                              } else {

                                                                              }
                                                                          }
                                                                      }.execute("");
                                                                  }

                                                                  @Override
                                                                  public void onFailure(HttpException e, String s) {
                                                                  }
                                                              });

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
                                                                             Utils.classroomArr=classRoomArr;
                                                                              return classRoomArr;
                                                                          }

                                                                          @Override
                                                                          protected void onPostExecute(Map<String, String> s) {
                                                                              ed_classroom_reply.setAdapter(new android.widget.ArrayAdapter<String>(ReplyActivity.this, android.R.layout.simple_spinner_dropdown_item, Utils.getMapListToListString(s)));
                                                                              if (map.get("clname").toString() != "") {


                                                                                  ed_classroom_reply.setSelection(Utils.getMapListId(s, map.get("clname").toString()));
                                                                                  ed_classroom_reply.setEnabled(false);
                                                                              } else {

                                                                              }
                                                                          }
                                                                      }.execute("");
                                                                  }

                                                                  @Override
                                                                  public void onFailure(HttpException e, String s) {
                                                                  }
                                                              });


                                                      //  et_classname_reply.set(map.get("cnname").toString());A

                                                      ed_date_reply.setText(map.get("rdate").toString());
                                                      et_time_reply.setText(map.get("rtime").toString());
                                                      et_course_reply.setText(map.get("rcourse").toString());
                                                      et_address_reply.setText(map.get("raddress").toString());
                                                      //  ed_classroom_reply.setText(map.get("clname").toString());
                                                      //  et_teacher_reply.setText(map.get("tename").toString());
                                                      ed_receiveteacher_reply.setText(map.get("rreceiveteacher").toString());
                                                      String date = (map.get("rdate").toString());
                                                      String[] strs = date.split("-");
                                                      mYear = Integer.parseInt(strs[0]);
                                                      mMonth = Integer.parseInt(strs[1]) - 1;
                                                      mDay = Integer.parseInt(strs[2]);
                                                      iv_imgclose_reply.setOnClickListener(new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View v) {
                                                              dialog.hide();

                                                          }
                                                      });

                                                      ed_date_reply.setOnClickListener(new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View v) {

                                                              // 创建DatePickerDialog对象
                                                              DatePickerDialog dpd = new DatePickerDialog(ReplyActivity.this,
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
                                                              et_classname_reply.setEnabled(flag);
                                                              ed_date_reply.setEnabled(flag);
                                                              et_time_reply.setEnabled(flag);
                                                              et_course_reply.setEnabled(flag);
                                                              et_address_reply.setEnabled(flag);
                                                              ed_classroom_reply.setEnabled(flag);
                                                              et_teacher_reply.setEnabled(flag);
                                                              ed_receiveteacher_reply.setEnabled(flag);
                                                          }

                                                          public void updateReply() {

                                                              String date = ed_date_reply.getText().toString().trim(); //日期
                                                              String time = et_time_reply.getText().toString().trim();//时间
                                                              String classnumid = classNameArr.get(et_classname_reply.getSelectedItem().toString().trim());//班级id
                                                              String course = et_course_reply.getText().toString().trim();//课程
                                                              String address = et_address_reply.getText().toString().trim();//地址
                                                              String classroomid = classRoomArr.get(ed_classroom_reply.getSelectedItem().toString().trim());// 教室id
                                                              String teacherid = teacherNameArr.get(et_teacher_reply.getSelectedItem().toString().trim()); //教师id
                                                              String receiveteacher = ed_receiveteacher_reply.getText().toString().trim();//接受任务老师 String
                                                              String id = map.get("rid").toString();  //要修改的表id
                                                              if ("".equals(date) || "".equals(time) || "0".equals(classnumid) || "".equals(course) || "".equals(address) || "0".equals(classroomid) || "".equals(address) || "0".equals(teacherid) || "".equals(receiveteacher) || "".equals(id)) {
                                                                  Toast.makeText(ReplyActivity.this, "信息不完整！", Toast.LENGTH_SHORT).show();
                                                                  return;
                                                              }else{

                                                                 // Toast.makeText(ReplyActivity.this, date + "," + time + "," + classnumid + "," + course + "," + address + "," + classroomid + "," + teacherid + "," + receiveteacher + "," + id, Toast.LENGTH_LONG).show();


                                                                      RequestParams params = new RequestParams();
                                                                      params.addBodyParameter("replyupcdate", date);
                                                                      params.addBodyParameter("replyuptime", time);
                                                                      params.addBodyParameter("replyupclassnumid", classnumid);
                                                                      params.addBodyParameter("replyupcourse",course);
                                                                      params.addBodyParameter("replyupaddress",address);
                                                                      params.addBodyParameter("replyupclassroomid",classroomid);
                                                                      params.addBodyParameter("replyupteacherid",teacherid);
                                                                      params.addBodyParameter("replyupreceiveteacher",receiveteacher);
                                                                      params.addBodyParameter("replyuprid",id);


                                                                      httpUtils.send(HttpRequest.HttpMethod.POST, HttpClientUtil.HTTP_URL + "ReplyUpdateServlet", params, new RequestCallBack()  {

                                                                          @Override
                                                                          public void onSuccess(ResponseInfo responseInfo) {

                                                                           Toast.makeText(ReplyActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                                                                              getDate();
                                                                          }

                                                                          @Override
                                                                          public void onFailure(HttpException e, String s) {
                                                                              Toast.makeText(ReplyActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
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

                                              }

        );


    }

    public void getDate() {

        httpUtils.send(HttpRequest.HttpMethod.POST, HttpClientUtil.HTTP_URL + "ReplyQueryAllServlet",
                new RequestCallBack() {
                    @Override
                    public void onFailure(HttpException error,
                                          String message) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo responseInfo) {
                        String resultJson = responseInfo.result
                                .toString();

                        list = JsonUtil.getJsonToListMap(resultJson, new String[]{"rid", "rdate", "rtime", "cnname", "clname", "rreceiveteacher", "rcourse", "tename", "raddress", "rclassnuid"});
                        setAdapter();

                    }
                });


    }

    private void setAdapter() {

        mGoogleCardsAdapter = new GoogleCardsAdapter(this);
        //   mGoogleCardsAdapter.setList(list);
        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(new SwipeDismissAdapter(mGoogleCardsAdapter, (OnDismissCallback) this));
        swingBottomInAnimationAdapter.setListView(listview_reply);

        listview_reply.setAdapter(swingBottomInAnimationAdapter);

        mGoogleCardsAdapter.addAll(list);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_reply:
                this.finish();
                break;
            case R.id.tv_add_reply:
                Intent intent=new Intent(ReplyActivity.this,ReplyAddActivity.class);
                this.startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onDismiss(ListView listView, int[] reverseSortedPositions) {

        //Toast.makeText(ReplyActivity.this,"测试",Toast.LENGTH_LONG).show();
        // 弹出警告框

        for (final int position : reverseSortedPositions) {

            AlertDialog.Builder b = new AlertDialog.Builder(ReplyActivity.this);
            b.setIcon(android.R.drawable.ic_dialog_alert);
            b.setTitle("提示信息");
            b.setMessage("确定要删除这条数据吗?");
            b.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Map<String, Object> map = mGoogleCardsAdapter.getItem(position);
                    String id = map.get("rid").toString();
                    RequestParams params = new RequestParams();
                    params.addBodyParameter("replydeletelid", id);
                    //          Toast.makeText(ReplyActivity.this,"测试:id="+id,Toast.LENGTH_LONG).show();

                    httpUtils.send(HttpRequest.HttpMethod.POST, HttpClientUtil.HTTP_URL + "ReplyDeleteServlet", params, new RequestCallBack() {

                        @Override
                        public void onSuccess(ResponseInfo responseInfo) {

                            Toast.makeText(ReplyActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                            //getDate();
                            mGoogleCardsAdapter.remove(position);
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            Toast.makeText(ReplyActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            b.setNegativeButton("取消", null);
            b.show();// 显示

        }

    }

    private static class GoogleCardsAdapter extends ArrayAdapter<Map<String, Object>> {
        private Context mContext;
        private LayoutInflater inflater;

        public GoogleCardsAdapter(Context context) {
            mContext = context;
            inflater = LayoutInflater.from(mContext);

        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            final Map<String, Object> map;
            view = inflater.inflate(R.layout.item_reply_curricular, parent, false);
            TextView tv_date_reply = (TextView) view.findViewById(R.id.tv_date_reply);
            TextView tv_teacher_reply = (TextView) view.findViewById(R.id.tv_teacher_reply);
            TextView tv_number_reply = (TextView) view.findViewById(R.id.tv_number_reply);
            TextView tv_course_reply = (TextView) view.findViewById(R.id.tv_course_reply);
            TextView tv_address_reply = (TextView) view.findViewById(R.id.tv_address_reply);
            RelativeLayout toptime = (RelativeLayout) view.findViewById(R.id.toptime);
            map = getItem(position);
            tv_date_reply.setText(map.get("rdate").toString());
            tv_teacher_reply.setText(map.get("tename").toString());
            tv_number_reply.setText(map.get("cnname").toString());
            tv_course_reply.setText(map.get("rcourse").toString());
            tv_address_reply.setText(map.get("raddress").toString());

            return view;
        }
//
//		private void setImageView(ViewHolder viewHolder, int position) {
//			int imageResId;
//			switch (getItem(position) % 5) {
//				case 0:
//					imageResId = R.drawable.img_nature1;
//					break;
//				case 1:
//					imageResId = R.drawable.img_nature2;
//					break;
//				case 2:
//					imageResId = R.drawable.img_nature3;
//					break;
//				case 3:
//					imageResId = R.drawable.img_nature4;
//					break;
//				default:
//					imageResId = R.drawable.img_nature5;
//			}
//
//			Bitmap bitmap = getBitmapFromMemCache(imageResId);
//			if (bitmap == null) {
//				bitmap = BitmapFactory.decodeResource(mContext.getResources(), imageResId);
//				addBitmapToMemoryCache(imageResId, bitmap);
//			}
//			viewHolder.imageView.setImageBitmap(bitmap);
//		}
//
//		private void addBitmapToMemoryCache(int key, Bitmap bitmap) {
//			if (getBitmapFromMemCache(key) == null) {
//				mMemoryCache.put(key, bitmap);
//			}
//		}
//
//		private Bitmap getBitmapFromMemCache(int key) {
//			return mMemoryCache.get(key);
//		}

        private static class ViewHolder {
            TextView textView;
            ImageView imageView;
        }
    }
}
