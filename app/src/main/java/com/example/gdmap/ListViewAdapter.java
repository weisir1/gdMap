package com.example.gdmap;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Tip;
import com.example.gdmap.interf.ItemViewClickListener;
import com.example.gdmap.util.Util;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private View view;
    private TextView adress;
    private TextView info;
    private TextView ride;
    private TextView walk;
    private TextView drive;
    private LinearLayout itemLeft;
    private ItemViewClickListener clickListener;
    private ListView listView;


    public ListViewAdapter(Context context,ListView listView) {
        this.context = context;
        this.listView = listView;
    }

    private List<Tip> list = new ArrayList<>();

    public void setData(List<Tip> list) {
        if (list == null) return;
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.listitem, parent, false);
        adress = convertView.findViewById(R.id.adress);
        info = convertView.findViewById(R.id.info);

        ride = convertView.findViewById(R.id.ride);
        walk = convertView.findViewById(R.id.walk);
        drive = convertView.findViewById(R.id.drive);
        itemLeft = convertView.findViewById(R.id.itemLeft);


        walk.setOnClickListener(this);
        drive.setOnClickListener(this);
        ride.setOnClickListener(this);
        itemLeft.setOnClickListener(this);

        adress.setText(list.get(position).getName());
        LatLonPoint point = list.get(position).getPoint();
        if (point != null) {
            DPoint ePoint = new DPoint(point.getLatitude(), point.getLongitude());
            DPoint current = new DPoint(Util.LATITUDE, Util.LONGITUDE);
            float distan = CoordinateConverter.calculateLineDistance(current, ePoint);
            String s = distan < 1000 ? ((int) distan + "米") : Util.floatTo2(distan / 1000) + "km";
            info.setText(list.get(position).getName()+"  "+list.get(position).getAddress()+"  "+s);

        }
        return convertView;
    }

    public void setOnItemClickListener(ItemViewClickListener listener){
        this.clickListener = listener;
    }
    @Override
    public void onClick(View v) {
        clickListener.onItemViewClick(v,listView.getPositionForView(v));
    }
}
