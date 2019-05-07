package com.example.yijia.util.callback;

import java.util.WeakHashMap;


public class CallbackIntegerManager {

    private static final WeakHashMap<Integer, IGlobalCallback> CALLBACKS = new WeakHashMap<>();

    private static class Holder {
        private static final CallbackIntegerManager INSTANCE = new CallbackIntegerManager();
    }

    public static CallbackIntegerManager getInstance() {
        return Holder.INSTANCE;
    }

    public CallbackIntegerManager addCallback(Integer tag, IGlobalCallback callback) {
        CALLBACKS.put(tag, callback);
        return this;
    }

    public IGlobalCallback getCallback(Integer tag) {
        return CALLBACKS.get(tag);
    }
}
