package com.yijia.common_yijia.main.personaldata;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.latte.delegates.LatteDelegate;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.latte.ui.widget.HeadLayout;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.database.YjUserProfile;
import com.yijia.common_yijia.main.mine.setup.UpDateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SetUserHeadDelegate extends LatteDelegate implements HeadLayout.OnClickHeadReturn, HeadLayout.OnClickHeadHeadImage, View.OnClickListener {
    @BindView(R2.id.head_layout)
    HeadLayout headLayout;
    @BindView(R2.id.big_head)
    ImageView bigHead;
    private View contentView;
    PopupWindow mPopWindow;
    TranslateAnimation animation;
    private List<LocalMedia> selectList = new ArrayList<>();
    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate();

    @Override
    public Object setLayout() {
        return R.layout.delegate_setuserhead;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initHead();
    }

    private void initHead() {
        headLayout.setHeadleftImg(true, R.mipmap.fanhui);
        headLayout.setHeadName("头像", "#FDBA63", 18);
        headLayout.setRightHeadImage(R.mipmap.sangedian, true);
        headLayout.setHeadlayoutBagColor("#000000");
        headLayout.setOnClickHeadReturn(this);
        headLayout.setOnClickHeadRightImage(this);
    }
    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        String imagePath = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getImagePath();
        Glide.with(_mActivity)
                .load(imagePath)
                .into(bigHead);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (contentView != null) {
            contentView = null;
        }
        if (mPopWindow != null) {
            mPopWindow = null;
        }
        if (animation != null) {
            animation = null;
        }
    }

    @Override
    public void onClickHeadReturn() {
        getSupportDelegate().pop();
    }

    @Override
    public void onClickHeadHeadImage() {
        //设置contentView
        contentView = LayoutInflater.from(getActivity()).inflate(R.layout.userhead_popupwindow, null);
        mPopWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);

        animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(200);

        //设置各个控件的点击响应
        TextView tv1 = contentView.findViewById(R.id.photo_select);
        TextView tv2 = contentView.findViewById(R.id.cancel);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        //显示PopupWindow
        mPopWindow.setOutsideTouchable(true);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        contentView.startAnimation(animation);
        mPopWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.photo_select) {
            //图片选择
            getImage();
            mPopWindow.dismiss();
        }else if (i == R.id.cancel){
            Toast.makeText(_mActivity, "取消", Toast.LENGTH_SHORT).show();
            mPopWindow.dismiss();
        }
    }

    private void getImage() {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(1)
                .minSelectNum(1)
                .selectionMode(PictureConfig.SINGLE)
                .previewImage(true)
                .isCamera(true)
                .enableCrop(true)
                .compress(true)
                .glideOverride(160, 160)
                .previewEggs(true)
                .withAspectRatio(1, 1)
                .hideBottomControls(false)
                .freeStyleCropEnabled(true)
                .showCropFrame(true)
                .showCropGrid(true)
                .selectionMedia(selectList)
                .forResult(500);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 500:
                    // 图片选择
                    selectList = PictureSelector.obtainMultipleResult(data);
                    if (!selectList.isEmpty()) {
                        String imgPath = selectList.get(0).getPath();
                        UpDateUtils.updatePersonalData(_mActivity, null, imgPath, new UpDateUtils.UpDateSuccessAndError() {
                            @Override
                            public void successAndError(UpDateUtils.UpDatePersonal upDatePersonal) {
                                if (upDatePersonal==UpDateUtils.UpDatePersonal.SUCCESS){
                                    YjUserProfile profile = YjDatabaseManager.getInstance().getDao().loadAll().get(0);
                                    profile.setImagePath(imgPath);
                                    YjDatabaseManager.getInstance().getDao().update(profile);
                                    Glide.with(_mActivity)
                                            .load(imgPath)
                                            .into(bigHead);
                                    Toast.makeText(_mActivity, "修改头像成功", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(_mActivity, "修改头像失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                    break;
            }
        }
    }

}