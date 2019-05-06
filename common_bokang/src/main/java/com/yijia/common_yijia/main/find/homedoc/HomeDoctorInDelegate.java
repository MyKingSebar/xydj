package com.yijia.common_yijia.main.find.homedoc;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.yijia.app.Latte;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.ui.TextViewUtils;
import com.example.yijia.util.GlideUtils;
import com.google.gson.Gson;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.friends.CommonClickListener;
import com.yijia.common_yijia.main.message.trtc2.PersonalChatFragmentLittle;
import com.yijia.common_yijia.main.robot.robotmain.RobotMainTabPagerAdapter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeDoctorInDelegate extends LatteDelegate {
    public static final String ID_KEY = "id";
    private long doctorId = 0;

    String token = null;

    AppCompatTextView tvTitle = null;
    AppCompatTextView tvSave = null;
    RelativeLayout tvBack = null;
    RecyclerView mRecyelerView = null;
    ViewPager mViewPager = null;
    MagicIndicator magicIndicator=null;
    HomeDoctorInTabPagerAdapter3 mHomeDoctorInTabPagerAdapter=null;


    public static HomeDoctorInDelegate create(String id) {
        final Bundle args = new Bundle();
        args.putString(ID_KEY,id);
        final HomeDoctorInDelegate delegate = new HomeDoctorInDelegate();
        delegate.setArguments(args);
        return delegate;
    }


    @Override
    public Object setLayout() {
        return R.layout.delegate_homedoctor_in;
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
        magicIndicator = rootView.findViewById(R.id.tablayout);
        mViewPager = rootView.findViewById(R.id.view_pager);
        tvTitle = rootView.findViewById(R.id.tv_title);
        tvSave = rootView.findViewById(R.id.tv_save);
        tvBack = rootView.findViewById(R.id.tv_back);
        tvTitle.setText("家庭医生");
        tvSave.setVisibility(View.INVISIBLE);
        tvBack.setOnClickListener(v -> getSupportDelegate().pop());

    }

    public void getHomedoc(long doctTeamId) {
        String url = "/doct/query_doct_team_member";
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
                            List<MultipleItemEntity> data = new HomeDoctorInDataConverter()
                                    .setJsonData(response)
                                    .convert();
                            mHomeDoctorInTabPagerAdapter=new HomeDoctorInTabPagerAdapter3(getChildFragmentManager(),data);
                            mViewPager.setAdapter(mHomeDoctorInTabPagerAdapter);
                            initMagicIndicator1(data);





//                            mAdapter = new HomeDoctorAdapter(data);
//                            mAdapter.setCommonClickListener(v -> {
//                                //TODO
//                            });
////                            mAdapter.setOnLoadMoreListener(HomeDoctorDelegate.this, mRecyelerView);
//                            final LinearLayoutManager manager = new LinearLayoutManager(Latte.getApplicationContext());
//                            mRecyelerView.setLayoutManager(manager);
//                            mRecyelerView.setAdapter(mAdapter);
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

//    private void initTabLayout() {
//        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
//        mTabLayout.setSelectedTabIndicatorColor
//                (ContextCompat.getColor(getContext(), R.color.app_text_orange));
//        mTabLayout.setTabTextColors(ColorStateList.valueOf(Color.BLACK));
//        mTabLayout.setBackgroundColor(Color.WHITE);
//        mTabLayout.setupWithViewPager(mViewPager);
//    }

//    private void initPager() {
//        final PagerAdapter adapter = new RobotMainTabPagerAdapter(getFragmentManager());
//        mViewPager.setAdapter(adapter);
//    }

    private void initMagicIndicator1(List<MultipleItemEntity> data) {
        magicIndicator.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(getContext());
//        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return data.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(context);
                // load custom layout
                View customLayout = LayoutInflater.from(context).inflate(R.layout.item_tab_homedoctorin, null);
                final ImageView titleImg =customLayout.findViewById(R.id.title_img);
                final AppCompatTextView tv_title = customLayout.findViewById(R.id.tv_title);
                final AppCompatTextView tv_text =customLayout.findViewById(R.id.tv_text);
                TextViewUtils.AppCompatTextViewSetText(tv_title, data.get(index).getField(HomeDoctorInMultipleFields.DOCTNAME));
                TextViewUtils.AppCompatTextViewSetText(tv_text, data.get(index).getField(HomeDoctorInMultipleFields.DOCTLEVELNAME));
                GlideUtils.load(getContext(),data.get(index).getField(HomeDoctorInMultipleFields.DOCTHEADIMAGE),titleImg,GlideUtils.USERMODE);

                commonPagerTitleView.setContentView(customLayout);

                commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {

                    @Override
                    public void onSelected(int index, int totalCount) {
                        tv_title.setTextColor(TextViewUtils.getColor(getContext(),R.color.main_text_orange));
                        tv_text.setTextColor(TextViewUtils.getColor(getContext(),R.color.main_text_orange));
                    }

                    @Override
                    public void onDeselected(int index, int totalCount) {
                        tv_title.setTextColor(TextViewUtils.getColor(getContext(),R.color.main_text_black_dark));
                        tv_text.setTextColor(TextViewUtils.getColor(getContext(),R.color.main_text_gary_99));
                    }

                    @Override
                    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
//                        titleImg.setScaleX(1.3f + (0.8f - 1.3f) * leavePercent);
//                        titleImg.setScaleY(1.3f + (0.8f - 1.3f) * leavePercent);
                    }

                    @Override
                    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
//                        titleImg.setScaleX(0.8f + (1.3f - 0.8f) * enterPercent);
//                        titleImg.setScaleY(0.8f + (1.3f - 0.8f) * enterPercent);
                    }
                });

                commonPagerTitleView.setOnClickListener(v -> mViewPager.setCurrentItem(index));

                return commonPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }


}
