package com.yijia.common_yijia.main.index;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.commcon_xfyun.Tts;
import com.example.yijia.app.Latte;
import com.example.yijia.delegates.LatteDelegate;
import com.example.latte.ec.R;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.ui.expandtextview.ExpandTextView;
import com.example.latte.ui.recycler.MultipleFields;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.latte.ui.recycler.MultipleRecyclerAdapter;
import com.example.latte.ui.recycler.MultipleViewHolder;
import com.example.yijia.util.TimeFormat;
import com.example.yijia.util.log.LatteLogger;
import com.example.yijia.util.textViewSpanUtil;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.luck.picture.lib.tools.ScreenUtils;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.yijia.common_yijia.database.YjDatabaseManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public final class YjIndexAdapter extends MultipleRecyclerAdapter {
    private Tts tts = null;


    private YjIndexCommentAdapter mCommentAdapter = null;
    private LatteDelegate latteDelegate = null;

    final int LIKE = 1;
    final int DISLIKE = 2;

    private IIndexItemListener mIndexItemListener = null;
    private IIndexCanReadItemListener mIndexCanReadItemListener = null;

    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate();

    YjIndexAdapter(List<MultipleItemEntity> data, LatteDelegate delegate) {
        super(data);
        latteDelegate = delegate;
        //添加item布局
        addItemType(YjIndexItemType.INDEX_TEXT_ITEM, R.layout.item_index_text);
        addItemType(YjIndexItemType.INDEX_IMAGE_ITEM, R.layout.item_index_image);
        addItemType(YjIndexItemType.INDEX_VOICE_ITEM, R.layout.item_index_voice);
        addItemType(YjIndexItemType.INDEX_VIDEO_ITEM, R.layout.item_index_video);
        addItemType(YjIndexItemType.INDEX_IMAGES_ITEM, R.layout.item_index_images);
        //TODO
        addItemType(YjIndexItemType.INDEX_LETTER_ITEM, R.layout.item_index_letter);

    }


    public void setIndexItemListener(IIndexItemListener listener) {
        this.mIndexItemListener = listener;
    }
    public void setIndexCanReadItemListener(IIndexCanReadItemListener listener) {
        this.mIndexCanReadItemListener = listener;
    }


    @Override
    protected void convert(MultipleViewHolder holder, final MultipleItemEntity entity) {
        super.convert(holder, entity);
        initTts();
        //先取出所有值
        final Long userId = entity.getField(MultipleFields.ID);
        final String content = entity.getField(MultipleFields.TEXT);
        final String userHead = entity.getField(MultipleFields.IMAGE_URL);
        final String userNickname = entity.getField(YjIndexMultipleFields.USER_NICK_NAME);
        final String userRealName = entity.getField(YjIndexMultipleFields.USER_REAL_NAME);
        final int isOwn = entity.getField(YjIndexMultipleFields.ISOWN);
        final int circleId = entity.getField(YjIndexMultipleFields.CIRCLEID);
        final int contentType = entity.getField(YjIndexMultipleFields.CONTENTTYPE);
        final int visibleType = entity.getField(YjIndexMultipleFields.VISIBLETYPE);
        final String location = entity.getField(YjIndexMultipleFields.LOCATION);
        final String longitude = entity.getField(YjIndexMultipleFields.LONGITUDE);
        final String createdTime = entity.getField(YjIndexMultipleFields.CREATEDTIME);
        final JSONArray commentList = entity.getField(YjIndexMultipleFields.COMMENTLIST);
        final String likes = entity.getField(YjIndexMultipleFields.LIKELIST);
        final String[] imgs = entity.getField(YjIndexMultipleFields.IMGS);
        final String voiceUrl = entity.getField(YjIndexMultipleFields.VOICEURL);
        final String videoUrl = entity.getField(YjIndexMultipleFields.VIDEOURL);

        switch (holder.getItemViewType()) {
            case YjIndexItemType.INDEX_TEXT_ITEM:

                initViewText(holder, circleId, userNickname, content, createdTime, likes, commentList.toJSONString());


                break;

            case YjIndexItemType.INDEX_IMAGE_ITEM:

                initViewText(holder, circleId, userNickname, content, createdTime, likes, commentList.toJSONString());
                final AppCompatImageView tvImage = holder.getView(R.id.iv_photo);
                Glide.with(mContext)
                        .load(imgs[0])
                        .apply(OPTIONS)
                        .into(tvImage);

                break;
            case YjIndexItemType.INDEX_IMAGES_ITEM:

                initViewText(holder, circleId, userNickname, content, createdTime, likes, commentList.toJSONString());
                initImages(holder, imgs, imgs);


                break;
            case YjIndexItemType.INDEX_VOICE_ITEM:
                String[] voiceString = voiceUrl.split(",");
                LatteLogger.d("jialei", "voiceString" + voiceUrl);
                initViewText(holder, circleId, userNickname, content, createdTime, likes, commentList.toJSONString());
                initMedias(holder, voiceString);

                break;
            case YjIndexItemType.INDEX_VIDEO_ITEM:
                String[] videoString = videoUrl.split(",");
                LatteLogger.d("jialei", "videoString" + videoUrl);
                initViewText(holder, circleId, userNickname, content, createdTime, likes, commentList.toJSONString());
                initMedias(holder, videoString);
                break;
            case YjIndexItemType.INDEX_LETTER_ITEM:
                initViewText(holder, circleId, userNickname, content, createdTime, likes, commentList.toJSONString());
                initLetter(holder,circleId);
                break;


            default:
                break;
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    //评论PopupWindow
    private PopupWindow popupWindow;
    private View popupView = null;
    private EditText inputComment;
    private String nInputContentText;
    private TextView btn_submit;
    private RelativeLayout rl_input_container;
    private InputMethodManager mInputManager;

    @SuppressLint("WrongConstant")
    private void showPopupcomment(int circleId, String content, MultipleViewHolder holder,long  replyUserId,String name) {
        final int CIRCLEID = circleId;
        final String CONTENT = content;
        if (popupView == null) {
            //加载评论框的资源文件
            popupView = LayoutInflater.from(mContext).inflate(R.layout.comment_popupwindow, null);
        }
        inputComment = (EditText) popupView.findViewById(R.id.et_discuss);
        StringBuffer sb=new StringBuffer();
        sb.append("回复：");
        if(name!=null){
            sb.append(name);
        }
        inputComment.setHint(sb.toString());
        btn_submit = (Button) popupView.findViewById(R.id.btn_confirm);
        rl_input_container = (RelativeLayout) popupView.findViewById(R.id.rl_input_container);
        //利用Timer这个Api设置延迟显示软键盘，这里时间为200毫秒
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mInputManager = (InputMethodManager) latteDelegate.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                mInputManager.showSoftInput(inputComment, 0);
            }

        }, 200);
        if (popupWindow == null) {
            popupWindow = new PopupWindow(popupView, RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT, false);

        }
        //popupWindow的常规设置，设置点击外部事件，背景色
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popupWindow.dismiss();
                }
                return false;
            }
        });

        // 设置弹出窗体需要软键盘，放在setSoftInputMode之前
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        // 再设置模式，和Activity的一样，覆盖，调整大小。
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //设置popupwindow的显示位置，这里应该是显示在底部，即Bottom
        popupWindow.showAtLocation(popupView, Gravity.BOTTOM, 0, 0);
        popupWindow.update();

        //设置监听
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            // 在dismiss中恢复透明度
            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onDismiss() {
                mInputManager.hideSoftInputFromWindow(inputComment.getWindowToken(), 0); //强制隐藏键盘
            }
        });
        //外部点击事件
        rl_input_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputManager.hideSoftInputFromWindow(inputComment.getWindowToken(), 0); //强制隐藏键盘
                popupWindow.dismiss();
            }
        });
        //评论框内的发送按钮设置点击事件
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nInputContentText = inputComment.getText().toString().trim();
                if (nInputContentText == null || "".equals(nInputContentText)) {
                    Toast.makeText(mContext, "请输入评论内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendComment(CIRCLEID, nInputContentText, holder,replyUserId);
            }
        });
    }

    private void initViewText(MultipleViewHolder holder, final int circleId, String userNickname,
                              final String content, String createdTime, String likes, String commentList) {
        //取出所以控件
        final AppCompatTextView tvName = holder.getView(R.id.tv_name);
        final AppCompatTextView tvContent = holder.getView(R.id.tv_content);
        final AppCompatTextView tvTime = holder.getView(R.id.tv_time);
        final AppCompatTextView tvZan = holder.getView(R.id.tv_zan);
        final AppCompatTextView tvShare = holder.getView(R.id.tv_share);
        final LinearLayoutCompat llZan = holder.getView(R.id.ll_zan);
        final AppCompatTextView tvGetzan = holder.getView(R.id.tv_getzan);
        final RecyclerView rvComment = holder.getView(R.id.rv_comment);
        final AppCompatTextView tvComment = holder.getView(R.id.tv_comment);
        // 设置TextView可展示的宽度 ( 父控件宽度 - 左右margin - 左右padding）
//        int whidth = ScreenUtils.getScreenWidth(latteDelegate.getContext()) - ScreenUtils.dip2px(latteDelegate.getContext(), 16 * 2);
//        tvContent.initWidth(313+180);
//        tvContent.setMaxLines(2);
//        tvContent.setMaxLines(10000);
//        tvContent.initWidth(100000);
//        tvContent.setCloseText(content);


//         boolean[] isExpandDescripe = {false};
//        tvContent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isExpandDescripe[0]) {
//                    isExpandDescripe[0] = false;
//                    tvContent.setMaxLines(6);// 收起
//                } else {
//                    isExpandDescripe[0] = true;
//                    tvContent.setMaxLines(Integer.MAX_VALUE);// 展开
//                }
//                textViewSpanUtil.toggleEllipsize(latteDelegate.getContext(),
//                        tvContent, 6,
//                        content,
//                        "展开",
//                        R.color.main_text_blue_57, isExpandDescripe[0]);
//            }
//        });
        getLastIndexForLimit(tvContent,maxLine,content);
        //赋值
        tvName.setText(userNickname);
        //语音
//        if (TextUtils.isEmpty(content)) {
//            tvContent.setVisibility(View.GONE);
//        } else {
//
//            tvContent.setText(content);
//            tvContent.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    readContent(content);
//                }
//
//
//            });
//        }
        DateFormat df = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
        try {
            Date createdTime_d = df.parse(createdTime);
            Log.d("jialei","createdTime_d："+(createdTime_d.getYear()+- 1900)+","+createdTime_d.getMonth()+","+createdTime_d.getDay());
            String time=TimeFormat.getCompareNowString(createdTime_d);
            tvTime.setText(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (TextUtils.isEmpty(likes)) {
            llZan.setVisibility(View.GONE);
        } else {
            llZan.setVisibility(View.VISIBLE);
            tvGetzan.setText(likes);
        }


        final ArrayList<MultipleItemEntity> data =
                new YjIndexcommentDataConverter()
                        .setJsonData(commentList)
                        .convert();
        mCommentAdapter = new YjIndexCommentAdapter(data);
        mCommentAdapter.setmYjIndexCommentListener(new YjIndexCommentListener() {
            @Override
            public void OnItemClick(long replyUserId, String name) {
                showPopupcomment(circleId, content, holder,replyUserId,name);
            }
        });
        final LinearLayoutManager manager = new LinearLayoutManager(mContext);
        rvComment.setLayoutManager(manager);
        rvComment.setAdapter(mCommentAdapter);


        //点赞
        tvZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zan(circleId, v);
            }
        });

        //说一句
        tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupcomment(circleId, content, holder,0,null);
            }
        });
    }

    private void initImages(MultipleViewHolder holder, String[] imgs, String[] bigImgs) {
        final NineGridView ngImgs = holder.getView(R.id.ng_imgs);
        ArrayList<ImageInfo> imageInfo = new ArrayList<>();
        final int imgsSize = imgs.length;
        for (int i = 0; i < imgsSize; i++) {
            ImageInfo info = new ImageInfo();
            info.setThumbnailUrl(imgs[i]);
            info.setBigImageUrl(bigImgs[i]);
            imageInfo.add(info);
        }
        ngImgs.setAdapter(new NineGridViewClickAdapter(mContext, imageInfo));
    }
    private void initLetter(MultipleViewHolder holder,int circleId) {
        final AppCompatTextView canRead = holder.getView(R.id.tv_canread);
        canRead.setOnClickListener(v -> {
            if(mIndexCanReadItemListener!=null){
                mIndexCanReadItemListener.goCanReadList(circleId);
            }
        });
    }


    private void initMedias(MultipleViewHolder holder, String[] medias) {
        final PlayerView playerView = holder.getView(R.id.video_view);
        initializePlayer(playerView, medias);
    }

    private void sendComment(int circleId, String nInputContentText, MultipleViewHolder holder,long replyUserId) {
        final String url = "circle/insert_comment";
        final String token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        RxRestClient.builder()
                .url(url)
                .params("yjtk", token)
                .params("circleId", circleId)
                .params("content", nInputContentText)
                .params("replyUserId", replyUserId)
                .build()
                .post()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(mContext) {
                    @Override
                    public void onResponse(String response) {
                        LatteLogger.json("INSERT_COMMENT", response);
                        final JSONObject object = JSON.parseObject(response);
                        final String status = object.getString("status");

                        if (TextUtils.equals(status, "1001")) {
                            Toast.makeText(mContext, "评论成功", Toast.LENGTH_SHORT).show();
                            if (mIndexItemListener != null) {
                                mIndexItemListener.onIndexItemClick(0);
                            }
                        } else {
                            final String msg = JSON.parseObject(response).getString("status");
                            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                        }
                        mInputManager.hideSoftInputFromWindow(inputComment.getWindowToken(), 0);
                        popupWindow.dismiss();

                    }

                    @Override
                    public void onFail(Throwable e) {
                        Toast.makeText(mContext, "请稍后尝试", Toast.LENGTH_SHORT).show();
                        mInputManager.hideSoftInputFromWindow(inputComment.getWindowToken(), 0);

                        popupWindow.dismiss();
                    }
                });
    }

    private void zan(int circleId, final View v) {
        final String url = "/circle/like/" + circleId;
        final String token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        RxRestClient.builder()
                .url(url)
                .params("yjtk", token)
                .build()
                .post()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(mContext) {
                    @Override
                    public void onResponse(String response) {
                        LatteLogger.json("USER_PROFILE", response);
                        final JSONObject object = JSON.parseObject(response);
                        final String status = object.getString("status");

                        if (TextUtils.equals(status, "1001")) {
                            final JSONObject data = object.getJSONObject("data");
                            final int likeStatus = data.getInteger("likeStatus");
                            final AppCompatTextView tvZan = (AppCompatTextView) v;
                            switch (likeStatus) {
                                case LIKE:
                                    tvZan.setBackgroundResource(R.mipmap.icon_zan_c);
                                    break;
                                case DISLIKE:
                                    tvZan.setBackgroundResource(R.mipmap.icon_zan);
                                    break;
                                default:
                                    tvZan.setBackgroundResource(R.mipmap.icon_zan);
                                    break;
                            }
                        } else {
                            final String msg = JSON.parseObject(response).getString("status");
                            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFail(Throwable e) {
                        Toast.makeText(mContext, "请稍后尝试", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private ExoPlayer player;
    private boolean playWhenReady;
    private int currentWindow;
    private long playbackPosition;

    private void initializePlayer(PlayerView playerView, String[] mediaUrl) {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(mContext,
                    new DefaultRenderersFactory(mContext),
                    new DefaultTrackSelector(), new DefaultLoadControl());

            playerView.setPlayer(player);

            player.setPlayWhenReady(playWhenReady);
            player.seekTo(currentWindow, playbackPosition);
        }
        final int mediaUrlSize = mediaUrl.length;
        LatteLogger.d("jialei", "mediaUrlSize:" + mediaUrlSize);
        MediaSource[] mediaSources = new MediaSource[mediaUrlSize];
        for (int i = 0; i < mediaUrlSize; i++) {
            LatteLogger.d("jialei", "i:" + i);
            Uri uri = Uri.parse(mediaUrl[i]);
            mediaSources[i] = buildMediaSource(uri);

        }
        player.prepare(new ConcatenatingMediaSource(mediaSources), true, true);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    private void initTts() {
        if (null == tts) {
            tts = new Tts(mContext);
        }
    }

    private void readContent(String text) {
        tts.start(text);
    }

    private int maxLine = 6;
    private void getLastIndexForLimit(TextView tv, int maxLine, String content) {
         SpannableString elipseString;//收起的文字
         SpannableString notElipseString;//展开的文字
        //获取TextView的画笔对象
        TextPaint paint = tv.getPaint();
        //每行文本的布局宽度
        int width =latteDelegate.getContext().getResources().getDisplayMetrics().widthPixels - dip2px(latteDelegate.getContext(),47+28);
        //实例化StaticLayout 传入相应参数
        StaticLayout staticLayout = new StaticLayout(content,paint,width, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
        //判断content是行数是否超过最大限制行数3行
        if (staticLayout.getLineCount()>maxLine) {
            //定义展开后的文本内容
            String string1 = content + "    收起";
            notElipseString = new SpannableString(string1);
            //给收起两个字设成蓝色
            notElipseString.setSpan(new ForegroundColorSpan(Color.parseColor("#0079e2")), string1.length() - 2, string1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            //获取到第三行最后一个文字的下标
            int index = staticLayout.getLineStart(maxLine) - 1;
            //定义收起后的文本内容
            String substring = content.substring(0, index - 2) + "..." + "全文";
            elipseString = new SpannableString(substring);
            //给查看全部设成蓝色
            elipseString.setSpan(new ForegroundColorSpan(Color.parseColor("#0079e2")), substring.length() - 4, substring.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //设置收起后的文本内容
            tv.setText(elipseString);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.isSelected()) {
                        //如果是收起的状态
                        tv.setText(notElipseString);
                        tv.setSelected(false);
                    } else {
                        //如果是展开的状态
                        tv.setText(elipseString);
                        tv.setSelected(true);
                    }
                }
            });
            //将textview设成选中状态 true用来表示文本未展示完全的状态,false表示完全展示状态，用于点击时的判断
            tv.setSelected(true);
        } else {
            //没有超过 直接设置文本
            tv.setText(content);
            tv.setOnClickListener(null);
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context mContext, float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
