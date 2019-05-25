package com.yijia.common_yijia.main.index;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bokang.yijia.mobshare.platform.wechat.friends.WechatShare;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.ui.dialog.JDialogUtil;
import com.example.yijia.ui.sms.JSmsUtil;
import com.example.yijia.util.log.LatteLogger;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.YjBottomDelegate_with3;
import com.yijia.common_yijia.sign.ISignListener;
import com.yijia.common_yijia.sign.YjSignHandler;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


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
    long familyId=-1;
    long relationTypeId =-1;
    //是否指定为监护人：
    //int isGuardian
    //1-是，2-否
    private long isGuardian = 2;

    private String weChartdesc=null;
    private String imageUrl=null;
    private String linkUrl=null;
    private String smsMsg=null;
    private String weChattitle=null;

    public static final String INVITECODE = "InviteCode";
    public static final String FAMILYID = "familyId";
    public static final String RELATIONID = "relationTypeId";
    public static final String INVITYTYPE = "invitetype";

    public static final int INVITE_FOR_MINE=1;
    public static final int INVITE_FOR_OTHER=2;
    public static final int INVITE_PARENT=3;
    private int inviteType=0;

    public boolean isFirst=false;

    @OnClick(R2.id.tv_back)
    void back() {
        getSupportDelegate().pop();
//        getSupportDelegate().popTo(YjBottomDelegate_with3.class,false);
        if(isFirst){
            getSupportDelegate().pop();
            YjSignHandler.onSkipAddParents(signListener);
        }
    }

    @OnClick(R2.id.tv_wechat)
    void inviteWechat() {
        showShare();
    }

    @OnClick(R2.id.tv_note)
    void inviteNote() {
        JSmsUtil.INSTENCE.sendSmsWithBody(Objects.requireNonNull(getContext()), "", smsMsg);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
    }


    private final static String TAG = InviteDelagate.class.getSimpleName();

    public static InviteDelagate create(long familyId,long relationId,int type) {
        final Bundle args = new Bundle();
        args.putLong(FAMILYID, familyId);
        args.putLong(RELATIONID, relationId);
        args.putInt(INVITYTYPE, type);
//        args.putString(INVITECODE, inviteCode);
        final InviteDelagate delegate = new InviteDelagate();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        assert args != null;
        familyId = args.getLong(FAMILYID);
        relationTypeId = args.getLong(RELATIONID);
        inviteType = args.getInt(INVITYTYPE);
//        inviteCode = args.getString(INVITECODE);
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_invite;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        init();
        getInvite();
    }


    private void init() {
        inviteCode = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getInviteCode();
        tvSave.setVisibility(View.INVISIBLE);
        tvTitle.setText("邀请");
        String content = tvInvitecode.getText().toString();
//        tvInvitecode.setText(content.replace("XXXXXX", inviteCode));
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
        ws.shareWebpager(weChartdesc,weChattitle,linkUrl,imageUrl);
    }

    private void getInvite() {
        JDialogUtil.INSTANCE.showRxDialogShapeLoading(getContext());
        final String url = "friend/query_invite_code";
        String token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        RxRestClient.builder()
                .url(url)
                .params("yjtk", token)
                .params("inviteType", inviteType)
                .params("familyId", familyId)
                .params("relationTypeId", relationTypeId)
                .build()
                .get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        LatteLogger.json("delete_circle", response);
                        JSONObject object=JSON.parseObject(response);
                        final String status = object.getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            JSONObject data=object.getJSONObject("data");
                            weChartdesc=data.getString("weChartdesc");
                            imageUrl=data.getString("imageUrl");
                            linkUrl=data.getString("linkUrl");
                            smsMsg=data.getString("smsMsg");
                            weChattitle=data.getString("weChattitle");

                        } else {
                            final String msg = JSON.parseObject(response).getString("msg");
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                            getSupportDelegate().pop();
                        }
                        JDialogUtil.INSTANCE.dismiss();

                    }

                    @Override
                    public void onFail(Throwable e) {
                        Toast.makeText(getContext(), "请稍后尝试", Toast.LENGTH_SHORT).show();
                        JDialogUtil.INSTANCE.dismiss();
                        getSupportDelegate().pop();
                    }
                });
    }

    private ISignListener signListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        signListener = (ISignListener) activity;
    }
}
