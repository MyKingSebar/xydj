package com.example.latte.ui.ninegridview;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.latte.ui.R;
import com.lzy.ninegrid.NineGridView;

public class GlideImageLoader implements NineGridView.ImageLoader {

    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.mipmap.img_error)
            .error(R.mipmap.img_error)
            .centerCrop()
            .dontAnimate();

    @Override
    public void onDisplayImage(Context context, ImageView imageView, String url) {

        Glide.with(context)
                .load(url)
                .apply(OPTIONS)//
                .into(imageView);
    }

    @Override
    public Bitmap getCacheImage(String url) {
        return null;
    }
}
