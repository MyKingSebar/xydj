package com.yijia.common_yijia.main.index.pictureselector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.latte.delegates.LatteDelegate;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.yijia.common_yijia.main.index.YjIndexDelegate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class PhotoFragment extends LatteDelegate {

    @BindView(R2.id.et_text)
    AppCompatEditText etText;
    @BindView(R2.id.rv_imgs)
    RecyclerView recyclerView;

    @OnClick(R2.id.tv_back)
     void back() {
        getSupportDelegate().pop();
    }

    @OnClick(R2.id.tv_save)
     void save() {
        getSupportDelegate().pop();
    }

    //最大张数
    private  int maxselectnum = 9;
    //样式
    private final int THEMEID = R.style.picture_default_style;//R.style.picture_default_style,R.style.picture_white_style,R.style.picture_QQ_style,R.style.picture_Sina_style
    //是否开启点击声音
    private final boolean VOICE = false;
    //是否预览图片
    private boolean previewImg = true;
    //是否预览视频
    private boolean previewVideo = true;
    //是否预览音频
    private boolean previewAutio = false;
    /**
     * 单选or多选
     * PictureConfig.MULTIPLE
     * PictureConfig.SINGLE
     */
    private final int numMode = PictureConfig.MULTIPLE;
    //相册or单独拍照
    private final boolean ALBUMORNOT = true;
    //相册列表是否隐藏拍摄
    private final boolean ISCAMERA = true;
    //是否显示Gif图片
    private boolean isGif = false;
    //是否压缩图片
    private final boolean isCompress = true;
    //是否裁剪图片
    private final boolean isCrop = false;

    //剪裁相关
    private int aspect_ratio_x, aspect_ratio_y;//0:0  1:1 3:4 3:2 16:9
    //是否隐藏裁剪菜单栏
    private final boolean HIDEMENU = false;
    //裁剪框or图片拖动
    private final boolean cutStyle = true;
    //是否显示裁剪边框
    private boolean isShowCutFrame = true;
    //是否显示裁剪框网格
    private boolean isShowCutGride = true;
    //圆形头像裁剪模式
    private final boolean isCutCricle = false;


    /**
     * 模式
     * 图片:PictureMimeType.ofImage();
     * 视频：PictureMimeType.ofVideo();
     * 音频：PictureMimeType.ofAudio();
     */
    private final int ALLMODE = 0;
    private final int IMAGEMODE = 1;
    private final int VIDEOMODE = 2;
    private final int AUDIOMODE = 3;
//    public int ALLMODE = PictureMimeType.ofAll();
//    private final int IMAGEMODE = PictureMimeType.ofImage();
//    private final int VIDEOMODE = PictureMimeType.ofVideo();
//    private final int AUDIOMODE = PictureMimeType.ofAudio();
    private int chooseMode = ALLMODE;


    private GridImageAdapter adapter;


    private final static String TAG = PhotoFragment.class.getSimpleName();
    private View rootView;
    private List<LocalMedia> selectList = new ArrayList<>();


    @Override
    public Object setLayout() {
        return R.layout.delegate_edit_picture;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        init();
    }


    private void init() {

        switch (chooseMode){
            case ALLMODE:
                isGif=false;
                break;
            case IMAGEMODE:
                previewVideo=false;
                maxselectnum=9;
                break;
            case VIDEOMODE:
                previewImg=false;
                isGif=false;
                maxselectnum=1;
                break;
            case AUDIOMODE:
                previewImg=false;
                previewAutio=true;
//                previewImg=true;
                maxselectnum=1;
                break;
        }
        if (isCutCricle) {
            aspect_ratio_x = 1;
            aspect_ratio_y = 1;
            isShowCutFrame = false;
            isShowCutGride = false;
        }


        FullyGridLayoutManager manager = new FullyGridLayoutManager(getActivity(), 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(getActivity(), onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(maxselectnum);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                LocalMedia media = selectList.get(position);
                String pictureType = media.getPictureType();
                int mediaType = PictureMimeType.pictureToVideo(pictureType);
                switch (mediaType) {
                    case 1:
                        // 预览图片
                        PictureSelector.create(PhotoFragment.this).themeStyle(THEMEID).openExternalPreview(position, selectList);
                        break;
                    case 2:
                        // 预览视频
                        PictureSelector.create(PhotoFragment.this).externalPictureVideo(media.getPath());
                        break;
                    case 3:
                        // 预览音频
                        PictureSelector.create(PhotoFragment.this).externalPictureAudio(media.getPath());
                        break;
                }
            }
        });
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            boolean mode = ALBUMORNOT;
            if (mode) {
                // 进入相册 以下是例子：不需要的api可以不写
                PictureSelector.create(PhotoFragment.this)
                        .openGallery(chooseMode)
                        .theme(THEMEID)
                        .maxSelectNum(maxselectnum)
                        .minSelectNum(1)
                        .selectionMode(numMode)
                        .previewImage(previewImg)
                        .previewVideo(previewVideo)
                        .enablePreviewAudio(previewAutio) // 是否可播放音频
                        .isCamera(ISCAMERA)
                        .enableCrop(isCrop)
                        .compress(isCompress)
                        .glideOverride(160, 160)
                        .previewEggs(true)
                        .withAspectRatio(aspect_ratio_x, aspect_ratio_y)
                        .hideBottomControls(HIDEMENU)
                        .isGif(isGif)
                        .freeStyleCropEnabled(cutStyle)
                        .circleDimmedLayer(isCutCricle)
                        .showCropFrame(isShowCutFrame)
                        .showCropGrid(isShowCutGride)
                        .openClickSound(VOICE)
                        .selectionMedia(selectList)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
            } else {
                // 单独拍照
                PictureSelector.create(PhotoFragment.this)
                        .openCamera(chooseMode)
                        .theme(THEMEID)
                        .maxSelectNum(maxselectnum)
                        .minSelectNum(1)
                        .selectionMode(numMode)
                        .previewImage(previewImg)
                        .previewVideo(previewVideo)
                        .enablePreviewAudio(previewAutio) // 是否可播放音频
                        .isCamera(ISCAMERA)
                        .enableCrop(isCrop)
                        .compress(isCompress)
                        .glideOverride(160, 160)
                        .withAspectRatio(aspect_ratio_x, aspect_ratio_y)
                        .hideBottomControls(HIDEMENU)
                        .isGif(isGif)
                        .freeStyleCropEnabled(cutStyle)
                        .circleDimmedLayer(isCutCricle)
                        .showCropFrame(isShowCutFrame)
                        .showCropGrid(isShowCutGride)
                        .openClickSound(VOICE)
                        .selectionMedia(selectList)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
            }
        }
    };



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择
                    selectList = PictureSelector.obtainMultipleResult(data);
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        chooseMode = args.getInt(YjIndexDelegate.PICKTYPE);
    }
}
