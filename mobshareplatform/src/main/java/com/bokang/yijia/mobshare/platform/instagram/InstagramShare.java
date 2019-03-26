package com.bokang.yijia.mobshare.platform.instagram;

import com.bokang.yijia.mobshare.entity.ResourcesManager;
import com.bokang.yijia.mobshare.utils.DemoUtils;
import com.mob.MobSDK;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.instagram.Instagram;

/**
 * Created by yjin on 2017/6/22.
 */

public class InstagramShare {
	private PlatformActionListener platformActionListener;

	public InstagramShare(PlatformActionListener mListener){
		this.platformActionListener = mListener;
		DemoUtils.isValidClient("com.instagram.android");
	}

	public void shareText() {
		Platform qqq = ShareSDK.getPlatform(Instagram.NAME);
		Platform.ShareParams sp = new Platform.ShareParams();
		sp.setTitle("title");
		sp.setText("text");
		sp.setShareType(Platform.INSTAGRAM_FRIEND);
		qqq.setPlatformActionListener(null);
		qqq.share(sp);
	}

	public void shareTextImage(){
		Platform platform = ShareSDK.getPlatform(Instagram.NAME);
		Platform.ShareParams shareParams = new  Platform.ShareParams();
		shareParams.setText(ResourcesManager.getInstace(MobSDK.getContext()).getText());
		shareParams.setImagePath(ResourcesManager.getInstace(MobSDK.getContext()).getImagePath());
		shareParams.setImageUrl(ResourcesManager.getInstace(MobSDK.getContext()).getImageUrl());
		platform.setPlatformActionListener(platformActionListener);
		platform.share(shareParams);
	}

	public void shareVideo(){
		Platform platform = ShareSDK.getPlatform(Instagram.NAME);
		Platform.ShareParams shareParams = new  Platform.ShareParams();
		shareParams.setText(ResourcesManager.getInstace(MobSDK.getContext()).getText());
		shareParams.setFilePath(ResourcesManager.getInstace(MobSDK.getContext()).getFilePath());
		shareParams.setShareType(Platform.SHARE_VIDEO);
		platform.setPlatformActionListener(platformActionListener);
		platform.share(shareParams);
	}
}
