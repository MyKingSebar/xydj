package com.yijia.common_yijia.main.mine;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.latte.ec.R;
import com.example.latte.ec.R2;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.ui.dialog.JDialogUtil;
import com.example.yijia.util.callback.CallbackManager;
import com.example.yijia.util.callback.CallbackType;
import com.example.yijia.util.callback.IGlobalCallback;
import com.google.gson.Gson;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.index.GuardianshipDataConverter;
import com.yijia.common_yijia.main.index.friendcircle.choosefriend.LetterchoosefriendDelegate;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GuardianshipDelegate extends LatteDelegate implements GuardianshipListener {
    @BindView(R2.id.tv_save)
    AppCompatTextView tvSave;
    @BindView(R2.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R2.id.tv_icon)
    ImageView tvIcon;
    @BindView(R2.id.rv)
    RecyclerView rv;

    GuardianshipAdapter mAdapter = null;
    ArrayList<MultipleItemEntity> data = null;
    String token = null;
    long friendId=0;

    private final int QUERYTYPE=1;
    private final int PAGESIZE=100;

    @Override
    public Object setLayout() {
        return R.layout.delegate_guardianship;
    }

    @OnClick(R2.id.tv_back)
    void back() {
        getSupportDelegate().pop();
    }

    @OnClick(R2.id.tv_icon)
    void add() {
        LetterchoosefriendDelegate delegate = new LetterchoosefriendDelegate();
        Bundle mArgs = new Bundle();
        mArgs.putString(LetterchoosefriendDelegate.CHOOSEFRIENDTYPEKEY, LetterchoosefriendDelegate.choosefriendType.GUARDIANSHIPCHOOSEFRIEND.name());
        delegate.setArguments(mArgs);
        getSupportDelegate().start(delegate);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        initView();
        initCallback();
        getGuardianshipInfo(token);
    }

    private void initCallback() {
        CallbackManager.getInstance()
                .addCallback(CallbackType.FRAGMENT_GUARDIANSHIP_CHOOSEFRIEND_RESULT, new IGlobalCallback<String>() {
                    @Override
                    public void executeCallback(@Nullable String args) {
                        assert args != null;
                        String[] arg = args.split(",");
                        if (arg.length == 2) {
                            friendId = Long.parseLong(arg[1]);
                            if(friendId!=0){
                                JDialogUtil.INSTANCE.showRxDialogShapeLoading(getContext());
                                saveGuardianshipInfo(token,friendId);
                            }
                        }
                    }
                });

        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(manager);


    }

    private void initView() {
        tvTitle.setText("监护人管理");
        tvIcon.setVisibility(View.VISIBLE);
        tvSave.setVisibility(View.GONE);
    }

    private void getGuardianshipInfo(String token) {
        String url = "guardianship/query_guardianship/"+QUERYTYPE+"/1/"+PAGESIZE;
        RxRestClient.builder()
                .url(url)
                .params("yjtk", token)
//                .params("queryType", 3)
//                .params("pageNo", 1)
//                .params("pageSize", 100)
                .build()
                .get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        final String status = JSON.parseObject(response).getString("status");
                        Log.e("jialei","query_guardianship"+new Gson().toJson(response));
                        if (TextUtils.equals(status, "1001")) {
                            data = new GuardianshipDataConverter()
                                    .setJsonData(response)
                                    .convert();
                            mAdapter = new GuardianshipAdapter(data, GuardianshipDelegate.this);
                            mAdapter.setmGuardianshipListener(GuardianshipDelegate.this);
                            mAdapter.setmCommonClickListener(v->{
                                showToast("长按删除");
                            });
                            rv.setAdapter(mAdapter);
                            if (data.size() > 0) {
                                tvIcon.setVisibility(View.GONE);
                            } else {
                                tvIcon.setVisibility(View.VISIBLE);
                            }
                            JDialogUtil.INSTANCE.dismiss();
                        } else {
                            final String msg = JSON.parseObject(response).getString("msg");
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                            JDialogUtil.INSTANCE.dismiss();
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        JDialogUtil.INSTANCE.dismiss();
                    }
                });
    }

    private void deleteGuardianshipInfo(String token, long guardianUserId) {
        String url = "guardianship/delete_guardian";
        RxRestClient.builder()
                .url(url)
                .params("yjtk", token)
                .params("guardianUserId", guardianUserId)
                .build()
                .post()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        final String status = JSON.parseObject(response).getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                            data = new ArrayList<MultipleItemEntity>();
                            mAdapter = new GuardianshipAdapter(data, GuardianshipDelegate.this);
                            rv.setAdapter(mAdapter);
                            tvIcon.setVisibility(View.VISIBLE);
                        } else {
                            final String msg = JSON.parseObject(response).getString("msg");
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void saveGuardianshipInfo(String token, long guardianUserId) {
        String url = "guardianship/save_guardian";
        RxRestClient.builder()
                .url(url)
                .params("yjtk", token)
                .params("guardianUserId", guardianUserId)
                .build()
                .post()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        final String status = JSON.parseObject(response).getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            Toast.makeText(getContext(), "添加成功", Toast.LENGTH_SHORT).show();
                            getGuardianshipInfo(token);
                        } else {
                            final String msg = JSON.parseObject(response).getString("msg");
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                            JDialogUtil.INSTANCE.dismiss();
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        JDialogUtil.INSTANCE.dismiss();
                    }
                });
    }


    @Override
    public void onitemclick(long id) {
        deleteGuardianshipInfo(token, id);
    }
}
