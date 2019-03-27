package com.bokang.yijia.mobshare.platform.sina;

import com.bokang.yijia.mobshare.entity.ResourcesManager;
import com.bokang.yijia.mobshare.utils.DemoUtils;
import com.mob.MobSDK;

import org.json.JSONException;
import org.json.JSONObject;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;

/**
 * Created by yjin on 2017/6/22.
 */

public class WeiboShare {
	private PlatformActionListener platformActionListener;

	public WeiboShare(PlatformActionListener mListener){
		this.platformActionListener = mListener;
	}

	public void shareText(){
		Platform platform = ShareSDK.getPlatform(SinaWeibo.NAME);
		Platform.ShareParams shareParams = new  Platform.ShareParams();
		shareParams.setText(ResourcesManager.getInstace(MobSDK.getContext()).getText());
		if(!DemoUtils.isValidClientSina("com.sina.weibo")){
			shareParams.setUrl("http://www.mob.com");
		}
		platform.setPlatformActionListener(platformActionListener);
		platform.share(shareParams);
	}

	public void shareImage(){
		Platform platform = ShareSDK.getPlatform(SinaWeibo.NAME);
		Platform.ShareParams shareParams = new  Platform.ShareParams();
		shareParams.setText(ResourcesManager.getInstace(MobSDK.getContext()).getText());
		shareParams.setImageUrl(ResourcesManager.getInstace(MobSDK.getContext()).getImageUrl());
		shareParams.setImagePath(ResourcesManager.getInstace(MobSDK.getContext()).getImagePath());
		shareParams.setImageArray(ResourcesManager.getInstace(MobSDK.getContext()).randomPic());
		if(!DemoUtils.isValidClientSina("com.sina.weibo")){
			shareParams.setUrl("http://www.mob.com");
		}
		shareParams.setShareType(Platform.SHARE_IMAGE);
		shareParams.setLongitude(ResourcesManager.longitude);
		shareParams.setLatitude(ResourcesManager.latitude);
		platform.setPlatformActionListener(platformActionListener);
		platform.share(shareParams);
	}

	public void shareVideo(){
		Platform platform = ShareSDK.getPlatform(SinaWeibo.NAME);
		Platform.ShareParams shareParams = new  Platform.ShareParams();
		shareParams.setFilePath(ResourcesManager.getInstace(MobSDK.getContext()).getFilePath());
		shareParams.setText(ResourcesManager.getInstace(MobSDK.getContext()).getText());
		shareParams.setImageUrl(ResourcesManager.getInstace(MobSDK.getContext()).getImageUrl());
		shareParams.setShareType(Platform.SHARE_VIDEO);
		platform.setPlatformActionListener(platformActionListener);
		platform.share(shareParams);
	}

	public void shareText(PlatformActionListener mListener){
		Platform platform = ShareSDK.getPlatform(SinaWeibo.NAME);
		Platform.ShareParams shareParams = new  Platform.ShareParams();
		shareParams.setText(ResourcesManager.getInstace(MobSDK.getContext()).getText());
		if(!DemoUtils.isValidClientSina("com.sina.weibo")){
			shareParams.setUrl(ResourcesManager.getInstace(MobSDK.getContext()).getUrl());
		}
		platform.setPlatformActionListener(mListener);
		platform.share(shareParams);
	}

	public void shareImage(PlatformActionListener mListener){
		Platform platform = ShareSDK.getPlatform(SinaWeibo.NAME);
		Platform.ShareParams shareParams = new  Platform.ShareParams();
		shareParams.setImageUrl(ResourcesManager.getInstace(MobSDK.getContext()).getImageUrl());
		shareParams.setImagePath(ResourcesManager.getInstace(MobSDK.getContext()).getImagePath());
		if(!DemoUtils.isValidClientSina("com.sina.weibo")){
			shareParams.setUrl(ResourcesManager.getInstace(MobSDK.getContext()).getUrl());
		}
		shareParams.setLongitude(ResourcesManager.longitude);
		shareParams.setLatitude(ResourcesManager.latitude);
		platform.setPlatformActionListener(mListener);
		platform.share(shareParams);
	}

	/**
	 * ����linkcard����
	 **/
	public void shareLinkCard() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("url", "http://thyrsi.com/t6/672/1550816513x2890202977.jpg");
			jsonObject.put("width", 120);
			jsonObject.put("height", 120);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		Platform platform = ShareSDK.getPlatform(SinaWeibo.NAME);
		Platform.ShareParams sp = new Platform.ShareParams();
		sp.setText("MobSDK ֻΪ�����ʵķ���");
		sp.setLcCreateAt("2019-01-24");
		sp.setLcDisplayName("Mob-ȫ�����ȵĵ�����������");
		sp.setLcImage(jsonObject);
		sp.setLcSummary("��ֹ��SDK");
		sp.setLcUrl("http://www.mob.com/");
		sp.setLcObjectType("webpage");
		platform.setPlatformActionListener(platformActionListener);
		platform.share(sp);
	}

}
