package com.yijia.common_yijia.main.friends.base;

public class BasePresenter <V extends BaseView>{
    public V view;

    public V getView() {
        return view;
    }

    public BasePresenter(V view) {
        this.view = view;
    }

    public void onDestroy() {
        view = null;
    }
}
