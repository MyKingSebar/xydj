package com.example.latte.ui.image;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

@GlideModule
public class LatteGlideModel extends AppGlideModule {

        @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
