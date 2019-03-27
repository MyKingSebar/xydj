package com.bokang.yijia.mobshare.platform.dingding.message;

import com.bokang.yijia.mobshare.entity.ResourcesManager;
import com.bokang.yijia.mobshare.utils.DemoUtils;
import com.mob.MobSDK;

import cn.sharesdk.dingding.friends.Dingding;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by yjin on 2017/6/22.
 */

public class DingdingMessageShare {
	private PlatformActionListener platformActionListener;

	public DingdingMessageShare(PlatformActionListener mListener){
		this.platformActionListener = mListener;
		DemoUtils.isValidClient("com.alibaba.android.rimet");
	}

	public void shareText(){
		Platform platform = ShareSDK.getPlatform(Dingding.NAME);
		Platform.ShareParams shareParams = new  Platform.ShareParams();
		shareParams.setTitle(ResourcesManager.getInstace(MobSDK.getContext()).getTitle());
		shareParams.setText(ResourcesManager.getInstace(MobSDK.getContext()).getText());
		shareParams.setShareType(Platform.SHARE_TEXT);
		shareParams.setScence(1);
		platform.setPlatformActionListener(platformActionListener);
		platform.share(shareParams);
	}

	public void shareImage(){
		Platform platform = ShareSDK.getPlatform(Dingding.NAME);
		Platform.ShareParams shareParams = new  Platform.ShareParams();
		shareParams.setTitle(ResourcesManager.getInstace(MobSDK.getContext()).getTitle());
		shareParams.setText(ResourcesManager.getInstace(MobSDK.getContext()).getText());
		shareParams.setImagePath(ResourcesManager.getInstace(MobSDK.getContext()).getImagePath());
		shareParams.setImageUrl(ResourcesManager.getInstace(MobSDK.getContext()).getImageUrl());
		shareParams.setImageData(ResourcesManager.getInstace(MobSDK.getContext()).getImageBmp());
		shareParams.setShareType(Platform.SHARE_IMAGE);
		shareParams.setScence(1);
		platform.setPlatformActionListener(platformActionListener);
		platform.share(shareParams);
	}
	public void shareMusic(){
		Platform platform = ShareSDK.getPlatform(Dingding.NAME);
		Platform.ShareParams shareParams = new  Platform.ShareParams();
		shareParams.setTitle(ResourcesManager.getInstace(MobSDK.getContext()).getTitle());
		shareParams.setText(ResourcesManager.getInstace(MobSDK.getContext()).getText());
		shareParams.setImagePath(ResourcesManager.getInstace(MobSDK.getContext()).getImagePath());
		shareParams.setImageUrl(ResourcesManager.getInstace(MobSDK.getContext()).getImageUrl());
		shareParams.setImageData(ResourcesManager.getInstace(MobSDK.getContext()).getImageBmp());
		shareParams.setMusicUrl(ResourcesManager.getInstace(MobSDK.getContext()).getMusicUrl());
		shareParams.setUrl(ResourcesManager.getInstace(MobSDK.getContext()).getUrl());
		shareParams.setShareType(Platform.SHARE_MUSIC);
		shareParams.setScence(1);
		platform.setPlatformActionListener(platformActionListener);
		platform.share(shareParams);
	}

	public void shareVideo(){
		Platform platform = ShareSDK.getPlatform(Dingding.NAME);
		Platform.ShareParams shareParams = new  Platform.ShareParams();
		shareParams.setTitle(ResourcesManager.getInstace(MobSDK.getContext()).getTitle());
		shareParams.setText(ResourcesManager.getInstace(MobSDK.getContext()).getText());
		shareParams.setImagePath(ResourcesManager.getInstace(MobSDK.getContext()).getImagePath());
		shareParams.setImageUrl(ResourcesManager.getInstace(MobSDK.getContext()).getImageUrl());
		shareParams.setImageData(ResourcesManager.getInstace(MobSDK.getContext()).getImageBmp());
		shareParams.setUrl(ResourcesManager.getInstace(MobSDK.getContext()).getUrl());
		shareParams.setShareType(Platform.SHARE_VIDEO);
		shareParams.setScence(1);
		platform.setPlatformActionListener(platformActionListener);
		platform.share(shareParams);
	}

	public void shareWebPage(){
		Platform platform = ShareSDK.getPlatform(Dingding.NAME);
		Platform.ShareParams shareParams = new  Platform.ShareParams();
		shareParams.setText(ResourcesManager.getInstace(MobSDK.getContext()).getText());
		shareParams.setTitle(ResourcesManager.getInstace(MobSDK.getContext()).getTitle());
		shareParams.setUrl(ResourcesManager.getInstace(MobSDK.getContext()).getUrl());
		shareParams.setShareType(Platform.SHARE_WEBPAGE);
		shareParams.setScence(1);
		platform.setPlatformActionListener(platformActionListener);
		platform.share(shareParams);
	}

	public void shareZFB(){
		Platform platform = ShareSDK.getPlatform(Dingding.NAME);
		Platform.ShareParams shareParams = new  Platform.ShareParams();
		shareParams.setText(ResourcesManager.getInstace(MobSDK.getContext()).getText());
		shareParams.setTitle(ResourcesManager.getInstace(MobSDK.getContext()).getTitle());
		shareParams.setUrl(ResourcesManager.getInstace(MobSDK.getContext()).getUrl());
		shareParams.setShareType(Platform.SHARE_ZHIFUBAO);
		shareParams.setScence(1);
		platform.setPlatformActionListener(platformActionListener);
		platform.share(shareParams);
	}

