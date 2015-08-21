package com.sunmeng.educationaladministration.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.sunmeng.educationaladministration.R;

/**
 * Created by Sunmeng on 2015-08-20.
 * Email:Sunmeng1995@outlook.com
 */
public class SecheduleAdapter extends BaseAdapter {


    private Context mContext;
    private LayoutInflater inflater;

    /**
     * 新增课程选择下拉
     */
    private Spinner spn_className;
    private Spinner spn_teacherName;
    private AutoCompleteTextView act_courseName;

    private static final String[] classNameArr =
            {" TCAS1225 ", " TCAY1228 ", " TCDT17 ", " TCJT1230 ", " TCE151 ", " TCJT1230 ",
                    " TCDT17 "};
    private static final String[] spn_teacherNameArr =
            {" 唐碧 ", " 孙猛 ", " 高洪岩 ", " 孙培林 ", " 张捷 ", " 田源 ",
                    " 霍伟 "};

    private static final String[] courseNameArr =
            {" JAVA ", " COT ", " 开学典礼 ", " 数据库概述 ", " JPush ", " JAVA OOP ",
                    " 集合框架 "};

    public SecheduleAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 48;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        view = inflater.inflate(R.layout.item_schedule, null);
        final RelativeLayout rlInfo = (RelativeLayout) view.findViewById(R.id.rl_item_sch);
        final TextView tvclass = (TextView) view.findViewById(R.id.iv_item_class);
        final TextView tvTeacher = (TextView) view.findViewById(R.id.iv_item_teacher);


        rlInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int js = (position + 1) / 4 + 1;
                int date = (position + 1) % 4;
                if (date == 0) {
                    date = 4;
                    js = js - 1;
                }
                tvclass.setText("TCDT17");
                tvTeacher.setText("唐碧");
                rlInfo.setBackgroundColor(mContext.getResources().getColor(R.color.bjcolor));

                /**
                 * 课程添加弹出框
                 * */
                View alertDialogSechedule = inflater.inflate(R.layout.alertdialog_add_sechedule, null);
                spn_className = (Spinner) alertDialogSechedule.findViewById(R.id.spn_className);
                spn_teacherName = (Spinner) alertDialogSechedule.findViewById(R.id.spn_teacherName);
                act_courseName = (AutoCompleteTextView) alertDialogSechedule.findViewById(R.id.act_courseName);


                /**
                 * 填充下拉数据
                 */
                spn_className.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, classNameArr));
                spn_teacherName.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, spn_teacherNameArr));
                act_courseName.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, courseNameArr));

                new AlertDialog.Builder(mContext).setView(alertDialogSechedule).setTitle("添加课程信息").
                        setIcon(R.mipmap.table_column).
                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                }).show();
            }
        });


        return view;
    }
}
