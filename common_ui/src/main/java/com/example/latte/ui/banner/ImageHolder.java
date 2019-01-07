package com.example.latte.ui.banner;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.latte.ui.R;

public class ImageHolder extends Holder<String> {

    private Context context;
    private AppCompatImageView mImageView;
    private static final RequestOptions BANNER_OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontAnimate()
            .centerCrop();


    public ImageHolder(View itemView) {
        super(itemView);
    }


    @Override
    protected void initView(View itemView) {
        context=itemView.getContext();
        mImageView=itemView.findViewById(R.id.banner_recycler_item);
    }


    @Override
    public void updateUI(String data) {
        Glide.with(context)
                .load(data)
                .apply(BANNER_OPTIONS)
                .into(mImageView);
    }

}
