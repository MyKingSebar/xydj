package com.yijia.common_yijia.main.index.friendcircle.choosefriend;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.latte.ec.R;
import com.example.latte.ui.contactlist.adapter.ContactHolder;
import com.example.latte.ui.contactlist.cn.CNPinyinIndex;

import java.util.List;


/**
 * Created by you on 2017/9/12.
 */

public class SearchAdapter extends RecyclerView.Adapter<ContactHolder> {

    private Context mContext=null;
    private final List<CNPinyinIndex<ChooseFriendData>> ChooseFriendDataList;

    private static final RequestOptions OPTIONS = new RequestOptions()
            .placeholder(R.mipmap.default_head)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate();


    public SearchAdapter(Context context,List<CNPinyinIndex<ChooseFriendData>> ChooseFriendDataList) {
        this.ChooseFriendDataList = ChooseFriendDataList;
        this.mContext=context;
    }

    @Override
    public int getItemCount() {
        return ChooseFriendDataList.size();
    }

    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_main_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ContactHolder holder, int position) {
        CNPinyinIndex<ChooseFriendData> index = ChooseFriendDataList.get(position);
        ChooseFriendData ChooseFriendData = index.cnPinyin.data;
        Glide.with(mContext)
                .load(ChooseFriendData.getUserHead())
                .apply(OPTIONS)
                .into(holder.iv_header);

        SpannableStringBuilder ssb = new SpannableStringBuilder(ChooseFriendData.chinese());
        ForegroundColorSpan span = new ForegroundColorSpan(Color.BLUE);
        ssb.setSpan(span, index.start, index.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tv_name.setText(ssb);
    }

    public void setNewDatas(List<CNPinyinIndex<ChooseFriendData>> newDatas) {
        this.ChooseFriendDataList.clear();
        if (newDatas != null && !newDatas.isEmpty()) {
            this.ChooseFriendDataList.addAll(newDatas);
        }
        notifyDataSetChanged();
    }

}
