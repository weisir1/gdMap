package com.example.gdmap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.services.help.Tip;

import java.util.ArrayList;
import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.RvViewHolder> {
    private List<Tip> list = new ArrayList<>();

  public void setData(List<Tip> list){
      if (list == null) return;
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
  }

    @NonNull
    @Override
    public RvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, null, false);
        return new RvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvViewHolder holder, int position) {
            holder.textView.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class RvViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public RvViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.adress);
        }
    }

}
