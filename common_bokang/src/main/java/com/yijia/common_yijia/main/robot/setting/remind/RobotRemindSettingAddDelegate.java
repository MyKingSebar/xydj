package com.yijia.common_yijia.main.robot.setting.remind;

import android.annotation.SuppressLint;
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
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.latte.ec.R;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.latte.ui.recycler.SpacesItemDecoration;
import com.example.yijia.app.Latte;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.net.rx.BaseObserver;
import com.example.yijia.net.rx.RxRestClient;
import com.example.yijia.ui.TextViewUtils;
import com.example.yijia.ui.seekbar.RxSeekBar;
import com.example.yijia.util.callback.CallbackIntegerManager;
import com.example.yijia.util.callback.CallbackManager;
import com.example.yijia.util.callback.CallbackType;
import com.example.yijia.util.callback.IGlobalCallback;
import com.yijia.common_yijia.database.YjDatabaseManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author JiaLei, Email 15033111957@163.com, Date on 2019/5/13.
 * <p>
 * .,,       .,:;;iiiiiiiii;;:,,.     .,,
 * rGB##HS,.;iirrrrriiiiiiiiiirrrrri;,s&##MAS,
 * r5s;:r3AH5iiiii;;;;;;;;;;;;;;;;iiirXHGSsiih1,
 * .;i;;s91;;;;;;::::::::::::;;;;iS5;;;ii:
 * :rsriii;;r::::::::::::::::::::::;;,;;iiirsi,
 * .,iri;;::::;;;;;;::,,,,,,,,,,,,,..,,;;;;;;;;iiri,,.
 * ,9BM&,            .,:;;:,,,,,,,,,,,hXA8:            ..,,,.
 * ,;&@@#r:;;;;;::::,,.   ,r,,,,,,,,,,iA@@@s,,:::;;;::,,.   .;.
 * :ih1iii;;;;;::::;;;;;;;:,,,,,,,,,,;i55r;;;;;;;;;iiirrrr,..
 * .ir;;iiiiiiiiii;;;;::::::,,,,,,,:::::,,:;;;iiiiiiiiiiiiri
 * iriiiiiiiiiiiiiiii;;;::::::::::::::::;;;iiiiiiiiiiiiiiiir;
 * ,riii;;;;;;;;;;;;;:::::::::::::::::::::::;;;;;;;;;;;;;;iiir.
 * iri;;;::::,,,,,,,,,,:::::::::::::::::::::::::,::,,::::;;iir:
 * .rii;;::::,,,,,,,,,,,,:::::::::::::::::,,,,,,,,,,,,,::::;;iri
 * ,rii;;;::,,,,,,,,,,,,,:::::::::::,:::::,,,,,,,,,,,,,:::;;;iir.
 * ,rii;;i::,,,,,,,,,,,,,:::::::::::::::::,,,,,,,,,,,,,,::i;;iir.
 * ,rii;;r::,,,,,,,,,,,,,:,:::::,:,:::::::,,,,,,,,,,,,,::;r;;iir.
 * .rii;;rr,:,,,,,,,,,,,,,,:::::::::::::::,,,,,,,,,,,,,:,si;;iri
 * ;rii;:1i,,,,,,,,,,,,,,,,,,:::::::::,,,,,,,,,,,,,,,:,ss:;iir:
 * .rii;;;5r,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,sh:;;iri
 * ;rii;:;51,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,.:hh:;;iir,
 * irii;::hSr,.,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,.,sSs:;;iir:
 * irii;;:iSSs:.,,,,,,,,,,,,,,,,,,,,,,,,,,,..:135;:;;iir:
 * ;rii;;:,r535r:...,,,,,,,,,,,,,,,,,,..,;sS35i,;;iirr:
 * :rrii;;:,;1S3Shs;:,............,:is533Ss:,;;;iiri,
 * .;rrii;;;:,;rhS393S55hh11hh5S3393Shr:,:;;;iirr:
 * .;rriii;;;::,:;is1h555555h1si;:,::;;;iirri:.
 * .:irrrii;;;;;:::,,,,,,,,:::;;;;iiirrr;,
 * .:irrrriiiiii;;;;;;;;iiiiiirrrr;,.
 * .,:;iirrrrrrrrrrrrrrrrri;:.
 * ..,:::;;;;:::,,.
 */
