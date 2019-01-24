package com.yijia.common_yijia.main.index.friendcircle.choosefriend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.latte.ui.R;
import com.example.latte.ui.contactlist.adapter.ContactHolder;
import com.example.latte.ui.contactlist.adapter.HeaderHolder;
import com.example.latte.ui.contactlist.cn.CNPinyin;
import com.example.latte.ui.contactlist.stickyheader.StickyHeaderAdapter;

import java.util.List;


/**
 * Created by you on 2017/9/11.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactHolder> implements StickyHeaderAdapter<HeaderHolder> {

    private static final RequestOptions OPTIONS = new RequestOptions()
            .placeholder(com.example.latte.ec.R.color.app_text_gray)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate();

    private final List<CNPinyin<ChooseFriendData>> cnPinyinList;
    private Context mContext=null;

    public ContactAdapter(Context context,List<CNPinyin<ChooseFriendData>> cnPinyinList) {
        this.cnPinyinList = cnPinyinList;
        this.mContext=context;
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
                .apply(OPTIONS)
                .into(holder.iv_header);
        holder.tv_name.setText(contact.getNickname());
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
