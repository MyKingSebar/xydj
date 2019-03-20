package com.example.yijia.util;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.latte.R;

public class GlideUtils {
    public static final int USERMODE = 1;
    public static final int DEFAULTMODE = 0;


    public static final RequestOptions USEROPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.default_user_img).error(R.drawable.default_user_img)
            .centerCrop()
            .dontAnimate();
    public static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate();

    public static void load(Context context, String url, ImageView v, int type) {
         RequestOptions option = null;
        switch (type) {
            case 1:
                option = USEROPTIONS;
                break;
            case 0:
                option = OPTIONS;
                break;
            default:
                option = OPTIONS;
                break;

        }
        Glide.with(context)
                .load(url)
                .apply(option)
                .into(v);
    }
}

