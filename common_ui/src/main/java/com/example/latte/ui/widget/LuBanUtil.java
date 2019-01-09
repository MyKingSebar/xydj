package com.example.latte.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;
import top.zibin.luban.OnRenameListener;

public class LuBanUtil {

    public static void dealImageLuban(Context context, String sourceFilePath, String targetFilePath, OnCompressListener compressListener) {
        try {
            Luban.with(context)
                    .load(sourceFilePath)                                   // 传人要压缩的图片列表
                    .ignoreBy(100)                                  // 忽略不压缩图片的大小
                    .setTargetDir(targetFilePath)// 设置压缩后文件存储位置
                    .setRenameListener(new OnRenameListener() {
                        @Override
                        public String rename(String filePath) {
                            return targetFilePath;
                        }
                    })
                    .setCompressListener(compressListener).launch();    //启动压缩


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    interface   CustomCompressListener

    {
        void onError(Throwable throwable);
        void onSuccess(List<File> list);
    }
    public static void dealImageLubanList(Context context,List<String> sourceFilePath, String targetFilePath,CustomCompressListener compressListener) {
        try {
            CompositeDisposable mDisposable = new CompositeDisposable();
            mDisposable.add(Flowable.just(sourceFilePath)
                    .observeOn(Schedulers.io())
                    .map(new Function<List<String>, List<File>>() {
                        @Override
                        public List<File> apply(@NonNull List<String> list) throws Exception {
                            return Luban.with(context)
                                    .setTargetDir(targetFilePath)
                                    .load(list)
                                    .get();
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            compressListener.onError(throwable);
                        }
                    })
                    .onErrorResumeNext(Flowable.<List<File>>empty())
                    .subscribe(new Consumer<List<File>>() {
                        @Override
                        public void accept(@NonNull List<File> list) {
//                            for (File file : list) {
//                               // Log.i(TAG, file.getAbsolutePath());
//                                //showResult(originPhotos, file);
//                            }
                            compressListener.onSuccess(list);
                        }
                    }));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