	public void shareText(PlatformActionListener mListener){
		Platform platform = ShareSDK.getPlatform(Dingding.NAME);
		Platform.ShareParams shareParams = new  Platform.ShareParams();
		shareParams.setTitle(ResourcesManager.getInstace(MobSDK.getContext()).getTitle());
		shareParams.setText(ResourcesManager.getInstace(MobSDK.getContext()).getText());
		shareParams.setShareType(Platform.SHARE_TEXT);
		platform.setPlatformActionListener(mListener);
		shareParams.setScence(1);
		platform.share(shareParams);
	}

	public void shareImage(PlatformActionListener mListener){
		Platform platform = ShareSDK.getPlatform(Dingding.NAME);
		Platform.ShareParams shareParams = new  Platform.ShareParams();
		shareParams.setTitle(ResourcesManager.getInstace(MobSDK.getContext()).getTitle());
		shareParams.setText(ResourcesManager.getInstace(MobSDK.getContext()).getText());
		shareParams.setImagePath(ResourcesManager.getInstace(MobSDK.getContext()).getImagePath());
		shareParams.setImageUrl(ResourcesManager.getInstace(MobSDK.getContext()).getImageUrl());
		shareParams.setImageData(ResourcesManager.getInstace(MobSDK.getContext()).getImageBmp());
		shareParams.setShareType(Platform.SHARE_IMAGE);
		shareParams.setScence(1);
		platform.setPlatformActionListener(mListener);
		platform.share(shareParams);
	}
	public void shareMusic(PlatformActionListener mListener){
		Platform platform = ShareSDK.getPlatform(Dingding.NAME);
		Platform.ShareParams shareParams = new  Platform.ShareParams();
		shareParams.setTitle(ResourcesManager.getInstace(MobSDK.getContext()).getTitle());
		shareParams.setText(ResourcesManager.getInstace(MobSDK.getContext()).getText());
		shareParams.setImagePath(ResourcesManager.getInstace(MobSDK.getContext()).getImagePath());
		shareParams.setImageUrl(ResourcesManager.getInstace(MobSDK.getContext()).getImageUrl());
		shareParams.setImageData(ResourcesManager.getInstace(MobSDK.getContext()).getImageBmp());
		shareParams.setMusicUrl(ResourcesManager.getInstace(MobSDK.getContext()).getMusicUrl());
		shareParams.setUrl(ResourcesManager.getInstace(MobSDK.getContext()).getUrl());
		shareParams.setShareType(Platform.SHARE_MUSIC);
		shareParams.setScence(1);
		platform.setPlatformActionListener(mListener);
		platform.share(shareParams);
	}

	public void shareVideo(PlatformActionListener mListener){
		Platform platform = ShareSDK.getPlatform(Dingding.NAME);
		Platform.ShareParams shareParams = new  Platform.ShareParams();
		shareParams.setTitle(ResourcesManager.getInstace(MobSDK.getContext()).getTitle());
		shareParams.setText(ResourcesManager.getInstace(MobSDK.getContext()).getText());
		shareParams.setImagePath(ResourcesManager.getInstace(MobSDK.getContext()).getImagePath());
		shareParams.setImageUrl(ResourcesManager.getInstace(MobSDK.getContext()).getImageUrl());
		shareParams.setImageData(ResourcesManager.getInstace(MobSDK.getContext()).getImageBmp());
		shareParams.setUrl(ResourcesManager.getInstace(MobSDK.getContext()).getUrl());
		shareParams.setShareType(Platform.SHARE_VIDEO);
		shareParams.setScence(1);
		platform.setPlatformActionListener(mListener);
		platform.share(shareParams);
	}

	public void shareWebPage(PlatformActionListener mListener){
		Platform platform = ShareSDK.getPlatform(Dingding.NAME);
		Platform.ShareParams shareParams = new  Platform.ShareParams();
		shareParams.setText(ResourcesManager.getInstace(MobSDK.getContext()).getText());
		shareParams.setTitle(ResourcesManager.getInstace(MobSDK.getContext()).getTitle());
		shareParams.setUrl(ResourcesManager.getInstace(MobSDK.getContext()).getUrl());
		shareParams.setShareType(Platform.SHARE_WEBPAGE);
		platform.setPlatformActionListener(mListener);
		platform.share(shareParams);
	}

	public void shareZFB(PlatformActionListener mListener){
		Platform platform = ShareSDK.getPlatform(Dingding.NAME);
		Platform.ShareParams shareParams = new  Platform.ShareParams();
		shareParams.setText(ResourcesManager.getInstace(MobSDK.getContext()).getText());
		shareParams.setTitle(ResourcesManager.getInstace(MobSDK.getContext()).getTitle());
		shareParams.setUrl(ResourcesManager.getInstace(MobSDK.getContext()).getUrl());
		shareParams.setShareType(Platform.SHARE_ZHIFUBAO);
		shareParams.setScence(1);
		platform.setPlatformActionListener(mListener);
		platform.share(shareParams);
	}
}
