package com.example.gdmap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Poi;
import com.amap.api.maps.model.Text;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.AmapPageType;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.gdmap.interf.ItemViewClickListener;
import com.example.gdmap.util.Util;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AMap.OnMyLocationChangeListener, Inputtips.InputtipsListener, TextWatcher, ItemViewClickListener, AMap.OnMapClickListener {

    private MapView map;
    private AMap aMap;
    private TextView tuceng;
    private PopupWindow popupWindow;
    private UiSettings uiSettings;
    private double longitude;
    private double latitude;
    private EditText edEnd;
    private RecyclerView recycler;
    private RecycleAdapter recycleAdapter;
    private ListView listview;
    private ListViewAdapter listViewAdapter;
    private AmapNaviParams amapNaviParams;
    private CardView cardview;
    private Tip tip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        initView();

        map.onCreate(savedInstanceState);

        locationSetting();
//        locationShow();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET
                ,Manifest.permission.WRITE_EXTERNAL_STORAGE
                ,Manifest.permission.ACCESS_NETWORK_STATE
                ,Manifest.permission.ACCESS_WIFI_STATE
                ,Manifest.permission.READ_PHONE_STATE
                ,Manifest.permission.ACCESS_COARSE_LOCATION
                ,Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ,Manifest.permission.CHANGE_WIFI_STATE},100);
    }

    private void locationSetting() {
        MyLocationStyle locationStyle = new MyLocationStyle();
        locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        locationStyle.interval(120000);
        locationStyle.showMyLocation(true);
        locationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.dw));
        aMap.setMyLocationEnabled(true);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        aMap.setMyLocationStyle(locationStyle);
        uiSettings = aMap.getUiSettings();
        uiSettings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
        uiSettings.setLogoBottomMargin(-50);
        aMap.addOnMyLocationChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        map.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        map.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        map.onPause();
        super.onPause();
    }

    private void initView() {
        map = findViewById(R.id.map);
        tuceng = (TextView) findViewById(R.id.tuceng);
        tuceng.setOnClickListener(this);
        edEnd = findViewById(R.id.ed_end);
        cardview = findViewById(R.id.cardview);
        listview = findViewById(R.id.listview);
        cardview.setVisibility(View.GONE);
        listViewAdapter = new ListViewAdapter(this, listview);
        listViewAdapter.setOnItemClickListener(this);
        listview.setAdapter(listViewAdapter);
        if (aMap == null) {
            aMap = map.getMap();
        }

        listview.setVisibility(View.GONE);
//        edEnd.setOnTouchListener(this);
        edEnd.addTextChangedListener(this);
        aMap.addOnMapClickListener(this);
    }

    private void poiSearch(String name) {
//        query = new PoiSearch.Query(name, "", "");   //第二个参数poi搜索类型 第三个为搜索范围
//        query.setPageSize(8);
////        query.setPageNum();
//        poiSearch = new PoiSearch(this, query);
//        poiSearch.searchPOIAsyn();
//        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
//            @Override
//            public void onPoiSearched(PoiResult poiResult, int i) {
//
//            }
//
//            @Override
//            public void onPoiItemSearched(PoiItem poiItem, int i) {
//
//            }
//        });

        InputtipsQuery inputtipsQuery = new InputtipsQuery(name, "");
        inputtipsQuery.setCityLimit(false);   //限制在当前城市
        Inputtips inputtips = new Inputtips(this, inputtipsQuery);
        inputtips.setInputtipsListener(this);
        inputtips.requestInputtipsAsyn();
    }

    @Override
    public void onClick(View v) {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
        switch (v.getId()) {
            case R.id.tuceng:
                initPopWindow();
                break;
            case R.id.daohang:
                aMap.setMapType(AMap.MAP_TYPE_NAVI);
                break;
            case R.id.weixing:
                aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.yejing:
                aMap.setMapType(AMap.MAP_TYPE_NIGHT);
                break;
            case R.id.baizhou:
                aMap.setMapType(AMap.MAP_TYPE_NORMAL);
                break;
            case R.id.po:
                cardview.setVisibility(View.GONE);
                clearMarker();
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(Util.LATITUDE, Util.LONGITUDE)));
                break;
            case R.id.line:
                if (tip != null) {
                    startNavi(tip,AmapNaviType.DRIVER);
                }
                break;

        }
    }

    private void clearMarker() {
        List<Marker> marker = aMap.getMapScreenMarkers();
        for (int i = 0; i < marker.size(); i++) {
            marker.get(i).remove();
        }
    }

    @Override
    public void onMyLocationChange(Location location) {     //定位监听 用于定位button 将视角移回当前位置
        Util.LATITUDE = location.getLatitude();
        Util.LONGITUDE = location.getLongitude();
    }

    private TextView daohang;
    private TextView weixing;
    private TextView yejing;
    private TextView baizhou;
    private View view;

    private void initPopWindow() {
        if (view == null) {
            view = LayoutInflater.from(this).inflate(R.layout.tucengitemlayout, null);
            daohang = (TextView) view.findViewById(R.id.daohang);
            weixing = (TextView) view.findViewById(R.id.weixing);
            yejing = (TextView) view.findViewById(R.id.yejing);
            baizhou = (TextView) view.findViewById(R.id.baizhou);
            daohang.setOnClickListener(this);
            weixing.setOnClickListener(this);
            yejing.setOnClickListener(this);
            baizhou.setOnClickListener(this);
        }

        popupWindow = new PopupWindow(view, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
//        popupWindow.setAnimationStyle(R.anim.);
        popupWindow.showAsDropDown(tuceng, Util.dipToPx(this, -18), Util.dipToPx(this, 15));
    }

    private List<Tip> lists = new ArrayList<>();

    @Override
    public void onGetInputtips(List<Tip> list, int i) {
        lists.clear();
        if (i == AMapException.CODE_AMAP_SUCCESS) {
            if (list != null) {
                for (int j = 0; j < list.size(); j++) {
                    Tip tip = list.get(j);
                    if (tip.getPoint() != null) {
                        lists.add(tip);
                    }
                }
                listViewAdapter.setData(lists);
                listViewAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (("").equals(s.toString().trim())) {
            listview.setVisibility(View.GONE);
        } else {
            listview.setVisibility(View.VISIBLE);
        }
        poiSearch(s.toString().trim());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    private AMapLocationClient client;
    private AMapLocationClientOption clientOption;
    private AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {

        }
    };


    private void locationShow() {
        client = new AMapLocationClient(getApplicationContext());
        client.setLocationListener(locationListener);
        clientOption = new AMapLocationClientOption();
//        AMapLocationClientOption.AMapLocationPurpose.SignIn
//        AMapLocationClientOption.AMapLocationPurpose.Sport
//        AMapLocationClientOption.AMapLocationPurpose.Transport

        clientOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        clientOption.setInterval(1000);
        clientOption.setNeedAddress(true);
        if (client != null) {
            client.setLocationOption(clientOption);
            client.stopLocation();
            client.startLocation();
        }
    }

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                listview.setVisibility(View.VISIBLE);
//        }
//        return false;
//    }

    private long firstTime = System.currentTimeMillis(), secondTime;

    @Override
    public void onBackPressed() {

        secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            firstTime = secondTime;
        } else {
            super.onBackPressed();

        }

    }


    private void drawMarker(Tip tip) {
        listview.setVisibility(View.GONE);
        cardview.setVisibility(View.VISIBLE);
        this.tip = tip;
        LatLng latLng = new LatLng(tip.getPoint().getLatitude(), tip.getPoint().getLongitude());
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(tip.getName())
                .draggable(false)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker));
        aMap.addMarker(options);
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
    }


    @Override
    public void onItemViewClick(View view, int position) {
        clearMarker();
        if (lists != null) {
            AmapNaviType type = null;
            Tip tip = lists.get(position);
            if (tip != null) {
                switch (view.getId()) {
                    case R.id.itemLeft:
                        drawMarker(tip);
                        return;
                    case R.id.walk:
                        type = AmapNaviType.WALK;
                        break;
                    case R.id.drive:
                        type = AmapNaviType.DRIVER;
                        break;
                    case R.id.ride:
                        type = AmapNaviType.RIDE;
                        break;
                }
            }
            startNavi(tip, type);

        }

    }

    private void startNavi(Tip tip, AmapNaviType type) {
        if (tip.getPoint() != null) {
            LatLng latLng = new LatLng(tip.getPoint().getLatitude(), tip.getPoint().getLongitude());
            Poi poi = new Poi(tip.getName(), latLng, tip.getPoiID());
            amapNaviParams = new AmapNaviParams(null, null, poi, type, AmapPageType.ROUTE);
            amapNaviParams.setNeedDestroyDriveManagerInstanceWhenNaviExit(true);
            amapNaviParams.setBroadcastMode(getApplicationContext(), 2);
            AmapNaviPage naviPage = AmapNaviPage.getInstance();
            naviPage.showRouteActivity(getApplicationContext(), amapNaviParams, null);
            AMapNavi aMapNavi = AMapNavi.getInstance(this);
            aMapNavi.setUseInnerVoice(true, false);
        }

    }

    @Override
    public void onMapClick(LatLng latLng) {
        listview.setVisibility(View.GONE);
    }
}