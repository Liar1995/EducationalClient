package com.sunmeng.educationaladministration;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Spinner;

import com.sunmeng.educationaladministration.adapter.SecheduleAdapter;

public class SecheduleActivity extends Activity {


    private GridView gridView;
    private SecheduleAdapter secheduleAdapter;




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



    }

    public void setAdapter() {
        gridView.setAdapter(secheduleAdapter);

    }

}
