package com.yijia.common_yijia.main.find.homedoc;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.ec.R;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.yijia.app.Latte;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.ui.TextViewUtils;
import com.example.yijia.util.GlideUtils;
import com.example.yijia.util.callback.CallbackIntegerManager;
import com.example.yijia.util.callback.IGlobalCallback;
import com.google.gson.Gson;
import com.lianghanzhen.LazyViewPager;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.find.homedoc.doctorin_top.DoctorInTitieAdapter;
import com.yijia.common_yijia.main.find.homedoc.doctorin_top.DoctorInTitleDataConverter;
import com.yijia.common_yijia.main.find.homedoc.doctorin_top.DoctorInTitleItemListener;
import com.yijia.common_yijia.main.find.homedoc.doctorin_top.DoctorInTitleType;
import com.yijia.common_yijia.main.message.trtc2.PersonalChatFragmentLittle;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeDoctorInUseRecycDelegate extends LatteDelegate {
    public static final String ID_KEY = "id";
    private long doctorId = 0;

    String token = null;

    AppCompatTextView tvTitle = null;
    AppCompatTextView tvSave = null;
    RelativeLayout tvBack = null;
    RecyclerView mTopRecyelerView = null;
    RecyclerView mRecyelerView = null;
    FrameLayout frameLayout = null;
    //    LazyViewPager mViewPager = null;
//    MagicIndicator magicIndicator = null;
    DoctorInUseRecycAdapter mHomeDoctorInTabPagerAdapter = null;

    ConstraintLayout mClose = null;
    AppCompatTextView mCloseIcon = null;
    public static boolean closeStatue = false;

    DoctorInTitieAdapter mDoctorInTitieAdapter = null;
    DoctorInTitleItemListener mDoctorInTitleItemListener = null;

    PersonalChatFragmentLittle mDelegate = null;


    public static HomeDoctorInUseRecycDelegate create(String id) {
        final Bundle args = new Bundle();
        args.putString(ID_KEY, id);
        final HomeDoctorInUseRecycDelegate delegate = new HomeDoctorInUseRecycDelegate();
        delegate.setArguments(args);
        return delegate;
    }


    @Override
    public Object setLayout() {
        return R.layout.delegate_homedoctor_in_use_recyc;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        assert args != null;
        doctorId = Long.parseLong(Objects.requireNonNull(args.getString(ID_KEY)));
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        initVIew(rootView);

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (doctorId != 0) {
            getHomedoc(doctorId);
        } else {
            showToast("网络异常");
            getSupportDelegate().pop();
        }
    }

    private void initVIew(View rootView) {
        mRecyelerView = rootView.findViewById(R.id.tablayout);
        frameLayout = rootView.findViewById(R.id.view_pager);
        tvTitle = rootView.findViewById(R.id.tv_title);
        tvSave = rootView.findViewById(R.id.tv_save);
        tvBack = rootView.findViewById(R.id.tv_back);
        tvTitle.setText("家庭医生");
        tvSave.setVisibility(View.INVISIBLE);
        tvBack.setOnClickListener(v -> getSupportDelegate().pop());
        mTopRecyelerView = rootView.findViewById(R.id.rv_top);
        mDoctorInTitieAdapter = new DoctorInTitieAdapter(initTitleBottomData());
        mDoctorInTitieAdapter.setCartItemListener(initDoctorInTitleItemListener());
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        //调整RecyclerView的排列方向
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mTopRecyelerView.setLayoutManager(manager);
        mTopRecyelerView.setAdapter(mDoctorInTitieAdapter);
        mClose = rootView.findViewById(R.id.cl_close);
        mCloseIcon = rootView.findViewById(R.id.tv_close);

        mClose.setOnClickListener(v -> {
            if (closeStatue) {
                closeStatue = false;
                mRecyelerView.setVisibility(View.VISIBLE);
                TextViewUtils.setBackground(getContext(), mCloseIcon, R.mipmap.checkbox_docin_title_top);
            } else {
                closeStatue = true;
                mRecyelerView.setVisibility(View.GONE);
                TextViewUtils.setBackground(getContext(), mCloseIcon, R.mipmap.checkbox_docin_title_bottom);
            }
            for (Map.Entry<Integer, Integer> entry : HomeDoctorInTabPagerAdapter3.CALLBACKS.entrySet()) {
                final IGlobalCallback<String> callback;
                callback = CallbackIntegerManager
                        .getInstance()
                        .getCallback(entry.getValue());
                if (callback != null) {
                    callback.executeCallback("");
                }
            }
        });
    }

    private ArrayList<MultipleItemEntity> initTitleBottomData() {
        final ArrayList<MultipleItemEntity> data =
                new DoctorInTitleDataConverter()
                        .convert();
        return data;
    }

    private DoctorInTitleItemListener initDoctorInTitleItemListener() {
        mDoctorInTitleItemListener = (type, name) -> {
            Log.d("jialei", "type:" + type);
            if (type == null) {
                return;
            }
            if (TextUtils.equals(type, DoctorInTitleType.APPOINTMENTDOCTOR.name())) {
                showToast("预约就诊");
            } else if (TextUtils.equals(type, DoctorInTitleType.APPOINTMENTNURSE.name())) {
                showToast("预约护理");
            } else if (TextUtils.equals(type, DoctorInTitleType.APPOINTMENTPHYSIOTHERAPY.name())) {
                showToast("预约理疗");
            }
        };
        return mDoctorInTitleItemListener;
    }

    public void getHomedoc(long doctTeamId) {
        String url = "doct/query_doct_team_member";
        RxRestClient.builder()
                .url(url)
                .params("yjtk", token)
                .params("doctTeamId", doctTeamId)
                .build()
                .get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(Latte.getApplicationContext()) {
                    @Override
                    public void onResponse(String response) {
                        final JSONObject object = JSON.parseObject(response);


                        final String status = object.getString("status");
                        Log.e("jialei", "query_doct_team_member" + new Gson().toJson(response));
                        if (TextUtils.equals(status, "1001")) {
                            List<MultipleItemEntity> data = new HomeDoctorInUseRecycDataConverter()
                                    .setJsonData(response)
                                    .convert();
                            mHomeDoctorInTabPagerAdapter = new DoctorInUseRecycAdapter(data);
                            mHomeDoctorInTabPagerAdapter.setmCommonStringClickListener(info -> {
                                if (TextUtils.isEmpty(info)) {
                                    return;
                                }
                                String[] s = info.split(",");
                                if (s.length != 5) {
                                    return;
                                }
                                PersonalChatFragmentLittle delegate = PersonalChatFragmentLittle.create(s[0], s[1], s[2], s[3], Integer.parseInt(s[4]));
//                                mDelegate=(PersonalChatFragmentLittle) mDelegate.change(delegate);
                                getSupportDelegate().loadRootFragment(R.id.view_pager,delegate);
                            });
                            if (data.size() > 0) {
                                PersonalChatFragmentLittle mRoot = PersonalChatFragmentLittle.create(data.get(0).getField(HomeDoctorInMultipleFields.TENCENTIMID), data.get(0).getField(HomeDoctorInMultipleFields.DOCTNAME), data.get(0).getField(HomeDoctorInMultipleFields.DOCTHEADIMAGE), data.get(0).getField(HomeDoctorInMultipleFields.MAJOR), 0);
//                                if (mDelegate == null) {
                                    mDelegate = mRoot;
                                    getSupportDelegate().loadRootFragment(R.id.view_pager, mDelegate);
//                                } else {
//                                    mDelegate=(PersonalChatFragmentLittle) mDelegate.change(mRoot);
//                                }
                            }
                            final LinearLayoutManager manager = new LinearLayoutManager(getContext());
                            //调整RecyclerView的排列方向
                            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                            mRecyelerView.setLayoutManager(manager);
                            mRecyelerView.setAdapter(mHomeDoctorInTabPagerAdapter);

                        } else {
                            final String msg = JSON.parseObject(response).getString("msg");
                            Toast.makeText(Latte.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        Toast.makeText(Latte.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
