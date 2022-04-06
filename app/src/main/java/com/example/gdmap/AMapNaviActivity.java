package com.example.gdmap;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapRouteActivity;
import com.example.gdmap.iml.BackNaviListener;

public class AMapNaviActivity extends AmapRouteActivity {

    private AMapNaviView naviView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_map_navi);
        naviView = (AMapNaviView) findViewById(R.id.naviView);
        naviView.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        naviView.setAMapNaviViewListener(new BackNaviListener() {
            @Override
            public void clickCancel() {
                finish();
            }
        });
        naviView.setRouteMarkerVisible(true,false,true);
        naviView.setNaviMode(AMapNaviView.CAR_UP_MODE);
        AMapNaviViewOptions options = naviView.getViewOptions();

        options.setCameraBubbleShow(true);
        options.setTrafficBarEnabled(true);
        options.setAutoDisplayOverview(true);
        options.setNaviArrowVisible(true);
        options.setLaneInfoShow(true);
        options.setSecondActionVisible(true);
        naviView.setViewOptions(options);

//        AmapNaviParams params = new AmapNaviParams();
//        params.setNeedCalculateRouteWhenPresent(true);
//        params.setUseInnerVoice(true);
//        params.setShowExitNaviDialog(true);

    }

    @Override
    protected void onResume() {
        naviView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        naviView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        naviView.onDestroy();
        super.onDestroy();
    }
}