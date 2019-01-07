package com.example.latte.ui.recycler;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

public class MultipleItemEntity implements MultiItemEntity {

    private final ReferenceQueue<LinkedHashMap<Object, Object>> ITEM_QUEUE = new ReferenceQueue<>();
    private final LinkedHashMap<Object, Object> MULTYPLE_FIELDS = new LinkedHashMap<>();
    private final SoftReference<LinkedHashMap<Object, Object>> FILELDS_REFERENCE =
            new SoftReference<LinkedHashMap<Object, Object>>(MULTYPLE_FIELDS, ITEM_QUEUE);

     MultipleItemEntity(LinkedHashMap<Object, Object> fields) {
        FILELDS_REFERENCE.get().putAll(fields);
    }

    public static MultipleEntityBuilder builder(){
         return new MultipleEntityBuilder();
    }


    @Override
    public int getItemType() {
        return (int) FILELDS_REFERENCE.get().get(MultipleFields.ITEM_TYPE);
    }

    @SuppressWarnings("unchecked")
    public final <T> T getField(Object key) {
        return (T) FILELDS_REFERENCE.get().get(key);
    }

    public final LinkedHashMap<?, ?> getFields() {
        return FILELDS_REFERENCE.get();
    }

    public final MultipleItemEntity setField(Object key, Object value) {
        FILELDS_REFERENCE.get().put(key, value);
        return this;
    }
}
