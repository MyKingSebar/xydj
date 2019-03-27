package com.yijia.common_yijia.main.index;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.bokang.yijia.mobshare.platform.wechat.friends.WechatShare;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.ui.sms.JSmsUtil;
import com.yijia.common_yijia.database.YjDatabaseManager;

import butterknife.BindView;
import butterknife.OnClick;


public class InviteDelagate extends LatteDelegate {
    @BindView(R2.id.tv_save)
    AppCompatTextView tvSave;
    @BindView(R2.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R2.id.tv_invitecode)
    AppCompatTextView tvInvitecode;
    @BindView(R2.id.cb_guardian)
    AppCompatCheckBox cbGuardian;
    String inviteCode = null;
    //是否指定为监护人：
    //int isGuardian
    //1-是，2-否
    private long isGuardian = 2;


    @OnClick(R2.id.tv_back)
    void back() {
        getSupportDelegate().pop();
    }

    @OnClick(R2.id.tv_wechat)
    void inviteWechat() {
        showShare();
    }

    @OnClick(R2.id.tv_note)
    void inviteNote() {
        JSmsUtil.INSTENCE.sendSmsWithBody(getContext(), "", "ssssssssssssssssssssssssssssssss");
    }


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
    }


    private final static String TAG = InviteDelagate.class.getSimpleName();


    @Override
    public Object setLayout() {
        return R.layout.delegate_invite;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        init();
    }


    private void init() {
        inviteCode = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getInviteCode();
        tvSave.setVisibility(View.INVISIBLE);
        tvTitle.setText("邀请");
        String content = tvInvitecode.getText().toString();
        tvInvitecode.setText(content.replace("XXXXXX", inviteCode));
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void showShare() {
//        OnekeyShare oks = new OnekeyShare();
//        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
//
//        // title标题，微信、QQ和QQ空间等平台使用
//        oks.setTitle("aaaaaaaaaaaaaaaaaaaaaaaaaa");
//        // titleUrl QQ和QQ空间跳转链接
//        oks.setTitleUrl("http://sharesdk.cn");
//        // text是分享文本，所有平台都需要这个字段
//        oks.setText("我是分享文本");
//        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1359079573,3507998644&fm=26&gp=0.jpg");//确保SDcard下面存在此张图片
//        // url在微信、微博，Facebook等平台中使用
//        oks.setUrl("http://sharesdk.cn");
//        // comment是我对这条分享的评论，仅在人人网使用
//        oks.setComment("我是测试评论文本");
//        // 启动分享GUI
//        oks.show(getContext());
        WechatShare ws=new WechatShare();
        if(cbGuardian.isChecked()){
            isGuardian=1;
        }
        ws.shareWebpager("关注以后就可以看到一家人的所有照片啦!","我用小壹机器人记录一家人的回忆快来关注吧!","http://192.168.1.113:8880/?inviteCode="+inviteCode+"&isGuardian="+isGuardian,"http://47.105.157.64/group1/M00/00/05/rB8Ql1yaIqSAXICMAAAockYvlx4722.png");
    }
}
