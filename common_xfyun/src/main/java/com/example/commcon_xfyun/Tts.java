package com.example.commcon_xfyun;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.commcon_xfyun.setting.TtsSettings;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.sunflower.FlowerCollector;

import static android.content.Context.MODE_PRIVATE;

//语音合成
public class Tts {
    private Context mContext = null;

    public Tts(Context mContext) {
        this.mContext = mContext;
        onCreate();
        initParam();
    }


    // 语音合成对象
    private SpeechSynthesizer mTts;
    // 默认发音人

    private String voicer = "xiaoyan";

    private String[] mCloudVoicersEntries;

    private String[] mCloudVoicersValue;
    String texts = "";

    // 缓冲进度
    private int mPercentForBuffering = 0;
    // 播放进度
    private int mPercentForPlaying = 0;

    // 云端/本地单选按钮
    private RadioGroup mRadioGroup;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    private Toast mToast;
    private SharedPreferences mSharedPreferences;



    public void onCreate() {
        Log.e("jialei","onCreate.mContext==null"+(mContext==null));
        //        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(mContext, mTtsInitListener);

        // 云端发音人名称列表
        mCloudVoicersEntries = mContext.getResources().getStringArray(R.array.voicer_cloud_entries);
        mCloudVoicersValue = mContext.getResources().getStringArray(R.array.voicer_cloud_values);

        mSharedPreferences = mContext.getSharedPreferences(TtsSettings.PREFER_NAME, MODE_PRIVATE);
        mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
    }


    private boolean check() {
        if (null == mTts) {
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            this.showTip("创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化");
            return false;
        }
        return true;
    }

    // 取消合成
    public void stop() {
        mTts.stopSpeaking();
    }

    // 暂停播放
    public void pause() {
        mTts.pauseSpeaking();
    }

    // 继续播放
    public void resume() {
        mTts.resumeSpeaking();
    }
    public boolean isSpeaking() {
        return mTts.isSpeaking();
    }

    // 开始合成
    // 收到onCompleted 回调时，合成结束、生成合成音频
    // 合成的音频格式：只支持pcm格式
    public void start(String text) {
        if(!check()){
            return;
        }
        // 移动数据分析，收集开始合成事件
        FlowerCollector.onEvent(mContext, "tts_play");

        // 设置参数
        setParam();
        int code = mTts.startSpeaking(text, mTtsListener);
//			/**
//			 * 只保存音频不进行播放接口,调用此接口请注释startSpeaking接口
//			 * text:要合成的文本，uri:需要保存的音频全路径，listener:回调接口
//			*/
			/*String path = Environment.getExternalStorageDirectory()+"/tts.pcm";
			int code = mTts.synthesizeToUri(text, path, mTtsListener);*/

        if (code != ErrorCode.SUCCESS) {
            showTip("语音合成失败,错误码: " + code);
        }
    }
    public void start(String text,SynthesizerListener mTtsListener2) {
        if(!check()){
            return;
        }
        // 移动数据分析，收集开始合成事件
        FlowerCollector.onEvent(mContext, "tts_play");

        // 设置参数
        setParam();
        int code = mTts.startSpeaking(text, mTtsListener2);
//			/**
//			 * 只保存音频不进行播放接口,调用此接口请注释startSpeaking接口
//			 * text:要合成的文本，uri:需要保存的音频全路径，listener:回调接口
//			*/
			/*String path = Environment.getExternalStorageDirectory()+"/tts.pcm";
			int code = mTts.synthesizeToUri(text, path, mTtsListener);*/

        if (code != ErrorCode.SUCCESS) {
            showTip("语音合成失败,错误码: " + code);
        }
    }

    private int selectedNum = 0;

    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d("XfYun", "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败,错误码：" + code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            showTip("开始播放");
        }

        @Override
        public void onSpeakPaused() {
            showTip("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            showTip("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
            mPercentForBuffering = percent;


            showTip(String.format(mContext.getString(R.string.tts_toast_format),
                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            mPercentForPlaying = percent;
            showTip(String.format(mContext.getString(R.string.tts_toast_format),
                    mPercentForBuffering, mPercentForPlaying));

//            SpannableStringBuilder style = new SpannableStringBuilder(texts);
//            Log.e("XfYun", "beginPos = " + beginPos + "  endPos = " + endPos);
//            style.setSpan(new BackgroundColorSpan(Color.RED), beginPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            ((EditText) findViewById(R.id.tts_text)).setText(style);
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                showTip("播放完成");
            } else if (error != null) {
                showTip(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}

            if (SpeechEvent.EVENT_TTS_BUFFER == eventType) {
                byte[] buf = obj.getByteArray(SpeechEvent.KEY_EVENT_TTS_BUFFER);
                Log.e("MscSpeechLog", "buf is =" + buf);
            }

        }
    };

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    /**
     * 初始化参数
     */
    private void initParam() {
        //合成引擎设置
        mEngineType = SpeechConstant.TYPE_CLOUD;
        //在线合成发音人
        voicer = "xiaoyan";

    }

    /**
     * 参数设置
     *
     * @return
     */
    private void setParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY, "1");
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, mSharedPreferences.getString("speed_preference", "50"));
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "50"));
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");

        }
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "3"));
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "pcm");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.pcm");
    }


    public void onDestroy() {
        if (null != mTts) {
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }
    }

}
