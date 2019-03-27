package com.bokang.yijia.mobshare.platform.foursquare;

import com.bokang.yijia.mobshare.entity.ResourcesManager;
import com.mob.MobSDK;

import cn.sharesdk.foursquare.FourSquare;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by yjin on 2017/6/22.
 */

public class FourSquareShare {
	private PlatformActionListener platformActionListener;

	public FourSquareShare(PlatformActionListener mListener) {
		this.platformActionListener = mListener;
	}

	public void shareImage() {
		Platform platform = ShareSDK.getPlatform(FourSquare.NAME);
		Platform.ShareParams shareParams = new Platform.ShareParams();
		shareParams.setImagePath(ResourcesManager.getInstace(MobSDK.getContext()).getImagePath());
		shareParams.setImageUrl(ResourcesManager.getInstace(MobSDK.getContext()).getImageUrl());
		shareParams.setLatitude(ResourcesManager.latitude);
		shareParams.setLongitude(ResourcesManager.longitude);
		shareParams.setVenueName(ResourcesManager.VENUE_NAME);
		platform.setPlatformActionListener(platformActionListener);
		platform.share(shareParams);
	}

	public void addChecked() {
		Platform platform = ShareSDK.getPlatform(FourSquare.NAME);
		Platform.ShareParams shareParams = new Platform.ShareParams();
		shareParams.setText(ResourcesManager.getInstace(MobSDK.getContext()).getText());
		shareParams.setLatitude(ResourcesManager.latitude);
		shareParams.setLongitude(ResourcesManager.longitude);
		platform.setPlatformActionListener(platformActionListener);
		platform.share(shareParams);
	}

	public void uploadLocation() {
		Platform platform = ShareSDK.getPlatform(FourSquare.NAME);
		Platform.ShareParams shareParams = new Platform.ShareParams();
		shareParams.setText(ResourcesManager.getInstace(MobSDK.getContext()).getText());
		shareParams.setLatitude(ResourcesManager.latitude);
		shareParams.setLongitude(ResourcesManager.longitude);
		shareParams.setVenueName(ResourcesManager.VENUE_NAME);
		shareParams.setVenueDescription(ResourcesManager.VENUE_DESCRIPTION);
		platform.setPlatformActionListener(platformActionListener);
		platform.share(shareParams);
	}

	public void shareImage(PlatformActionListener mListener) {
		Platform platform = ShareSDK.getPlatform(FourSquare.NAME);
		Platform.ShareParams shareParams = new Platform.ShareParams();
		shareParams.setImagePath(ResourcesManager.getInstace(MobSDK.getContext()).getImagePath());
		shareParams.setImageUrl(ResourcesManager.getInstace(MobSDK.getContext()).getImageUrl());
		shareParams.setLatitude(ResourcesManager.latitude);
		shareParams.setLongitude(ResourcesManager.longitude);
		shareParams.setVenueName(ResourcesManager.VENUE_NAME);
		platform.setPlatformActionListener(mListener);
		platform.share(shareParams);
	}

	public void addChecked(PlatformActionListener mListener) {
		Platform platform = ShareSDK.getPlatform(FourSquare.NAME);
		Platform.ShareParams shareParams = new Platform.ShareParams();
		shareParams.setText(ResourcesManager.getInstace(MobSDK.getContext()).getText());
		shareParams.setLatitude(ResourcesManager.latitude);
		shareParams.setLongitude(ResourcesManager.longitude);
		platform.setPlatformActionListener(mListener);
		platform.share(shareParams);
	}

	public void uploadLocation(PlatformActionListener mListener) {
		Platform platform = ShareSDK.getPlatform(FourSquare.NAME);
		Platform.ShareParams shareParams = new Platform.ShareParams();
		shareParams.setText(ResourcesManager.getInstace(MobSDK.getContext()).getText());
		shareParams.setLatitude(ResourcesManager.latitude);
		shareParams.setLongitude(ResourcesManager.longitude);
		shareParams.setVenueName(ResourcesManager.VENUE_NAME);
		shareParams.setVenueDescription(ResourcesManager.VENUE_DESCRIPTION);
		platform.setPlatformActionListener(mListener);
		platform.share(shareParams);
	}
}
