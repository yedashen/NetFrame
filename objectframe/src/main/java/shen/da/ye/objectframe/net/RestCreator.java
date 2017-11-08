package shen.da.ye.objectframe.net;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author ChenYe
 *         created by on 2017/11/6 0006. 10:11
 **/

public class RestCreator {

    /**
     * 如果是release，最好是把下面的拦截器注释掉,另外这个拦截器会影响下载导致下载两次
     */
    private static final class OkHttpHolder {
        private static final int CONN_TIME_OUT = 15;
        private static final int READ_TIME_OUT = 10;
        private static final int WRITE_TIME_OUT = 10;

        private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }

    private static final class RetrofitHolder {
        private static final String BASE_URL = "http://127.0.0.1:50112/web/rest/";
        private static final Retrofit RETROFIT = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OkHttpHolder.OK_HTTP_CLIENT)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CallAdapterFactory.create())
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
