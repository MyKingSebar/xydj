package com.yijia.common_yijia.main.index;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.latte.ec.R;

import java.util.List;

/**
 * Created by wangpanfeng@bokangzhixin.com on 2019/4/18.
 */
public class MainFamilyAdapter extends BaseAdapter {

    private List<MainFamily> data;
    LayoutInflater inflater;
    private Context context;

    public MainFamilyAdapter(Context context, List<MainFamily> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return null == data ? 0 : data.size();
    }

    @Override
    public MainFamily getItem(int position) {
        return null == data ? new MainFamily() : data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_family, null);
            holder.name = convertView.findViewById(R.id.item_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MainFamily family = getItem(position);
        holder.name.setText(family.mainUserName);
        return convertView;
    }

    class ViewHolder {
        TextView name;
    }
}
