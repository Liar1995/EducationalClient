package com.sunmeng.educationaladministration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.sunmeng.educationaladministration.animateview.GoogleCardsActivity;

public class MainActivity extends Activity implements View.OnClickListener {

    private RelativeLayout rl_jwpk;
    private RelativeLayout kwhd;
    private RelativeLayout rl_xmdb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init() {
        rl_jwpk = (RelativeLayout) findViewById(R.id.rl_jwpk);
        rl_jwpk.setOnClickListener(this);

        kwhd= (RelativeLayout) findViewById(R.id.kwhd);
        kwhd.setOnClickListener(this);

        rl_xmdb= (RelativeLayout) findViewById(R.id.rl_xmdb);
        rl_xmdb.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rl_jwpk:
                Intent intent=new Intent(this,SecheduleActivity.class);
                startActivity(intent);
                break;
            case R.id.kwhd:
                Intent intent2=new Intent(this,GoogleCardsActivity.class);
                startActivity(intent2);
                break;
            case R.id.rl_xmdb:
                intent=new Intent(this,ReplyActivity.class);
                startActivity(intent);
                break;
        }
    }
}
