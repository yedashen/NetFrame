package shen.da.ye.netframe.net;

import android.content.Context;

import java.io.File;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import shen.da.ye.netframe.net.callbacks.IError;
import shen.da.ye.netframe.net.callbacks.IFailure;
import shen.da.ye.netframe.net.callbacks.IRequest;
import shen.da.ye.netframe.net.callbacks.ISuccess;
import shen.da.ye.netframe.net.callbacks.RequestCallbacks;
import shen.da.ye.netframe.net.download.DownloadHandler;
import shen.da.ye.netframe.ui.loader.LatteLoader;
import shen.da.ye.netframe.ui.loader.LoaderStyle;

/**
 * @author ChenYe
 *         created by on 2017/11/8 0008. 08:48
 **/

public class RestClient {


    private static final WeakHashMap<String, Object> PARAMS = RestCreator.getParams();
    private final String URL;
    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    private final LoaderStyle LOADER_STYLE;
    private final Context CONTEXT;
    private final String DOWNLOAD_DIR;
    private final String EXTENSION;
    private final String NAME;
    private final File FILE;
    private final RequestBody BODY;

    RestClient(String url, WeakHashMap<String, Object> params, IRequest request
            , ISuccess success, IFailure failure, IError error,
               LoaderStyle loaderStyle, Context context, String downloadDir
            , String extension, String name, File file, RequestBody body) {
        this.URL = url;
        PARAMS.putAll(params);
        this.REQUEST = request;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
        this.LOADER_STYLE = loaderStyle;
        this.CONTEXT = context;
        this.DOWNLOAD_DIR = downloadDir;
        this.EXTENSION = extension;
        this.NAME = name;
        this.FILE = file;
        this.BODY = body;
    }


    private void request(HttpMethod method) {
        final RestService service = RestCreator.getService();
        Call<String> call = null;

        if (REQUEST != null) {
            REQUEST.onRequestStart();
        }

        if (LOADER_STYLE != null) {
            LatteLoader.showLoading(CONTEXT, LOADER_STYLE);
        }

        switch (method) {
            case GET:
                call = service.get(URL, PARAMS);
                break;
            case POST:
                call = service.post(URL, PARAMS);
                break;
            case PUT:
                call = service.put(URL, PARAMS);
                break;
            case DELETE:
                call = service.delete(URL, PARAMS);
                break;
            case UPLOAD:
                final RequestBody requestBody =
                        RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()), FILE);
                final MultipartBody.Part body =
                        MultipartBody.Part.createFormData("file", FILE.getName(), requestBody);
                call = service.upload(URL, body);
                break;
            case PUT_RAW:
                call = service.putRaw(URL, BODY);
                break;
            default:
                break;
        }

        if (call != null) {
            call.enqueue(getCallback());
        }
    }

    private Callback<String> getCallback() {
        return new RequestCallbacks(REQUEST, SUCCESS, FAILURE, ERROR, LOADER_STYLE);
    }

    public final void get() {
        request(HttpMethod.GET);
    }

    public final void post() {
        if (BODY == null) {
            request(HttpMethod.POST);
        } else {
            if (!PARAMS.isEmpty()) {
                throw new RuntimeException("params must be null!");
            }
            request(HttpMethod.POST_RAW);
        }
    }

    public final void put() {
        if (BODY == null) {
            request(HttpMethod.PUT);
        } else {
            if (!PARAMS.isEmpty()) {
                throw new RuntimeException("params must be null!");
            }
            request(HttpMethod.PUT_RAW);
        }
    }

    public final void delete() {
        request(HttpMethod.DELETE);
    }

    public final void download() {
        new DownloadHandler(URL, REQUEST, DOWNLOAD_DIR, EXTENSION, NAME,
                SUCCESS, FAILURE, ERROR)
                .handleDownload();
    }

    public final void upload() {
        request(HttpMethod.UPLOAD);
    }
}
