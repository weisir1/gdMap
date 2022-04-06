package com.example.gdmap.iml;

import com.amap.api.maps.model.CameraPosition;
import com.amap.api.navi.MyNaviViewListener;

abstract public class BackNaviListener implements MyNaviViewListener {
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onNaviSetting() {

    }

    @Override
    public void onNaviCancel() {
        clickCancel();

    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }

    @Override
    public void onNaviMapMode(int i) {

    }

    @Override
    public void onNaviTurnClick() {

    }

    @Override
    public void onNextRoadClick() {

    }

    @Override
    public void onScanViewButtonClick() {

    }

    @Override
    public void onLockMap(boolean b) {

    }

    @Override
    public void onNaviViewLoaded() {

    }

    @Override
    public void onMapTypeChanged(int i) {

    }

    @Override
    public void onNaviViewShowMode(int i) {

    }
    public abstract void clickCancel();


}
