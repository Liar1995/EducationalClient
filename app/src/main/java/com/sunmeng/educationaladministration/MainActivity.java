package com.sunmeng.educationaladministration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements View.OnClickListener {

    private RelativeLayout rl_jwpk;

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

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rl_jwpk:
                Intent intent=new Intent(this,SecheduleActivity.class);
                startActivity(intent);
                break;

        }
    }
}
