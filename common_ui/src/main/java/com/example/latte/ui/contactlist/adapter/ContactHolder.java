package com.example.latte.ui.contactlist.adapter;

import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.latte.ui.R;

/**
 * Created by you on 2017/9/11.
 */

public class ContactHolder extends RecyclerView.ViewHolder {

    public final ImageView iv_header;

    public final TextView tv_name;

    public final LinearLayout ll_choosefriend;

    public ContactHolder(View itemView) {
        super(itemView);
        iv_header = (ImageView) itemView.findViewById(R.id.iv_header);
        tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        ll_choosefriend = (LinearLayout) itemView.findViewById(R.id.ll_choosefriend);
    }
}
