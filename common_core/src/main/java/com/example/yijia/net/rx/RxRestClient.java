package com.example.yijia.net.rx;

import android.content.Context;

import com.example.yijia.net.HttpMethod;
import com.example.yijia.net.RestCreator;
import com.example.yijia.ui.loader.LatteLoader;
import com.example.yijia.ui.loader.LoaderStyle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class RxRestClient {

    private final String URL;
    private final WeakHashMap<String, Object> PARAMS = new WeakHashMap<>();
    private final RequestBody BODY;
    private final LoaderStyle LOADER_STYLE;
    private final File FILE;
    private final File[] FILES;
    private final Context CONTEXT;


    public RxRestClient(String url,
                        Map<String, Object> params,
                        RequestBody body,
                        File file,
                        File[] files,
                        Context context,
                        LoaderStyle loaderStyle) {
        this.URL = url;
        PARAMS.clear();
        PARAMS.putAll(params);


        this.BODY = body;
        this.FILE = file;
        this.FILES = files;
        this.CONTEXT = context;
        this.LOADER_STYLE = loaderStyle;
    }

    public static RxRestClientBuilder builder() {
        return new RxRestClientBuilder();
    }

    private Observable<String> request(HttpMethod method) {
        final RxRestService service = RestCreator.getRxRestService();
        Observable<String> observable = null;


        if (LOADER_STYLE != null) {
            LatteLoader.showLoading(CONTEXT, LOADER_STYLE);
        }

        switch (method) {
            case GET:
                observable = service.get(URL, PARAMS);
                break;
            case POST:
                observable = service.post(URL, PARAMS);
                break;
            case POST_RAW:
                observable = service.postRaw(URL, BODY);
                break;
            case PUT:
                observable = service.put(URL, PARAMS);
                break;
            case PUT_RAW:
                observable = service.putRaw(URL, BODY);
                break;
            case DELETE:
                observable = service.delete(URL, PARAMS);
                break;
            case UPLOAD:
                final RequestBody requestBody =
                        RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()), FILE);
                final MultipartBody.Part body =
                        MultipartBody.Part.createFormData("file", FILE.getName());
                observable = RestCreator.getRxRestService().upload(URL, body);
                break;
            case UPLOADWITHPARAM:
                List<MultipartBody.Part> parts = new ArrayList<>(FILES.length);
                for (File file : FILES) {
                    RequestBody requestBody2 =
                            RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()), file);
                    MultipartBody.Part body2 =
                            MultipartBody.Part.createFormData("files", file.getName(), requestBody2);
                    parts.add(body2);
                }

                observable = RestCreator.getRxRestService().uploadwithParam(URL, PARAMS, parts);
                break;
            default:
                break;

        }
        return observable;
    }


    public final Observable<String> get() {
        return request(HttpMethod.GET);
    }

    public final Observable<String> post() {
        if (BODY == null) {
            return request(HttpMethod.POST);
        } else {
            if (!PARAMS.isEmpty()) {
                throw new RuntimeException("params must be null!");
            }
            return request(HttpMethod.POST_RAW);
        }
    }

    public final Observable<String> put() {
        if (BODY == null) {
            return request(HttpMethod.PUT);
        } else {
            if (!PARAMS.isEmpty()) {
                throw new RuntimeException("params must be null!");
            }
            return request(HttpMethod.PUT_RAW);
        }
    }

    public final Observable<String> delete() {
        return request(HttpMethod.DELETE);
    }

    public final Observable<String> upload() {
        return request(HttpMethod.UPLOAD);
    }

    public final Observable<String> uploadwithparams() {
        return request(HttpMethod.UPLOADWITHPARAM);
    }

    public final Observable<ResponseBody> download() {
        final Observable<ResponseBody> responseBodyObservable = RestCreator.getRxRestService().download(URL, PARAMS);
        return responseBodyObservable;

    }
}
