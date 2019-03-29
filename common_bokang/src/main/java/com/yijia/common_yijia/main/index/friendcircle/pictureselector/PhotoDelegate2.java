package com.yijia.common_yijia.main.index.friendcircle.pictureselector;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.latte.ui.wxvideoedit.EsayVideoEditActivity;
import com.example.yijia.app.Latte;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.ui.dialog.JDialogUtil;
import com.example.yijia.ui.dialog.RxDialogSureCancelListener;
import com.example.yijia.util.log.LatteLogger;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.index.YjIndexDelegate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;


public class PhotoDelegate2 extends LatteDelegate {

    public static long mLong2 = 0;


    boolean isfirst = false;

    @BindView(R2.id.et_text)
    AppCompatEditText etText;
    @BindView(R2.id.rv_imgs)
    RecyclerView recyclerView;

    @OnClick(R2.id.tv_back)
    void back() {
        JDialogUtil.INSTANCE.showRxDialogSureCancel(getContext(), null, 0, "放弃本次编辑内容吗？", new RxDialogSureCancelListener() {
            @Override
            public void RxDialogSure() {
                getSupportDelegate().pop();
                JDialogUtil.INSTANCE.dismiss();
            }

            @Override
            public void RxDialogCancel() {
                JDialogUtil.INSTANCE.dismiss();
            }
        });
    }

    //朋友圈参数
    //1-文本，2-照片，3-语音，4-视频
    private int circleType = 0;
    ////1-文字，2-语音
    private int contentType = 1;
    //可见类型：1-全部可见，2-部分可见，3-部分不可见
    private int visibleType = 1;
    private int[] visibleOrInvisibleUserIds = {};
    private List<Integer> visibleOrInvisibleUserIds2 = new ArrayList<>();
    private String location = "";
    private double longitude = 0;
    private double latitude = 0;

    private String urlType = "pictureUrl";
    private String urlTop = null;
    @OnClick(R2.id.tv_save)
    void save() {


        final String token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        LatteLogger.w("upLoadImg", "" + chooseMode);
        switch (chooseMode) {

            case ALLMODE:
                if (selectList.size() == 0) {
                    showToast("请选择");
                    return;
                } else {
                    LocalMedia media = selectList.get(0);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case IMAGEMODE:
                            circleType = 2;
                            urlType = "pictureUrl";
                            urlTop = "picture/upload";
                            upLoadImg(token);
                            break;
                        case VIDEOMODE:
                            circleType = 4;
                            urlType = "videoUrl";
                            urlTop = "video/upload";
                            upLoadImg(token);
                            break;
                        case AUDIOMODE:
                            circleType = 3;
                            urlType = "audioUrl";
                            urlTop = "audio/upload";
                            upLoadImg(token);
                            break;
                        default:
                            break;
                    }

                }
                break;

            case IMAGEMODE:
                circleType = 2;
                urlType = "pictureUrl";
                urlTop = "picture/upload";
                upLoadImg(token);
                break;
            case VIDEOMODE:
                circleType = 4;
                urlType = "videoUrl";
                urlTop = "video/upload";
                upLoadImg(token);
                break;
            case AUDIOMODE:
                circleType = 3;
                urlType = "audioUrl";
                urlTop = "audio/upload";
                upLoadImg(token);
                break;
            case TEXTMODE:
                circleType = 1;

                upLoadInfo(token, etText.getText().toString(), "");

                break;
            default:
                break;

        }


    }

    //最大张数
    private int maxselectnum = 1;
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
    private final int numMode = PictureConfig.SINGLE;
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
    private final int TEXTMODE = 4;
    //    public int ALLMODE = PictureMimeType.ofAll();
