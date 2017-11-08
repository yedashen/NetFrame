package shen.da.ye.objectframe.net.download;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import shen.da.ye.objectframe.ObjectNetFrameApp;
import shen.da.ye.objectframe.net.CallAdapterFactory;

/**
 * @author ChenYe
 *         created by on 2017/11/8 0008. 11:16
 **/

public class DownloadCreator {

    private static final class DownloadProgressHolder {
        private static final LocalBroadcastManager LOCAL_BROADCAST_MANAGER = LocalBroadcastManager.getInstance(ObjectNetFrameApp.mShareInstance);
        private static final Intent INTENT = new Intent("message_progress");
        private static final DownloadProgressListener DOWNLOAD_PROGRESS_LISTENER = new DownloadProgressListener() {
            @Override
            public void update(long bytesRead, long contentLength, boolean done) {
                INTENT.putExtra("currentRead", bytesRead);
                INTENT.putExtra("total", contentLength);
                LOCAL_BROADCAST_MANAGER.sendBroadcast(INTENT);
            }
        };
    }

    private static final class InterceptorHolder {
        private static final DownloadInterceptor DOWNLOAD_INTERCEPTOR = new DownloadInterceptor(DownloadProgressHolder.DOWNLOAD_PROGRESS_LISTENER);
    }

    private static final class OkHttpHolder {
        private static final int CONN_TIME_OUT = 60;
        private static final int READ_TIME_OUT = 60;
        private static final int WRITE_TIME_OUT = 60;

        private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
                .connectTimeout(CONN_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(InterceptorHolder.DOWNLOAD_INTERCEPTOR)
                .retryOnConnectionFailure(true)
                .build();
    }

    private static final class RetrofitHolder {
        private static final String BASE_URL = "http://127.0.0.1:50112/web/";
        private static final Retrofit RETROFIT = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OkHttpHolder.OK_HTTP_CLIENT)
                .addCallAdapterFactory(CallAdapterFactory.create())
                .build();
    }

    private static final class ServiceHolder {
        private static final DownloadService REST_SERVICE = RetrofitHolder.RETROFIT
                .create(DownloadService.class);
    }

    public static DownloadService getService() {
        return ServiceHolder.REST_SERVICE;
    }
}
