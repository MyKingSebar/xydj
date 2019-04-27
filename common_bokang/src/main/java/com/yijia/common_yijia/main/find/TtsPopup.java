package com.yijia.common_yijia.main.find;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.example.commcon_xfyun.Lat;
import com.example.commcon_xfyun.LatCallbackInterface;
import com.example.latte.ui.R;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.qcloud.bokang.BokangChatManager;
import com.yijia.common_yijia.main.message.trtc.BoKangSendMessageListener;
import com.yijia.common_yijia.main.message.trtc.BokangSendMessageUtil;

import razerdp.basepopup.BasePopupWindow;

public class TtsPopup extends BasePopupWindow implements LatCallbackInterface {
    private AppCompatTextView mCancelButton;
    private AppCompatTextView mCompeleteButton;
    private AppCompatEditText mInputEdittext;
    private Context mContext = null;
    private String tencentId = null;
    private TtsPopuCallBack mTtsPopuCallBack = null;

    public void setmTtsPopuCallBack(TtsPopuCallBack mTtsPopuCallBack) {
        this.mTtsPopuCallBack = mTtsPopuCallBack;
    }

    private Lat mlat = null;
    StringBuffer mStringBuffer = null;
    //光标所在位置
    int index = 0;


    TIMConversation conversation = null;
    BokangSendMessageUtil bokangSendMessageUtil = null;

    public TtsPopup(Context context) {
        super(context);
        mContext = context;
        mCancelButton = findViewById(R.id.tv_cancel);
        mCancelButton.setOnClickListener(v -> {
            dismiss();
        });
        mCompeleteButton = findViewById(R.id.tv_ok);
        mCompeleteButton.setOnClickListener(v -> {
            if (TextUtils.isEmpty(tencentId)) {
                Toast.makeText(mContext, "tencentId==null", Toast.LENGTH_LONG).show();
                dismiss();
                return;
            }
            if (TextUtils.isEmpty(mInputEdittext.getText())) {
                Toast.makeText(mContext, "还未输入文字", Toast.LENGTH_LONG).show();
                return;
            }
            if (mTtsPopuCallBack != null) {
                mTtsPopuCallBack.TtsBack(mInputEdittext.getText().toString());
            } else {
                bokangSendMessageUtil.sendMessage(bokangSendMessageUtil.buildBokangTextMessage(mInputEdittext.getText().toString()));
            }
            mStringBuffer.setLength(0);
            mInputEdittext.setText("");
            dismiss();
        });
        mInputEdittext = findViewById(R.id.et_text);
        mStringBuffer = new StringBuffer();
        checkLat();
        setClipChildren(false);
        setAutoShowInputMethod(mInputEdittext, true);
        setPopupGravity(Gravity.CENTER);

    }

    public TtsPopup(Context context, boolean delayInit) {
        super(context, delayInit);
    }

    public TtsPopup(Context context, int width, int height) {
        super(context, width, height);
    }

    public TtsPopup(Context context, int width, int height, boolean delayInit) {
        super(context, width, height, delayInit);
    }

    private void initLat() {
        checkLat();
        index = mInputEdittext.getSelectionStart();
        mlat.iatStart();
    }

    public void setId(String tencentId) {
        this.tencentId = tencentId;
    }

    // 必须实现，这里返回您的contentView
    // 为了让库更加准确的做出适配，强烈建议使用createPopupById()进行inflate
    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.basepopu_tts);
    }

    // 以下为可选代码（非必须实现）
    // 返回作用于PopupWindow的show和dismiss动画，本库提供了默认的几款动画，这里可以自由实现
    @Override
    protected Animation onCreateShowAnimation() {
        return getDefaultScaleAnimation(true);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getDefaultScaleAnimation(false);
    }


    private void checkLat() {
        if (null == mlat) {
            Context context = getContext();
            if (null == context) {
                return;
            }
            mlat = new Lat(mContext, Lat.INPUTASS, this);
        }
    }

    @Override
    public void latSuccess(String s) {
        Log.d("latSuccess", "latSuccess:" + s);
        mStringBuffer.setLength(0);
        mStringBuffer.append(mInputEdittext.getText().toString());
        if (index < 0 || index >= mStringBuffer.length()) {
            mStringBuffer.append(s);
        } else {
            mStringBuffer.insert(index, s);//光标所在位置插入文字
        }
        mInputEdittext.setText(mStringBuffer);
        index += s.length();
        mInputEdittext.setSelection(index);
        initLat();
    }

    @Override
    public void latError(String s) {
        initLat();
    }

    @Override
    public void showPopupWindow(View anchorView) {
        super.showPopupWindow(anchorView);
        initLatAndIm();
    }

    private void initLatAndIm() {
        initLat();
        initIm();
    }

    private void initIm() {
        if (!TextUtils.isEmpty(tencentId)) {
            conversation = TIMManager.getInstance().getConversation(TIMConversationType.C2C, tencentId);
        } else {
            Toast.makeText(mContext, "获取会话id失败", Toast.LENGTH_LONG).show();
            dismiss();
        }
        if (conversation == null) {
            Toast.makeText(mContext, "获取会话失败", Toast.LENGTH_LONG).show();
            dismiss();
        }
        bokangSendMessageUtil = new BokangSendMessageUtil(conversation, mBoKangSendMessageListener, mContext);
    }

    @Override
    public void showPopupWindow() {
        super.showPopupWindow();
        initLatAndIm();
    }

    BoKangSendMessageListener mBoKangSendMessageListener = new BoKangSendMessageListener() {
        @Override
        public void messageSuccess(TIMMessage timMessage) {
            Log.e("jialei", "TtsPopu:success" + timMessage);
        }

        @Override
        public void messageError(int code, String desc) {
            Log.e("jialei", "TtsPopu:messageError" + code + ",+desc");
        }
    };

    @Override
    public void dismiss() {
        super.dismiss();
        mlat.iatCancel();
    }
}
