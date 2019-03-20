package com.yijia.common_yijia.main.index.friendcircle;

import android.support.v7.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.example.latte.ec.R;
import com.example.latte.ui.recycler.MultipleFields;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.latte.ui.recycler.MultipleRecyclerAdapter;
import com.example.latte.ui.recycler.MultipleViewHolder;
import com.example.yijia.delegates.LatteDelegate;
import com.example.yijia.util.GlideUtils;
import com.yijia.common_yijia.main.index.YjIndexCommentMultipleFields;
import com.yijia.common_yijia.main.index.YjIndexItemType;
import com.yijia.common_yijia.main.index.YjIndexMultipleFields;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public final class LetterPeopleAdapter extends MultipleRecyclerAdapter {
private LatteDelegate mDelegate=null;
    LetterPeopleAdapter(List<MultipleItemEntity> data, LatteDelegate delegate) {
        super(data);
        //添加item布局
        addItemType(YjIndexItemType.INDEX_USERLIST_ITEM, R.layout.item_user_image_text);
        mDelegate=delegate;
    }

    @Override
    protected void convert(MultipleViewHolder holder, final MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()) {
            case YjIndexItemType.INDEX_USERLIST_ITEM:
                //先取出所有值

//                final Long friendUserId = entity.getField(MultipleFields.ID);
//                final String tencentImUserId = entity.getField(MultipleFields.TENCENTIMUSERID);
                final String userHead = entity.getField(MultipleFields.IMAGE_URL);
//                final int userStatus = entity.getField(YjIndexMultipleFields.STATUS);
//                final String realName = entity.getField(YjIndexMultipleFields.USER_REAL_NAME);
                final String nickname = entity.getField(YjIndexMultipleFields.USER_NICK_NAME);

                //取出所以控件
                final AppCompatTextView tvName = holder.getView(R.id.tv_name);
                final CircleImageView civ = holder.getView(R.id.civ);

                //赋值
                tvName.setText(nickname);
                Glide.with(mDelegate)
                        .load(userHead)
                        .apply(GlideUtils.USEROPTIONS)
                        .into(civ);
                //点赞
//                tvZan.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        final String url = "/circle/like/" + circleId;
//                        final String token = YjDatabaseManager.getInstance().getDao().loadAll().get(0).getYjtk();
//                        RxRestClient.builder()
//                                .url(url)
//                                .params("yjtk", token)
//                                .build()
//                                .post()
//                                .subscribeOn(Schedulers.io())
//                                .observeOn(AndroidSchedulers.mainThread())
//                                .subscribe(new BaseObserver<String>(mContext) {
//                                    @Override
//                                    public void onResponse(String response) {
//                                        LatteLogger.json("USER_PROFILE", response);
//                                        final JSONObject object = JSON.parseObject(response);
//                                        final String status = object.getString("status");
//
//                                        if (TextUtils.equals(status, "1001")) {
//                                            final JSONObject data = object.getJSONObject("data");
//                                            final int likeStatus = object.getInteger("likeStatus");
//                                            switch (likeStatus) {
//                                                case LIKE:
//                                                    tvZan.setBackgroundResource(R.mipmap.icon_zan_c);
//                                                    break;
//                                                case DISLIKE:
//                                                    tvZan.setBackgroundResource(R.mipmap.icon_zan);
//                                                    break;
//                                                default:
//                                                    tvZan.setBackgroundResource(R.mipmap.icon_zan);
//                                                    break;
//                                            }
//                                        } else {
//                                            final String msg = JSON.parseObject(response).getString("status");
//                                            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onFail(Throwable e) {
//                                        Toast.makeText(mContext, "请稍后尝试", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                    }
//                });


                break;

            default:
                break;
        }
    }
}
