package shen.da.ye.netframe.net;

import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author ChenYe
 *         created by on 2017/11/8 0008. 08:45
 **/

public class RestCreator {
    /**
     * (3)下面是一张图片的url
     * http://file.ataw.cn/HospPerformance/Model/Image/2017/06/20/File/20170620173507137A9A7CC4BD991149058A765A34095728CF.jpg?ut=20170620173516
     * <p>
     * （4）登录的
     * private static final String BASE_URL = "http://127.0.0.1:50112/web/rest/";
     * loginService
     * params.put("userno","P31175685");
     * params.put("password","zpepc001@");
     * params.put("sessionid", UUID.randomUUID().toString().replaceAll("-",""));
     */
    private static final class ParamHolder {
        private static final WeakHashMap<String, Object> REQUEST_PARAMS = new WeakHashMap<>();
    }

    public static WeakHashMap<String, Object> getParams() {
        return ParamHolder.REQUEST_PARAMS;
    }

    /**
     * (1)如果是release，最好是把下面的拦截器注释掉,另外这个拦截器会影响下载导致下载两次
     * (2)无论是什么版本，下面这个拦截器会造成不能边下边写到sd中，所以测下载的时候把拦截器注释掉
     */
    private static final class OkHttpHolder {
        private static final int CONN_TIME_OUT = 60;
        private static final int READ_TIME_OUT = 60;
        private static final int WRITE_TIME_OUT = 60;

        private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
//                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(CONN_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                .build();
    }

    private static final class RetrofitHolder {
        private static final String BASE_URL = "http://127.0.0.1:50112/web/";
        private static final Retrofit RETROFIT = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OkHttpHolder.OK_HTTP_CLIENT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    private static final class ServiceHolder {
        private static final RestService REST_SERVICE = RetrofitHolder.RETROFIT
                .create(RestService.class);
    }

    public static RestService getService() {
        return ServiceHolder.REST_SERVICE;
    }
}
