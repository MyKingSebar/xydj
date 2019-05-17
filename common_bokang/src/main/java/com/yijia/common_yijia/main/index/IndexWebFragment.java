package com.yijia.common_yijia.main.index;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.ContentFrameLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.latte.ec.R;
import com.example.yijia.app.ConfigKeys;
import com.example.yijia.app.Latte;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.delegates.web.WebDelegateImpl;
import com.example.yijia.delegates.web.client.WebViewClientImpl;
import com.example.yijia.ui.TextViewUtils;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.robot.setting.remind.RobotRemindSettingAddWeekDelegate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JiaLei
 * Created on 2019/5/17 10:26
 * ━━━━━━神兽出没━━━━━━
 * 　　┏┓　　　┏┓+ +
 * 　┏┛┻━━━┛┻┓ + +
 * 　┃　　　　　　　┃
 * 　┃　　　━　　　┃ ++ + + +
 * ████━████ ┃+
 * 　┃　　　　　　　┃ +
 * 　┃　　　┻　　　┃
 * 　┃　　　　　　　┃ + +
 * 　┗━┓　　　┏━┛
 * 　　　┃　　　┃
 * 　　　┃　　　┃ + + + +
 * 　　　┃　　　┃
 * 　　　┃　　　┃ +  神兽保佑
 * 　　　┃　　　┃    代码无bug
 * 　　　┃　　　┃　　+
 * 　　　┃　 　　┗━━━┓ + +
 * 　　　┃ 　　　　　　　┣┓
 * 　　　┃ 　　　　　　　┏┛
 * 　　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　　　┃┫┫　┃┫┫
 * 　　　　┗┻┛　┗┻┛+ + + +
 * ━━━━━━感觉萌萌哒━━━━━━
 */
public class IndexWebFragment extends LatteDelegate {
    public static final String NAME = "name";
    public static final String URL = "url";
    public static final String TYPE = "type";
    public static final String USERID = "userId";
    public static final int ZCJL_TYPE = 1;
    public static final int CTQM_TYPE = 2;
    public static final int KHJL_TYPE = 3;
    public static final int TXJL_TYPE = 4;

    private final String ZCJL = "自测记录";
    private final String CTQM = "常听曲目";
    private final String KHJL = "看护记录";
    private final String TXJL = "提醒记录";

    private final String ZCJL_URL = "SelfRecord";
    private final String CTQM_URL = "RegularSongs";
    private final String KHJL_URL = "NurseRecords";
    private final String TXJL_URL = "RemindRecord";

    Map<String, String> map = new HashMap<>();


    RelativeLayout rl;
    String name;
    String url;
    int type = 0;
    long userId = 0;
    String token=null;


    @Override
    public Object setLayout() {
        return R.layout.delegate_index_web;
    }

    public static IndexWebFragment create(long userId, int type) {
        final Bundle args = new Bundle();
        args.putInt(TYPE, type);
        args.putLong(USERID, userId);
        final IndexWebFragment delegate = new IndexWebFragment();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        assert args != null;
        type = args.getInt(TYPE);
        userId = args.getLong(USERID);

    }


    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initView(rootView);
    }

    private void initView(View rootView) {
        rl = rootView.findViewById(R.id.tv_back);
        rl.setOnClickListener(v -> getSupportDelegate().pop());
        AppCompatTextView tvTitle = rootView.findViewById(R.id.tv_title);
        AppCompatTextView tvSave = rootView.findViewById(R.id.tv_save);
        tvSave.setVisibility(View.GONE);
        map.put(ZCJL, ZCJL_URL);
        map.put(CTQM, CTQM_URL);
        map.put(KHJL, KHJL_URL);
        map.put(TXJL, TXJL_URL);
        switch (type) {
            case ZCJL_TYPE:
                name = ZCJL;
                break;
            case CTQM_TYPE:
                name = CTQM;
                break;
            case KHJL_TYPE:
                name = KHJL;
                break;
            case TXJL_TYPE:
                name = TXJL;
                break;
            default:
        }
        if(map.containsKey(name)){
            url=map.get(name);
        }
        TextViewUtils.AppCompatTextViewSetText(tvTitle, name);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (!TextUtils.isEmpty(url)) {
            //http://192.168.1.123:8880/SelfRecord?userId=245746017008488448&yjtk=d930b03734974878b8e25c44f2f4d904
            token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
//            final WebDelegateImpl fragment = WebDelegateImpl.create("http://www.baidu.com");
            final WebDelegateImpl fragment = WebDelegateImpl.create(Latte.getConfiguration(ConfigKeys.WEB_HOST)+url+"?userId="+userId+"&yjtk="+token);
            fragment.setTopDelegate(this.getParentDelegate());
            getSupportDelegate().loadRootFragment(R.id.web_index_container, fragment);
        }
    }
}
