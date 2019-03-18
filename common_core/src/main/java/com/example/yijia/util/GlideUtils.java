package com.example.yijia.util;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.latte.R;

public class GlideUtils {
    public static final RequestOptions USEROPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.default_user_img).error(R.drawable.default_user_img)
            .centerCrop()
            .dontAnimate();
    public static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate();
}


