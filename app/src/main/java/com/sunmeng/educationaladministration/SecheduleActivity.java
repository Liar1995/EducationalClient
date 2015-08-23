package com.sunmeng.educationaladministration;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.GridView;

import com.sunmeng.educationaladministration.adapter.SecheduleAdapter;

public class SecheduleActivity extends Activity {


    private GridView gridView;
    private SecheduleAdapter secheduleAdapter;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_schedule);

        init();
        setAdapter();
    }

    public void init() {
        gridView = (GridView) findViewById(R.id.gv_schedules);
        secheduleAdapter = new SecheduleAdapter(this);
        inflater = LayoutInflater.from(SecheduleActivity.this);
        View alertDialogSechedule = inflater.inflate(R.layout.alertdialog_data_select, null);
        new AlertDialog.Builder(SecheduleActivity.this).setView(alertDialogSechedule).setTitle("请选择排课日期").
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

    public void setAdapter() {
        gridView.setAdapter(secheduleAdapter);

    }

}
