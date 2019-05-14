package com.yijia.common_yijia.main.robot.robotmain;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.latte.ec.R;
import com.example.yijia.delegates.LatteDelegate;
import com.yijia.common_yijia.database.YjDatabaseManager;
import com.yijia.common_yijia.sign.ISignListener;
import com.yijia.common_yijia.sign.YjSignHandler;

public class ParentInfoDelegate extends LatteDelegate {

    private String token = null;

    private RelativeLayout titleLeft;
    private AppCompatTextView title, titleRight;
    private boolean isFirstLogin = false;
    private boolean isFather = true;
    private EditText name, birth, phone;

    private RadioButton sonRadio;

    private TextView done;


    @Override
    public Object setLayout() {
        return R.layout.delegate_add_parent_info;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
        isFather = getArguments().getBoolean(AddParentsDelegate.EXTRA_ISFATHER);
        isFirstLogin = getArguments().getBoolean(AddParentsDelegate.EXTRA_ISFIRST_LOGIN);
        initView(rootView);
    }

    private void initView(View rootView) {
        titleLeft = rootView.findViewById(R.id.tv_back);
        titleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportDelegate().pop();
            }
        });

        title = rootView.findViewById(R.id.tv_title);
        title.setText(isFather ? R.string.add_parents_father_info : R.string.add_parents_mather_info);

        titleRight = rootView.findViewById(R.id.tv_save);
        titleRight.setVisibility(View.GONE);

        name = rootView.findViewById(R.id.parent_info_name_value);
        name.setHint(isFather ? R.string.add_parents_father_name_hint : R.string.add_parents_mather_name_hint);
        birth = rootView.findViewById(R.id.parent_info_birth_value);
        birth.setHint(isFather ? R.string.add_parents_father_birth_hint : R.string.add_parents_mather_birth_hint);
        birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        birth.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, 1970, 0, 1);
                dialog.show();
            }
        });
        phone = rootView.findViewById(R.id.parent_info_phone_value);
        phone.setHint(isFather ? R.string.add_parents_father_phone_hint : R.string.add_parents_mather_phone_hint);

        sonRadio = rootView.findViewById(R.id.parent_info_son);

        done = rootView.findViewById(R.id.parent_info_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportDelegate().hideSoftInput();
                getSupportDelegate().popTo(AddParentsDelegate.class, true);
                if(isFirstLogin) {
                    YjSignHandler.onSkipAddParents(signListener);
                }
            }
        });

    }

    private ISignListener signListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        signListener = (ISignListener) activity;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
    }

}
