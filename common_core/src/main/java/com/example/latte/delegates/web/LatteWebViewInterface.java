package com.example.latte.delegates.web;

import android.webkit.JavascriptInterface;

import com.alibaba.fastjson.JSON;
import com.example.latte.delegates.web.event.Event;
import com.example.latte.delegates.web.event.EventManager;

 class LatteWebViewInterface {

    private final WebDelegate DELEGATE;


    public LatteWebViewInterface(WebDelegate delegate) {
        this.DELEGATE = delegate;
    }

    static LatteWebViewInterface create(WebDelegate delegate){
        return new LatteWebViewInterface(delegate);
    }

    //android4.4以后必须有这个注解事件才会响应
    @SuppressWarnings("unused")
    @JavascriptInterface
    public String event(String params){
        final String action=JSON.parseObject(params).getString("action");
        final Event event=EventManager.getInstance().createEvent(action);
        if(event!=null){
            event.setAction(action);
            event.setDelegate(DELEGATE);
            event.setContext(DELEGATE.getContext());
            event.setUrl(DELEGATE.getUrl());
            return event.execute(params);
        }
        return null;
    }
}
