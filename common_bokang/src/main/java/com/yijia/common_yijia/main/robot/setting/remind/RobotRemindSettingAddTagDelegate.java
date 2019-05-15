package com.yijia.common_yijia.main.robot.setting.remind;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.latte.ec.R;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.yijia.app.Latte;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.ui.TextViewUtils;
import com.example.yijia.util.callback.CallbackManager;
import com.example.yijia.util.callback.CallbackType;
import com.example.yijia.util.callback.IGlobalCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JiaLei, Email 15033111957@163.com, Date on 2019/5/14.
 *
 *            .,,       .,:;;iiiiiiiii;;:,,.     .,,
 *          rGB##HS,.;iirrrrriiiiiiiiiirrrrri;,s&##MAS,
 *         r5s;:r3AH5iiiii;;;;;;;;;;;;;;;;iiirXHGSsiih1,
 *            .;i;;s91;;;;;;::::::::::::;;;;iS5;;;ii:
 *          :rsriii;;r::::::::::::::::::::::;;,;;iiirsi,
 *       .,iri;;::::;;;;;;::,,,,,,,,,,,,,..,,;;;;;;;;iiri,,.
 *    ,9BM&,            .,:;;:,,,,,,,,,,,hXA8:            ..,,,.
 *   ,;&@@#r:;;;;;::::,,.   ,r,,,,,,,,,,iA@@@s,,:::;;;::,,.   .;.
 *    :ih1iii;;;;;::::;;;;;;;:,,,,,,,,,,;i55r;;;;;;;;;iiirrrr,..
 *   .ir;;iiiiiiiiii;;;;::::::,,,,,,,:::::,,:;;;iiiiiiiiiiiiri
 *   iriiiiiiiiiiiiiiii;;;::::::::::::::::;;;iiiiiiiiiiiiiiiir;
 *  ,riii;;;;;;;;;;;;;:::::::::::::::::::::::;;;;;;;;;;;;;;iiir.
 *  iri;;;::::,,,,,,,,,,:::::::::::::::::::::::::,::,,::::;;iir:
 * .rii;;::::,,,,,,,,,,,,:::::::::::::::::,,,,,,,,,,,,,::::;;iri
 * ,rii;;;::,,,,,,,,,,,,,:::::::::::,:::::,,,,,,,,,,,,,:::;;;iir.
 * ,rii;;i::,,,,,,,,,,,,,:::::::::::::::::,,,,,,,,,,,,,,::i;;iir.
 * ,rii;;r::,,,,,,,,,,,,,:,:::::,:,:::::::,,,,,,,,,,,,,::;r;;iir.
 * .rii;;rr,:,,,,,,,,,,,,,,:::::::::::::::,,,,,,,,,,,,,:,si;;iri
 *  ;rii;:1i,,,,,,,,,,,,,,,,,,:::::::::,,,,,,,,,,,,,,,:,ss:;iir:
 *  .rii;;;5r,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,sh:;;iri
 *   ;rii;:;51,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,.:hh:;;iir,
 *    irii;::hSr,.,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,.,sSs:;;iir:
 *     irii;;:iSSs:.,,,,,,,,,,,,,,,,,,,,,,,,,,,..:135;:;;iir:
 *      ;rii;;:,r535r:...,,,,,,,,,,,,,,,,,,..,;sS35i,;;iirr:
 *       :rrii;;:,;1S3Shs;:,............,:is533Ss:,;;;iiri,
 *        .;rrii;;;:,;rhS393S55hh11hh5S3393Shr:,:;;;iirr:
 *          .;rriii;;;::,:;is1h555555h1si;:,::;;;iirri:.
 *            .:irrrii;;;;;:::,,,,,,,,:::;;;;iiirrr;,
 *               .:irrrriiiiii;;;;;;;;iiiiiirrrr;,.
 *                  .,:;iirrrrrrrrrrrrrrrrri;:.
 *                        ..,:::;;;;:::,,.
 */
public class RobotRemindSettingAddTagDelegate extends LatteDelegate {
    AppCompatTextView tvTitle;
    RelativeLayout rl;
    AppCompatEditText etTag;
    String tag;
    private static final String TAG="tag";

    @Override
    public Object setLayout() {
        return R.layout.delegate_robot_setting_remind_add_tag;
    }

    public static RobotRemindSettingAddTagDelegate create(String tag) {
        final Bundle args = new Bundle();
        args.putString(TAG, tag);
        final RobotRemindSettingAddTagDelegate delegate = new RobotRemindSettingAddTagDelegate();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        assert args != null;
        tag = args.getString(TAG);

    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initVIew(rootView);

    }

    private void initVIew(View rootView) {
        rl = rootView.findViewById(R.id.tv_back);
        rl.setOnClickListener(v -> {
            getSupportDelegate().pop();
        });
        tvTitle = rootView.findViewById(R.id.tv_title);
        TextViewUtils.AppCompatTextViewSetText(tvTitle, "标签");
        AppCompatTextView tvSave = rootView.findViewById(R.id.tv_save);
        TextViewUtils.AppCompatTextViewSetText(tvSave, "保存");
        etTag=rootView.findViewById(R.id.et);
        etTag.setText(tag);
        tvSave.setOnClickListener(v->{
            String mTag=etTag.getText().toString();
            if(TextUtils.isEmpty(mTag)){
                showToast("请输入标签");
                return;
            }
            beforeExit(mTag);
            getSupportDelegate().pop();
        });
    }


    private void beforeExit(String s) {
        final IGlobalCallback<String> callback;
        callback = CallbackManager
                .getInstance()
                .getCallback(CallbackType.ROBOT_REMIND_TAG);
        if (callback != null) {
            callback.executeCallback(s);
        }
    }


}
