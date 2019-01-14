//package com.yijia.common_yijia.main.message;
//
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//
//import com.example.latte.delegates.LatteDelegate;
//import com.example.latte.ec.R;
//import com.example.latte.ec.R2;
//import com.example.latte.ec.detail.RecyclerImageAdapter;
//import com.example.latte.ui.recycler.ItemType;
//import com.example.latte.ui.recycler.MultipleFields;
//import com.example.latte.ui.recycler.MultipleItemEntity;
//
//import java.util.ArrayList;
//
//import butterknife.BindView;
//
//
//public class RecyeleDelegate extends LatteDelegate {
//
//    @BindView(R2.id.rv_image_container)
//    RecyclerView mRecyclerView = null;
//
//    private static final String ARG_PICTURES = "ARG_PICTURES";
//
//    @Override
//    public Object setLayout() {
//        return R.layout.delegate_recycleview;
//    }
//
//    private void initImages() {
//        final ArrayList<String> pictures =
//                getArguments().getStringArrayList(ARG_PICTURES);
//        final ArrayList<MultipleItemEntity> entities = new ArrayList<>();
//        final int size;
//        if (pictures != null) {
//            size = pictures.size();
//            for (int i = 0; i < size; i++) {
//                final String imageUrl = pictures.get(i);
//                final MultipleItemEntity entity = MultipleItemEntity
//                        .builder()
//                        .setItemType(ItemType.SINGLE_BIG_IMAGE)
//                        .setField(MultipleFields.IMAGE_URL, imageUrl)
//                        .build();
//                entities.add(entity);
//            }
//            final RecyclerImageAdapter adapter = new RecyclerImageAdapter(entities);
//            mRecyclerView.setAdapter(adapter);
//        }
//    }
//
//    public static RecyeleDelegate create(ArrayList<String> pictures) {
//        final Bundle args = new Bundle();
//        args.putStringArrayList(ARG_PICTURES, pictures);
//        final RecyeleDelegate delegate = new RecyeleDelegate();
//        delegate.setArguments(args);
//        return delegate;
//    }
//
//    @Override
//    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
//        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
//        mRecyclerView.setLayoutManager(manager);
//        initImages();
//    }
//}
