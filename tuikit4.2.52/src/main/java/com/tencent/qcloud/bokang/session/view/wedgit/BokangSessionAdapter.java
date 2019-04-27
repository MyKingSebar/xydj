
package com.tencent.qcloud.bokang.session.view.wedgit;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.qcloud.bokang.session.view.BokangSessionPanel;
import com.tencent.qcloud.uikit.R;
import com.tencent.qcloud.uikit.TUIKit;
import com.tencent.qcloud.uikit.api.session.ISessionAdapter;
import com.tencent.qcloud.uikit.api.session.ISessionProvider;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfo;
import com.tencent.qcloud.uikit.business.session.model.SessionInfo;
import com.tencent.qcloud.uikit.business.session.model.SessionProvider;
import com.tencent.qcloud.uikit.business.session.view.SessionIconView;
import com.tencent.qcloud.uikit.common.BackgroundTasks;
import com.tencent.qcloud.uikit.common.utils.DateTimeUtil;
import com.tencent.qcloud.uikit.common.utils.UIUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BokangSessionAdapter extends ISessionAdapter {

    private List<SessionInfo> dataSource = new ArrayList<>();

    private int mRightWidth = UIUtils.getPxByDp(60);

    private BokangSessionPanel mSessionPanel;

    private BokangSessionListener mBokangSessionListener=null;

    public void setmBokangSessionListener(BokangSessionListener mBokangSessionListener) {
        this.mBokangSessionListener = mBokangSessionListener;
    }

    public BokangSessionAdapter(BokangSessionPanel sessionPanel) {
        mSessionPanel = sessionPanel;
    }

    public void setDataProvider(ISessionProvider provider) {
        dataSource = provider.getDataSource();
        if (provider instanceof SessionProvider) {
            ((SessionProvider) provider).attachAdapter(this);
        }
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        BackgroundTasks.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BokangSessionAdapter.super.notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public SessionInfo getItem(int position) {
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(TUIKit.getAppContext()).inflate(R.layout.bokang_session_adapter, parent, false);
            holder = new ViewHolder();
            holder.item_left = (RelativeLayout) convertView.findViewById(R.id.item_left);
            holder.item_right = (RelativeLayout) convertView.findViewById(R.id.item_right);
            holder.iv_icon = (SessionIconView) convertView.findViewById(R.id.session_icon);
            holder.tv_title = (TextView) convertView.findViewById(R.id.session_title);
            holder.tv_msg = (TextView) convertView.findViewById(R.id.session_last_msg);
            holder.tv_time = (TextView) convertView.findViewById(R.id.session_time);
            holder.tv_speak = (TextView) convertView.findViewById(R.id.session_speak);
            holder.tv_unRead = (TextView) convertView.findViewById(R.id.session_unRead);
            holder.item_right_txt = (TextView) convertView.findViewById(R.id.item_right_txt);
            convertView.setTag(holder);
        } else {// 有直接获得ViewHolder
            holder = (ViewHolder) convertView.getTag();
        }

        SessionInfo session = dataSource.get(position);
        MessageInfo lastMsg = session.getLastMessage();
        if (session.isTop()) {
            holder.item_left.setBackgroundColor(convertView.getResources().getColor(R.color.top_session_color));
        } else {
            holder.item_left.setBackgroundColor(Color.WHITE);
        }
//        if (mSessionPanel.getInfoView() != null) {
//            holder.iv_icon.invokeInformation(session, mSessionPanel.getInfoView());
//        }


        if (session.isGroup()) {
            holder.tv_speak.setVisibility(View.INVISIBLE);
            holder.iv_icon.setDefaultImageResId(R.drawable.default_group);
        } else {
            List<String> urls = new ArrayList<>();
            urls.add(session.getIconUrl());
            holder.iv_icon.setIconUrls(urls);

            holder.tv_speak.setVisibility(View.VISIBLE);
            holder.tv_speak.setOnClickListener(v->{
                if(mBokangSessionListener!=null){
                    mBokangSessionListener.sperkClick(session.getPeer(),v);
                }
            });
            holder.iv_icon.setDefaultImageResId(R.drawable.default_head);
        }

        holder.tv_title.setText(session.getTitle());
        holder.tv_msg.setText("");
        holder.tv_time.setText("");
        if (lastMsg != null) {
            if (lastMsg.getStatus() == MessageInfo.MSG_STATUS_REVOKE) {
                if (lastMsg.isSelf())
                    holder.tv_msg.setText("您撤回了一条消息");
                else if (lastMsg.isGroup()) {
                    holder.tv_msg.setText(lastMsg.getFromUser() + "撤回了一条消息");
                } else {
                    holder.tv_msg.setText("对方撤回了一条消息");
                }

            } else {
                if (lastMsg.getExtra() != null)
                    holder.tv_msg.setText(lastMsg.getExtra().toString());
            }

            holder.tv_time.setText(DateTimeUtil.getTimeFormatText(new Date(lastMsg.getMsgTime())));
        }


        if (session.getUnRead() > 0) {
            holder.tv_unRead.setVisibility(View.VISIBLE);
            holder.tv_unRead.setText("" + session.getUnRead());
        } else {
            holder.tv_unRead.setVisibility(View.GONE);
        }

        holder.item_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRightItemClick(v, position);
                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        RelativeLayout item_left;
        RelativeLayout item_right;
        TextView tv_title;
        TextView tv_msg;
        TextView tv_time;
        TextView tv_unRead;
        SessionIconView iv_icon;
        TextView item_right_txt;
        TextView tv_speak;
    }

    /**
     * 单击事件监听器
     */
    private onRightItemClickListener mListener = null;

    public void setOnRightItemClickListener(onRightItemClickListener listener) {
        mListener = listener;
    }

    public interface onRightItemClickListener {
        void onRightItemClick(View v, int position);
    }
}
