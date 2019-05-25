package com.yijia.common_yijia.main.index.invite;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
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
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.main.index.InviteDelagate;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author JiaLei, Email 15033111957@163.com, Date on 2019/5/20.
 * <p>
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
public class InviteRelationshipDelegate extends LatteDelegate {
    public static final String FAMILYID = "familyId";
    AppCompatTextView tvTitle;
    RecyclerView rv;
    String token = null;
    long familyId = 0;
    InviteRelationshipAdapter mAdapter = null;
    RelativeLayout rl;
    boolean isFirst = true;

    @Override
    public Object setLayout() {
        return R.layout.delegate_robot_setting_remind_add_type;
    }

    public static InviteRelationshipDelegate create(long userid) {
        final Bundle args = new Bundle();
        args.putLong(FAMILYID, userid);
        final InviteRelationshipDelegate delegate = new InviteRelationshipDelegate();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        assert args != null;
        familyId = args.getLong(FAMILYID);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (!isFirst) {
            getSupportDelegate().pop();
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        isFirst = false;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        initVIew(rootView);
    }

    private void initVIew(View rootView) {
        rl = rootView.findViewById(R.id.tv_back);
        rl.setOnClickListener(v -> getSupportDelegate().pop());
        tvTitle = rootView.findViewById(R.id.tv_title);
        TextViewUtils.AppCompatTextViewSetText(tvTitle, "选择关系");
        AppCompatTextView tvSave = rootView.findViewById(R.id.tv_save);
        tvSave.setVisibility(View.GONE);
//        tvSave.setOnClickListener(v->{
//
//        });
        rv = rootView.findViewById(R.id.rv);
//        rv.addItemDecoration(new SpacesItemDecoration(1));
        getInfo(token);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (familyId == 0) {
            InviteDelagate mDelegate = InviteDelagate.create(familyId, 0, InviteDelagate.INVITE_FOR_MINE);
            getSupportDelegate().start(mDelegate);
        }
    }

    private void getInfo(String token) {
        RxRestClient.builder()
                .url("family/query_relation_type")
                .params("yjtk", token)
                .params("category", 2)
                .build()
                .get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        final JSONObject obj = JSON.parseObject(response);
                        final String status = obj.getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            List<MultipleItemEntity> data = new InviteRelationshipDataConverter()
                                    .setJsonData(response)
                                    .convert();
                            initAdapter(data);
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


    private void initAdapter(List<MultipleItemEntity> data) {
        mAdapter = new InviteRelationshipAdapter(data);
        mAdapter.setCommonListener(param -> {
            if (TextUtils.isEmpty(param)) {
                showToast("param==null");
                return;
            }
            long itemId = Long.parseLong(param);
            InviteDelagate mDelegate = InviteDelagate.create(familyId, itemId, InviteDelagate.INVITE_FOR_OTHER);
            getSupportDelegate().start(mDelegate);
        });
        final LinearLayoutManager manager = new LinearLayoutManager(Latte.getApplicationContext());
        rv.setLayoutManager(manager);
        rv.setAdapter(mAdapter);
    }
}


