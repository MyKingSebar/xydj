package com.yijia.common_yijia.main.index.friendcircle.choosefriend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.latte.ec.R;
import com.example.latte.ui.contactlist.adapter.ContactHolder;
import com.example.latte.ui.contactlist.adapter.HeaderHolder;
import com.example.latte.ui.contactlist.cn.CNPinyin;
import com.example.latte.ui.contactlist.stickyheader.StickyHeaderAdapter;
import com.example.latte.util.GlideUtils;

import java.util.List;



public class ContactAdapter extends RecyclerView.Adapter<ContactHolder> implements StickyHeaderAdapter<HeaderHolder> {

    private final List<CNPinyin<ChooseFriendData>> cnPinyinList;
    private Context mContext=null;
    private ChooseFriendItemLisener mLisener=null;

    public ContactAdapter(Context context,List<CNPinyin<ChooseFriendData>> cnPinyinList,ChooseFriendItemLisener lisener) {
        this.cnPinyinList = cnPinyinList;
        this.mContext=context;
        this.mLisener=lisener;
    }

    @Override
    public int getItemCount() {
        return cnPinyinList.size();
    }

    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_main_item, parent, false));
    }


    @Override
    public void onBindViewHolder(ContactHolder holder, int position) {
        ChooseFriendData contact = cnPinyinList.get(position).data;
        Glide.with(mContext)
                .load(contact.getUserHead())
                .apply(GlideUtils.USEROPTIONS)
                .into(holder.iv_header);
        holder.tv_name.setText(contact.getNickname());
        if(null!=mLisener){
            holder.ll_choosefriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLisener.onItemClick(contact);
                }
            });
        }

    }

    @Override
    public long getHeaderId(int childAdapterPosition) {
        return cnPinyinList.get(childAdapterPosition).getFirstChar();
    }

    @Override
    public void onBindHeaderViewHolder(HeaderHolder holder, int childAdapterPosition) {
        holder.tv_header.setText(String.valueOf(cnPinyinList.get(childAdapterPosition).getFirstChar()));
    }

    @Override
    public HeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new HeaderHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_header, parent, false));
    }

}
