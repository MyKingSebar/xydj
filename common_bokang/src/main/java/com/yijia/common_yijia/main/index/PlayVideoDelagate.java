package com.yijia.common_yijia.main.index;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.latte.ec.R;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.util.log.LatteLogger;
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
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.index.friendcircle.LetterPeopleAdapter;
import com.yijia.common_yijia.main.index.friendcircle.LetterPeopleDataConverter;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class PlayVideoDelagate extends LatteDelegate {
    RecyclerView rv = null;
    int circleId=0;
    private ExoPlayer player;
    private boolean playWhenReady=true;
    private int currentWindow;
    private long playbackPosition;
    String[] medias=null;
    PlayerView pv=null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_play_video;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        init(rootView);
    }


    private void init(View view) {
//        RelativeLayout rl = view.findViewById(R.id.tv_back);
//        rl.setOnClickListener(v -> getSupportDelegate().pop());
//        AppCompatTextView tv = view.findViewById(R.id.tv_title);
//        tv.setText("");
//        AppCompatTextView tv2 = view.findViewById(R.id.tv_save);
//        tv2.setVisibility(View.INVISIBLE);
        pv=view.findViewById(R.id.video_view);
        initializePlayer(pv,medias);
        pv.setUseController(false);
        pv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                getSupportDelegate().pop();
                return false;
            }
        });

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args=getArguments();
        if(args==null){
            return;
        }
        medias=args.getStringArray("videos");
    }


    private void initializePlayer(PlayerView playerView, String[] mediaUrl) {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(getContext(),
                    new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(), new DefaultLoadControl());

            playerView.setPlayer(player);

            player.setPlayWhenReady(playWhenReady);
            player.seekTo(currentWindow, playbackPosition);

        }
        final int mediaUrlSize = mediaUrl.length;
        MediaSource[] mediaSources = new MediaSource[mediaUrlSize];
        for (int i = 0; i < mediaUrlSize; i++) {
            Uri uri = Uri.parse(mediaUrl[i]);
            LatteLogger.d("jialei", "uri:" + uri
            );
            mediaSources[i] = buildMediaSource(uri);

        }
        player.prepare(new ConcatenatingMediaSource(mediaSources), true, true);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }
}
