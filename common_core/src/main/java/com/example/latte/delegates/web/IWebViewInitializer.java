package com.example.latte.delegates.web;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public interface IWebViewInitializer {

    WebView initWebView(WebView webView);

    //WebViewClient浏览器本身控制
    WebViewClient initWebViewClient();

    //WebChromeClient内部页面的控制
    WebChromeClient initWebChromeClient();


}