//    private final int IMAGEMODE = PictureMimeType.ofImage();
//    private final int VIDEOMODE = PictureMimeType.ofVideo();
//    private final int AUDIOMODE = PictureMimeType.ofAudio();
    private int chooseMode = ALLMODE;


    private GridImageAdapter adapter;


    private final static String TAG = PhotoDelegate2.class.getSimpleName();
    private View rootView;
    private List<LocalMedia> selectList = new ArrayList<>();

    private static int successFiles = 0;


    @Override
    public Object setLayout() {
        return R.layout.delegate_edit_picture;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        if (chooseMode == IMAGEMODE || chooseMode == VIDEOMODE || chooseMode == AUDIOMODE||chooseMode ==ALLMODE) {
            isfirst = true;
        }
        init();
    }


    private void init() {
        etText.setCursorVisible(true);
        switch (chooseMode) {
            case ALLMODE:
                isGif = false;
                //定制
                previewAutio = false;
                maxselectnum = 1;
                break;
            case IMAGEMODE:
                previewVideo = false;
                maxselectnum = 1;
                break;
            case VIDEOMODE:
                previewImg = false;
                isGif = false;
                maxselectnum = 1;
                break;
            case AUDIOMODE:
                previewImg = false;
                previewAutio = true;
//                previewImg=true;
                maxselectnum = 1;
                break;
            case TEXTMODE:
                recyclerView.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        if (isCutCricle) {
            aspect_ratio_x = 1;
            aspect_ratio_y = 1;
            isShowCutFrame = false;
            isShowCutGride = false;
        }


        FullyGridLayoutManager manager = new FullyGridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(getContext(), onAddPicClickListener);
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
                        PictureSelector.create(PhotoDelegate2.this).themeStyle(THEMEID).openExternalPreview(position, selectList);
                        break;
                    case 2:
                        // 预览视频
                        PictureSelector.create(PhotoDelegate2.this).externalPictureVideo(media.getPath());
                        break;
                    case 3:
                        // 预览音频
                        PictureSelector.create(PhotoDelegate2.this).externalPictureAudio(media.getPath());
                        break;
                    default:
                        break;
                }
            }
        });

        if (isfirst) {
            intoPick();
            isfirst = false;
        }
    }

    private void intoPick() {
        boolean mode = ALBUMORNOT;
        if (mode) {
            // 进入相册 以下是例子：不需要的api可以不写
            PictureSelector.create(PhotoDelegate2.this)
                    .openGallery(chooseMode)
                    .theme(THEMEID)
                    .maxSelectNum(maxselectnum)
                    .minSelectNum(1)
                    .selectionMode(numMode)
                    .previewImage(previewImg)
                    .previewVideo(previewVideo)
                    .compress(isCompress)

                    .enablePreviewAudio(previewAutio) // 是否可播放音频
                    .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
                    .recordVideoSecond(15)//视频秒数录制 默认60s int
                    .videoQuality(0)// 视频录制质量 0 or 1 int
                    .cropCompressQuality(50)// 裁剪压缩质量 默认90 int
                    .isCamera(ISCAMERA)
                    .enableCrop(isCrop)

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
            PictureSelector.create(PhotoDelegate2.this)
                    .openCamera(chooseMode)
                    .theme(THEMEID)
                    .maxSelectNum(maxselectnum)
                    .minSelectNum(1)
                    .selectionMode(numMode)
                    .previewImage(previewImg)
                    .previewVideo(previewVideo)
                    .compress(isCompress)
                    .enablePreviewAudio(previewAutio) // 是否可播放音频
                    .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
                    .recordVideoSecond(15)//视频秒数录制 默认60s int
                    .videoQuality(0)// 视频录制质量 0 or 1 int
                    .cropCompressQuality(10)// 裁剪压缩质量 默认90 int
                    .isCamera(ISCAMERA)
                    .enableCrop(isCrop)

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

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            intoPick();
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
                    if (selectList.size() == 0) {
                        return;
                    }
                    LocalMedia media = selectList.get(0);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 图片
//                            PictureSelector.create(PhotoDelegate2.this).themeStyle(THEMEID).openExternalPreview(position, selectList);
                            break;
                        case 2:
                            // 视频
                            LatteLogger.d("path", "path:" + selectList.get(0).getPath());
                            int time = getPlayTime(selectList.get(0).getPath());
                            LatteLogger.d("path", "time:" + time);
                            String path = selectList.get(0).getPath();
                            if (time / 100 > 50) {
                                showToast("您的视频大于5秒，请裁剪后发送！");
                                Intent intent = new Intent();
                                intent.putExtra(EsayVideoEditActivity.PATH, path);
                                intent.setClass(getContext(), EsayVideoEditActivity.class);
                                startActivityForResult(intent, EsayVideoEditActivity.CHOOSEVIDEO_REQUEST);
                            } else {
                            }
                            LatteLogger.d("selectList", "selectList.getCompressPath:" + selectList.get(0).getCompressPath());
                            break;
                        case 3:
                            // 音频
//                            PictureSelector.create(PhotoDelegate2.this).externalPictureAudio(media.getPath());
                            break;
                        default:
                            break;
                    }
//                    switch (chooseMode) {
//                        case VIDEOMODE:
//                            if (selectList.size() == 0) {
//                                return;
//                            }
//                            LatteLogger.d("path", "path:" + selectList.get(0).getPath());
//                            int time = getPlayTime(selectList.get(0).getPath());
//                            LatteLogger.d("path", "time:" + time);
//                            String path = selectList.get(0).getPath();
////                            if (time == null) {
////                                showToast("读取视频时间失败");
////                                return;
////                            }
////                            Long timeLong = Long.getLong(time);
////                            if (timeLong / 1000 > 5) {
//                            if (time / 100 > 50) {
//                                showToast("您的视频大于5秒，请裁剪后发送！");
//                                Intent intent = new Intent();
//                                intent.putExtra(EsayVideoEditActivity.PATH, path);
//                                intent.setClass(getContext(), EsayVideoEditActivity.class);
//                                startActivityForResult(intent, EsayVideoEditActivity.CHOOSEVIDEO_REQUEST);
//                            } else {
////                                // 选择本地视频压缩
////                                LocalMediaConfig.Buidler buidler = new LocalMediaConfig.Buidler();
////                                final LocalMediaConfig config = buidler
////                                        .setVideoPath(selectList.get(0).getPath())
////                                        .captureThumbnailsTime(1)
////                                        .doH264Compress(new AutoVBRMode())
////                                        .setFramerate(15)
////                                        .setScale(1.0f)
////                                        .build();
////                                OnlyCompressOverBean onlyCompressOverBean = new LocalMediaCompress(config).startCompress();
//                            }
//                            LatteLogger.d("selectList", "selectList.getCompressPath:" + selectList.get(0).getCompressPath());
//                            break;
//                        default:
//                            break;
//                    }
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    break;

                case EsayVideoEditActivity.CHOOSEVIDEO_REQUEST:
                    LatteLogger.d("jiancai", "CHOOSEVIDEO_REQUEST");
                    String videopath = data.getStringExtra(EsayVideoEditActivity.PATH);
                    LatteLogger.d("jiancai", "videopath" + videopath);
                    if (!TextUtils.isEmpty(videopath)) {
                        selectList.get(0).setPath(videopath);
                        adapter.setList(selectList);
                        adapter.notifyDataSetChanged();
                    }

                    break;

                default:
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

    private File[] getFiles(List<LocalMedia> list) {
        if (list != null) {
            int size = list.size();
            File[] files = new File[size];
            for (int i = 0; i < size; i++) {
                files[i] = new File(list.get(i).getPath());
            }
            return files;
        } else {
            LatteLogger.w("upLoadImg", "getFiles() == null");
            return null;
        }
    }

    private List<String> getFilesStringList() {
        if (selectList != null) {
            int size = selectList.size();
            List<String> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                list.add(selectList.get(i).getCompressPath());
            }
            return list;
        } else {
            LatteLogger.w("upLoadImg", "getFilesStringList == null");
            return null;
        }
    }

    private void RxUpLoad(String token, File[] files) {
        LatteLogger.e("jialei", "RxUpLoad:" + urlTop);
        RxRestClient.builder()
                .url(urlTop)
                .params("yjtk", token)
//                .params("files", new File[]{new File(imgPath)})
                .files(files)
                .build()
                .uploadwithparams()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        LatteLogger.json("picture/upload", response);
                        final JSONObject object = JSON.parseObject(response);
                        final String status = object.getString("status");
                        if (TextUtils.equals(status, "1001")) {

                            final JSONObject dataObject = object.getJSONObject("data");
                            final String filePath = dataObject.getString("path");
                            upLoadInfo(token, etText.getText().toString(), filePath);
                        } else {
                            Toast.makeText(getContext(), object.getString("msg"), Toast.LENGTH_SHORT).show();
                            JDialogUtil.INSTANCE.dismiss();
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        LatteLogger.e("picture/upload", "onFail:" + e.getMessage());
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        JDialogUtil.INSTANCE.dismiss();
                    }
                });
    }

    private void upLoadImg(String token) {
        JDialogUtil.INSTANCE.showRxDialogShapeLoading(getContext());
        mLong2 = 0;
        LatteLogger.w("upLoadImg", "upLoadImg");
//        final String url = "picture/upload";

        if (TextUtils.isEmpty(urlTop)) {
            LatteLogger.w("upLoadImg", "urlTop == null");
            return;
        }
        switch (chooseMode) {
            case IMAGEMODE:
                luBan(token);
                break;
            default:
                File[] files = getFiles(selectList);
                if (files == null) {
                    LatteLogger.w("upLoadImg", "files == null");
                    return;
                }
                RxUpLoad(token, files);
                break;
        }

    }

    private String getPath() {
        String path = Environment.getExternalStorageDirectory() + "/Luban/image/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }

    private void luBan(String token) {
        LatteLogger.w("upLoadImg", "luBan");
//        File[] files = getFiles(selectList);
//        long mLong=0;
//
//        for(int i=0;i<files.length;i++){
//            mLong+=files[i].length();
//        }
//        LatteLogger.w("upLoadImg", "mLong"+mLong);
        final List<String> filesString = getFilesStringList();
        if (filesString == null) {
            LatteLogger.e("filesString", "filesString==null");
            return;
        }
        final int size = filesString.size();
        File[] files = new File[size];
        successFiles = 0;
        Luban.with(Latte.getApplicationContext())
                .load(filesString)
                .ignoreBy(100)
                .setTargetDir(getPath())
                .setCompressListener(
                        new OnCompressListener() {
                            @Override
                            public void onStart() {
                                LatteLogger.w("upLoadImg", "onStart");
                            }

                            @Override
                            public void onSuccess(File file) {
                                files[successFiles] = file;
                                successFiles++;
                                if (successFiles == size) {
                                    RxUpLoad(token, files);
                                }
//                        mLong2+=file.length();
//                        LatteLogger.w("upLoadImg", "mLong2:" + mLong2);
                            }

//                    @Override
//                                         public void onSuccess(List<LocalMedia> list) {
//                                             LatteLogger.w("upLoadImg", "onSuccess" + list.size());
//                                             RxUpLoad(token, getFiles(list));
//                                         }


                            @Override
                            public void onError(Throwable e) {
                                LatteLogger.w("upLoadImg", "onError" + e.getMessage());
                            }
                        }
                ).launch();
    }

    private void upLoadInfo(String token, String content, String filesString) {
        LatteLogger.w("upLoadImg", "upLoadInfo");
        final String url = "circle/insert";
        if (circleType == 1) {
            filesString = "";
        }
        LatteLogger.w("upLoadImg", "yjtk:" + token + "circleType:" + circleType + "contentType:" + contentType + "content:" + content + urlType + ":" + filesString + "visibleType:" + visibleType + "visibleOrInvisibleUserIds:" + Arrays.toString(visibleOrInvisibleUserIds) +
                "location:" + location + "longitude:" + longitude + "latitude:" + latitude);
        RxRestClient.builder()
                .url(url)
                .params("yjtk", token)
                //circleType  1-文本，2-照片，3-语音，4-视频，5-家书
                .params("circleType", circleType)
                .params("contentType", contentType)//1-文字，2-语音
                .params("content", content)
                .params("audioUrl", "")
                .params("pictureUrl", "")
                .params("videoUrl", "")
                .params("videoCoverUrl", "")
                .params(urlType, filesString)
                .params("visibleType", visibleType)
//                .params("visibleOrInvisibleUserIds",JSONArray.parseArray(JSON.toJSONString(visibleOrInvisibleUserIds2)))
                //TODO 如果不行就用new Gson（）.toJson()这个
//                .params("visibleOrInvisibleUserIds", Arrays.toString(visibleOrInvisibleUserIds))
                .params("visibleOrInvisibleUserIds", Arrays.toString(visibleOrInvisibleUserIds))
                .params("location", location)
                .params("longitude", longitude)
                .params("latitude", latitude)
                .build()
                .post()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        LatteLogger.json("circle/insert", response);
                        final String status = JSON.parseObject(response).getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            getSupportDelegate().pop();
                            //清缓存
                            PictureFileUtils.deleteCacheDirFile(Latte.getApplicationContext());
                            JDialogUtil.INSTANCE.dismiss();
                        } else {
                            final String msg = JSON.parseObject(response).getString("msg");
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                            JDialogUtil.INSTANCE.dismiss();
                        }

                    }

                    @Override
                    public void onFail(Throwable e) {
                        LatteLogger.json("circle/insert", "onFail:" + e.getMessage());
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        JDialogUtil.INSTANCE.dismiss();
                    }
                });
    }


    private int getPlayTime(String mUri) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        int durning = 0;
        try {
            mediaPlayer.setDataSource(mUri);
            mediaPlayer.prepare();
            durning = mediaPlayer.getDuration();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return durning;


//        android.media.MediaMetadataRetriever mmr = new android.media.MediaMetadataRetriever();
//        try {
//            if (mUri != null) {
//                HashMap<String, String> headers = null;
//                if (headers == null) {
//                    headers = new HashMap<String, String>();
//                    headers.put("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.4.2; zh-CN; MW-KW-001 Build/JRO03C) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 UCBrowser/1.0.0.001 U4/0.8.0 Mobile Safari/533.1");
//                }
//                mmr.setDataSource(mUri, headers);
//            } else {
//                //mmr.setDataSource(mFD, mOffset, mLength);
//            }
//
//            String duration = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);//时长(毫秒)
////            String width = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);//宽
////            String height = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);//高
//
////            Toast.makeText(this, "playtime:" + duration + "w=" + width + "h=" + height, Toast.LENGTH_SHORT).show();
//            LatteLogger.d("duration", duration);
//
//            return duration;
//        } catch (Exception ex) {
//            LatteLogger.e("TAG", "MediaMetadataRetriever exception " + ex);
//        } finally {
//            mmr.release();
//        }
//
//        return null;
    }


}