public class RobotRemindSettingAddDelegate extends LatteDelegate {
    public static final String USERID = "userid";
    public static final String REMINDTYPEID = "remindTypeId";
    public static final String REMINDID = "remindId";
    AppCompatTextView tvTitle;
    RelativeLayout rl;
    String token = null;
    long userId = 0;
    long remindTypeId = 0;
    long remindId = 0;
    ConstraintLayout clRepetition, clTag;
    AppCompatTextView tvRepetition, tvTag;
    FrameLayout cl_choose1;
    String tag = null;


    ArrayList<Integer> whatDay = new ArrayList<>();
    String whatDayString = null;


    OptionsPickerView pvOptions;
    private ArrayList<PickBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    String[] s1 = {"00时00分", "00时30分", "01时00分", "01时30分", "02时00分", "02时30分", "03时00分", "03时30分", "04时00分", "04时30分", "05时00分", "05时30分", "06时00分", "06时30分", "07时00分", "07时30分", "08时00分", "08时30分", "09时00分", "09时30分", "10时00分", "10时30分", "11时00分", "11时30分", "12时00分", "12时30分", "13时00分", "13时30分", "14时00分", "14时30分", "15时00分", "15时30分", "16时00分", "16时30分", "17时00分", "17时30分", "18时00分", "18时30分", "19时00分", "19时30分", "20时00分", "20时30分", "21时00分", "21时30分", "22时00分", "22时30分", "23时00分", "23时30分"};
    DateFormat format1 = new SimpleDateFormat("HH时mm分");
    DateFormat format2 = new SimpleDateFormat("HH:mm:ss");
    Date date = null;
    Date date2 = null;

    String startTime = s1[0];
    String endTime = s1[1];

    @Override
    public Object setLayout() {
        return R.layout.delegate_robot_setting_remind_add;
    }

    public static RobotRemindSettingAddDelegate create(long userid, long remindTypeId, long remindId) {
        final Bundle args = new Bundle();
        args.putLong(USERID, userid);
        args.putLong(REMINDTYPEID, remindTypeId);
        args.putLong(REMINDID, remindId);

        final RobotRemindSettingAddDelegate delegate = new RobotRemindSettingAddDelegate();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        assert args != null;
        userId = args.getLong(USERID);
        remindTypeId = args.getLong(REMINDTYPEID);
        remindId = args.getLong(REMINDID);
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {


        token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        initVIew(rootView);
        initPick();
        initdata();
    }

    private void initdata() {
        if (remindId == 0) {
            return;
        }
        getData();
    }

    private void getData() {
        RxRestClient.builder()
                .url("remind/query_remind")
                .params("yjtk", token)
                .params("remindId", remindId)
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
                            final JSONObject data = obj.getJSONObject("data");
                            remindTypeId = data.getLong("remindTypeId");
                            userId = data.getLong("targetUserId");
                            startTime = data.getString("startTime");
                            endTime = data.getString("endTime");
                            tag = data.getString("tag");
                            TextViewUtils.AppCompatTextViewSetText(tvTag, tag);
                            whatDayString = data.getString("whatDay");
                            String[] s = whatDayString.split(",");
                            int length = s.length;
                            if (length == 7) {
                                TextViewUtils.AppCompatTextViewSetText(tvRepetition, "每天");
                            } else if (length == 1) {
                                int i = Integer.parseInt(s[0]);
                                TextViewUtils.AppCompatTextViewSetText(tvRepetition, RobotRemindSettingAddWeekDataConverter.week[i - 1]);
                            } else if (length > 0) {
                                int i = Integer.parseInt(s[0]);
                                TextViewUtils.AppCompatTextViewSetText(tvRepetition, RobotRemindSettingAddWeekDataConverter.week[i - 1] + "等");
                            }
                            whatDay.clear();
                            for (int i = 0; i < length; i++) {
                                whatDay.add(Integer.parseInt(s[i]));
                            }
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

    private void initPick() {
        for (int i = 0; i < s1.length; i++) {
            options1Items.add(new PickBean(i, s1[i], "描述部分", "其他数据"));
            ArrayList<String> item = new ArrayList<>();
            for (int j = 0 + i + 1; j < s1.length; j++) {
                item.add(s1[j]);
            }
            options2Items.add(item);
        }
        initOptionPicker();
    }

    private void initOptionPicker() {//条件选择器初始化
        try {
            date = format1.parse(options1Items.get(0).getName());
            date2 = format1.parse(options1Items.get(0 + 0 + 1).getName());
            String ss1 = format2.format(date);
            String ss2 = format2.format(date2);
            startTime = ss1;
            endTime = ss2;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /**
         * 注意 ：如果是三级联动的数据(省市区等)，请参照 JsonDataActivity 类里面的写法。
         */

        pvOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText()
                        + options2Items.get(options1).get(options2)
                        /* + options3Items.get(options1).get(options2).get(options3).getPickerViewText()*/;
                System.out.println(tx);
//                btn_Options.setText(tx);
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.rl);
                        TextView add = (TextView) v.findViewById(R.id.tv_add);
                        rl.setVisibility(View.GONE);
                        add.setVisibility(View.GONE);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                pvTime.returnData();
                                /*pvTime.dismiss();*/
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                /*pvTime.dismiss();*/
                            }
                        });
                    }
                })
