package com.yijia.common_yijia.main.index;

import android.support.v7.widget.AppCompatTextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.latte.ec.R;
import com.example.latte.ec.main.cart.ICartItemListener;
import com.example.latte.ui.recycler.MultipleItemEntity;
import com.example.latte.ui.recycler.MultipleRecyclerAdapter;
import com.example.latte.ui.recycler.MultipleViewHolder;

import java.util.List;


public final class YjIndexCommentAdapter extends MultipleRecyclerAdapter {


    private ICartItemListener mCartItemListener = null;

    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate();

    YjIndexCommentAdapter(List<MultipleItemEntity> data) {
        super(data);
        //添加item布局
        addItemType(YjIndexItemType.INDEX_COMMENTLIST_ITEM, R.layout.item_index_comment);
    }


    public void setCartItemListener(ICartItemListener listener) {
        this.mCartItemListener = listener;
    }


    @Override
    protected void convert(MultipleViewHolder holder, final MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()) {
            case YjIndexItemType.INDEX_COMMENTLIST_ITEM:
                //先取出所有值
                final Long commentId = entity.getField(YjIndexCommentMultipleFields.COMMENTID);
                final Long commentUserId = entity.getField(YjIndexCommentMultipleFields.COMMENTUSERID);
                final String commentUserNickname = entity.getField(YjIndexCommentMultipleFields.COMMENTUSERNICKNAME);
                final String commentUserRealName = entity.getField(YjIndexCommentMultipleFields.COMMENTUSERREALNAME);
                final String commentUserHead = entity.getField(YjIndexCommentMultipleFields.COMMENTUSERHEAD);
                final String commentContent = entity.getField(YjIndexCommentMultipleFields.COMMENTCONTENT);
                final int replyUserId = entity.getField(YjIndexCommentMultipleFields.REPLYUSERID);
                final String replyUserNickname = entity.getField(YjIndexCommentMultipleFields.REPLYUSERNICKNAME);
                final String replyUserRealName = entity.getField(YjIndexCommentMultipleFields.REPLYUSERREALNAME);
                final String commentCreatedTime = entity.getField(YjIndexCommentMultipleFields.COMMENTCREATEDTIME);
                final int isOwnComment = entity.getField(YjIndexCommentMultipleFields.ISOWNCOMMENT);

                //取出所以控件
                final AppCompatTextView tvName = holder.getView(R.id.tv_name);
                final AppCompatTextView tvComment = holder.getView(R.id.tv_comment);

                //赋值
                tvName.setText(commentUserNickname + ":");
                StringBuffer comment = new StringBuffer();
                if (replyUserId != 0) {
                    comment.append("@").append(replyUserNickname);
                }
                comment.append(commentContent);
                tvComment.setText(comment.toString());

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
