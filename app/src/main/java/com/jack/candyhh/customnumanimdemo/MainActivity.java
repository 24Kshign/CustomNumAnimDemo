package com.jack.candyhh.customnumanimdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.jack.candyhh.customnumanimdemo.widget.CustomNumAnimView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    @BindView(R.id.acv_customview)
    CustomNumAnimView acvCustomNumAnimView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.acv_btn_start, R.id.acv_btn_stop})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.acv_btn_start:
                acvCustomNumAnimView.startAnim();
                break;
            case R.id.acv_btn_stop:
                acvCustomNumAnimView.pauseAnim();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        acvCustomNumAnimView.stopAnim();
    }
}
