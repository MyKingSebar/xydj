package com.yijia.common_yijia.main.robot.setting.remind;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatTextView;
import android.widget.CheckBox;

import com.example.latte.ec.R;
import com.example.latte.ui.recycler.MultipleFields;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.latte.ui.recycler.MultipleRecyclerAdapter;
import com.example.latte.ui.recycler.MultipleViewHolder;
import com.example.yijia.ui.TextViewUtils;
import com.yijia.common_yijia.main.friends.CommonStringClickListener;
import com.yijia.common_yijia.main.index.YjIndexItemType;

import java.util.List;

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
public final class RobotRemindSettingAddWeekAdapter extends MultipleRecyclerAdapter {
    CommonStringClickListener mCommonListener = null;
    private boolean[] checks = {true, true, true, true, true, true, true};
    private final List<Integer> checkint;

    public boolean[] getChecks() {
        return checks;
    }


    public RobotRemindSettingAddWeekAdapter(List<MultipleItemEntity> data, List<Integer> checkint) {
        super(data);
        //添加item布局
        this.checkint = checkint;
        if (checkint != null) {
            int isize = checkint.size();
            int jsize = checks.length;
            for (int j = 0; j < jsize; j++) {
                checks[j] = false;
            }
            for (int i = 0; i < isize; i++) {
                checks[checkint.get(i) - 1] = true;
            }
        }
        addItemType(YjIndexItemType.ROBOT_REMINDSETTING_ADD_WEEK, R.layout.item_robot_remind_setting_add_week);
    }


    @Override
    protected void convert(MultipleViewHolder holder, final MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()) {
            case YjIndexItemType.ROBOT_REMINDSETTING_ADD_WEEK:
//                final int positopn = holder.getAdapterPosition();
                //先取出所有值
                final int weekid = entity.getField(MultipleFields.ID);
                final String title = entity.getField(MultipleFields.NAME);

                //取出所以控件
                final AppCompatTextView tvName = holder.getView(R.id.tv_name);
                final CheckBox cb = holder.getView(R.id.cb);
                cb.setChecked(checks[weekid-1]);
                final ConstraintLayout cl = holder.getView(R.id.cl);
                cl.setOnClickListener(v -> {
                    if (cb.isChecked()) {
                        cb.setChecked(false);
                        checks[weekid-1] = false;
                    } else {
                        cb.setChecked(true);
                        checks[weekid-1] = true;
                    }
                });
                //赋值
                TextViewUtils.AppCompatTextViewSetText(tvName, title);

                break;

            default:
                break;
        }
    }
}
