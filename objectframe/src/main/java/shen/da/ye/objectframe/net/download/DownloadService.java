package shen.da.ye.objectframe.net.download;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import rx.Observable;

/**
 * @author ChenYe
 *         created by on 2017/11/8 0008. 11:17
 **/

public interface DownloadService {

    /**
     * 下载apk接口
     *
     * @return
     */
    @Streaming
    @GET("NetWorkServlet?filename=voice.apk")
    Observable<ResponseBody> downloadApk();
}