////                .setType(new boolean[]{true, true, true, false, false, false})
////                .setLabel("", "", "", "", "", "") //设置空字符串以隐藏单位提示   hide label
//                .setDividerColor(Color.DKGRAY)
//                .setContentTextSize(20)
////                .setDate(selectedDate)
////                .setRangDate(startDate, selectedDate)
                .setDecorView(cl_choose1)//非dialog模式下,设置ViewGroup, pickerView将会添加到这个ViewGroup中
//                .setOutSideColor(0x00000000)
//                .setOutSideCancelable(false)
                .setTitleText("选择")
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setSelectOptions(0, 1)//默认选中项
                .setBgColor(Color.WHITE)
                .setTitleBgColor(Color.DKGRAY)
                .setTitleColor(Color.LTGRAY)
                .setCancelColor(Color.YELLOW)
                .setSubmitColor(Color.YELLOW)
                .setTextColorCenter(Color.BLACK)
                .isRestoreItem(false)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabels("", "", "")
//                .setOutSideColor(0x00000000) //设置外部遮罩颜色
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        try {
                            date = format1.parse(options1Items.get(options1).getName());
                            if (options2 + options1 + 1 > options1Items.size() - 1) {
//                                Toast.makeText(getContext(), "~", Toast.LENGTH_SHORT).show();
                                return;
                            } else {

                                date2 = format1.parse(options1Items.get(options2 + options1 + 1).getName());
                                String ss1 = format2.format(date);
                                String ss2 = format2.format(date2);
                                startTime = ss1;
                                endTime = ss2;
//                                Toast.makeText(getContext(), ss1+"~"+ss2, Toast.LENGTH_SHORT).show();
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .build();

//        pvOptions.setSelectOptions(1,1);
        /*pvOptions.setPicker(options1Items);//一级选择器*/
        pvOptions.setPicker(options1Items, options2Items);//二级选择器
        /*pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器*/
        pvOptions.setKeyBackCancelable(false);//系统返回键监听屏蔽掉
        pvOptions.show();
    }

    private void initVIew(View rootView) {
        rl = rootView.findViewById(R.id.tv_back);
        rl.setOnClickListener(v -> {
            getSupportDelegate().pop();
        });
        tvTitle = rootView.findViewById(R.id.tv_title);
        TextViewUtils.AppCompatTextViewSetText(tvTitle, "添加提醒");
        AppCompatTextView tvSave = rootView.findViewById(R.id.tv_save);
        TextViewUtils.AppCompatTextViewSetText(tvSave, "保存");
        tvSave.setOnClickListener(v -> {
            if (remindId == 0) {
                insertRemind(token, userId);
            } else {
                updateRemind(token, userId);
            }

        });
        clRepetition = rootView.findViewById(R.id.cl_repetition);
        clTag = rootView.findViewById(R.id.cl_tag);
        tvTag = rootView.findViewById(R.id.tv_tag);
        tvRepetition = rootView.findViewById(R.id.textView3);
        cl_choose1 = rootView.findViewById(R.id.cl_choose1);
        for (int i = 0; i < 7; i++) {
            whatDay.add(i + 1);
        }
        whatDayString = "1,2,3,4,5,6,7";
        TextViewUtils.AppCompatTextViewSetText(tvRepetition, "每天");
        clRepetition.setOnClickListener(v -> {
            RobotRemindSettingAddWeekDelegate mRobotRemindSettingAddWeekDelegate = RobotRemindSettingAddWeekDelegate.create(whatDay);
            getSupportDelegate().start(mRobotRemindSettingAddWeekDelegate);
        });
        clTag.setOnClickListener(v -> {
            RobotRemindSettingAddTagDelegate mRobotRemindSettingAddTagDelegate = RobotRemindSettingAddTagDelegate.create(tvTag.getText().toString());
            getSupportDelegate().start(mRobotRemindSettingAddTagDelegate);
        });

        CallbackManager.getInstance()
                .addCallback(CallbackType.ROBOT_REMIND_WEEK, (IGlobalCallback<String>) args -> {
                    if (args == null) {
                        return;
                    }
                    String[] strings = args.split(",");
                    int size = strings.length;
                    whatDay.clear();
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < size; i++) {
                        int day = Integer.parseInt(strings[i]);
                        if (i == 0) {
                            sb.append(RobotRemindSettingAddWeekDataConverter.week[day - 1]);
                        }
                        whatDay.add(day);
                    }
                    if (whatDay.size() > 1) {
                        sb.append("等");
                    }
                    TextViewUtils.AppCompatTextViewSetText(tvRepetition, sb.toString());
                });
        CallbackManager.getInstance()
                .addCallback(CallbackType.ROBOT_REMIND_TAG, (IGlobalCallback<String>) args -> {
                    if (args == null) {
                        return;
                    }
                    TextViewUtils.AppCompatTextViewSetText(tvTag, args.toString());
                });
    }

    private String whatDayToString(List<Integer> list) {
        if (list == null) {
            showToast("list==null");
            return "";
        }
        int size = list.size();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < size; i++) {

            if (i != 0) {
                sb.append(",");
            }
            sb.append(list.get(i));
        }
        return sb.toString();
    }

    private void insertRemind(String token, long userId) {
        if (userId == 0) {
            showToast("网络异常");
            return;
        }
        tag = tvTag.getText().toString();
        if (TextUtils.isEmpty(tag)) {
            showToast("请输入标签");
            return;
        }
        if (null == whatDay || whatDay.size() == 0) {
            showToast("请选择重复日期");
            return;
        }
        whatDayString = whatDayToString(whatDay);
        RxRestClient.builder()
                .url("remind/insert_remind")
                .params("yjtk", token)
                .params("remindTypeId", remindTypeId)
                .params("tag", tag)
                .params("whatDay", whatDayString)
                .params("startTime", startTime)
                .params("endTime", endTime)
                .params("targetUserId", userId)
                .build()
                .post()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        final JSONObject obj = JSON.parseObject(response);
                        final String status = obj.getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            showToast("添加成功");
                            beforeExit("");
                            getSupportDelegate().popTo(RobotRemindSettingDelegate.class, false);
//                            List<MultipleItemEntity> data = new RobotRemindSettingDataConverter()
//                                    .setJsonData(response)
//                                    .convert();

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

    private void updateRemind(String token, long userId) {
        if (userId == 0) {
            showToast("网络异常");
            return;
        }
        tag = tvTag.getText().toString();
        if (TextUtils.isEmpty(tag)) {
            showToast("请输入标签");
            return;
        }
        if (null == whatDay || whatDay.size() == 0) {
            showToast("请选择重复日期");
            return;
        }
        whatDayString = whatDayToString(whatDay);
        RxRestClient.builder()
                .url("remind/update_remind")
                .params("yjtk", token)
                .params("id", remindId)
                .params("remindTypeId", remindTypeId)
                .params("tag", tag)
                .params("whatDay", whatDayString)
                .params("startTime", startTime)
                .params("endTime", endTime)
                .params("targetUserId", userId)
                .build()
                .post()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getContext()) {
                    @Override
                    public void onResponse(String response) {
                        final JSONObject obj = JSON.parseObject(response);
                        final String status = obj.getString("status");
                        if (TextUtils.equals(status, "1001")) {
                            showToast("添加成功");
                            beforeExit("");
                            getSupportDelegate().popTo(RobotRemindSettingDelegate.class, false);
//                            List<MultipleItemEntity> data = new RobotRemindSettingDataConverter()
//                                    .setJsonData(response)
//                                    .convert();

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

    private void beforeExit(String s) {
        final IGlobalCallback<String> callback;
        callback = CallbackManager
                .getInstance()
                .getCallback(CallbackType.ROBOT_REMIND_ADD);
        if (callback != null) {
            callback.executeCallback(s);
        }
    }
}
